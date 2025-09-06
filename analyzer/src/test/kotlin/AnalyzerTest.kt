import config.AnalyzerConfig
import node.Statement
import coordinates.Position
import diagnostic.Diagnostic
import node.AssignmentStatement
import node.BinaryExpression
import node.DeclarationStatement
import node.ExpressionStatement
import node.IdentifierExpression
import node.LiteralExpression
import node.PrintStatement
import node.Program
import type.CommonTypes
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import rules.IdentifierStyle
import java.io.File
import kotlin.collections.get

class AnalyzerTest {
    private fun runAnalyzer(
        stmts: List<Statement>,
        config: AnalyzerConfig = AnalyzerConfig(),
    ): List<Diagnostic> {
        // write config to temp JSON file
        val tempConfigFile =
            File.createTempFile("analyzer", ".json").apply {
                writeText(
                    """
                    {
                       "identifierStyle":"${config.identifierStyle}",
                       "restrictPrintlnArgs":${config.restrictPrintlnArgs}
                    }
                    """.trimIndent(),
                )
                deleteOnExit()
            }

        val program = Program(stmts)
        val analyzer = DefaultAnalyzer(tempConfigFile.absolutePath)
        return analyzer.analyze(program)
    }

    private fun tok(
        text: String,
        type: CommonTypes = CommonTypes.IDENTIFIER,
        position: Position = Position(0, 0),
    ) = PrintScriptToken(type, text, position)

    private fun lit(
        value: Int,
        position: Position = Position(0, 0),
    ) = LiteralExpression(PrintScriptToken(CommonTypes.NUMBER_LITERAL, value.toString(), position))

    @Test
    fun `valid camelCase identifiers produce no diagnostics`() {
        val stmts =
            listOf(
                DeclarationStatement(tok("myVar"), tok("Int"), lit(123)),
                AssignmentStatement(tok("anotherVar"), lit(456)),
                PrintStatement(IdentifierExpression(tok("myVar"))),
            )
        assertTrue(runAnalyzer(stmts).isEmpty())
    }

    @Test
    fun `invalid camelCase identifier is flagged`() {
        val stmts =
            listOf(
                DeclarationStatement(tok("My_var"), tok("Int"), lit(0)),
            )
        val diags = runAnalyzer(stmts)
        assertEquals(1, diags.size)
        assertEquals(
            "Identifier 'My_var' does not match CAMEL_CASE",
            diags[0].message,
        )
    }

    @Test
    fun `println with complex expression is flagged when restricted`() {
        val cfg = AnalyzerConfig(restrictPrintlnArgs = true) // Explicitly set restriction
        val expr = BinaryExpression(lit(1), tok("+", CommonTypes.OPERATORS), lit(2))
        val stmts = listOf(PrintStatement(expr))
        val diags = runAnalyzer(stmts, cfg) // Pass the config explicitly
        assertEquals(1, diags.size)
        assertEquals(
            "println must take only a literal or identifier",
            diags[0].message,
        )
    }

    @Test
    fun `println with complex expression allowed when restriction off`() {
        val cfg = AnalyzerConfig(restrictPrintlnArgs = false)
        val expr = BinaryExpression(lit(1), tok("+", CommonTypes.OPERATORS), lit(2))
        val stmts = listOf(PrintStatement(expr))
        assertTrue(runAnalyzer(stmts, cfg).isEmpty())
    }

    @Test
    fun `snake_case style rejects camelCase names`() {
        val cfg = AnalyzerConfig(identifierStyle = IdentifierStyle.SNAKE_CASE)
        val stmts =
            listOf(
                DeclarationStatement(tok("myVar"), tok("Int"), lit(3)),
            )
        val diags = runAnalyzer(stmts, cfg)
        assertEquals(1, diags.size)
        assertEquals(
            "Identifier 'myVar' does not match SNAKE_CASE",
            diags[0].message,
        )
    }

    @Test
    fun `expression statement with literal expression produces no diagnostics`() {
        val stmts = listOf(ExpressionStatement(lit(42)))
        assertTrue(runAnalyzer(stmts).isEmpty())
    }

    @Test
    fun `multiple invalid identifiers produce multiple diagnostics`() {
        val decl = DeclarationStatement(tok("my_var"), tok("Int"), lit(1))
        val assign = AssignmentStatement(tok("AnotherVar"), lit(2))
        val stmts = listOf(decl, assign)
        val diags = runAnalyzer(stmts)
        assertEquals(2, diags.size)
        assertEquals(
            "Identifier 'my_var' does not match CAMEL_CASE",
            diags[0].message,
        )
        assertEquals(
            "Identifier 'AnotherVar' does not match CAMEL_CASE",
            diags[1].message,
        )
    }

    @Test
    fun `snake_case style accepts snake case identifiers`() {
        val cfg = AnalyzerConfig(identifierStyle = IdentifierStyle.SNAKE_CASE)
        val decl = DeclarationStatement(tok("my_var"), tok("Int"), lit(3))
        assertTrue(runAnalyzer(listOf(decl), cfg).isEmpty())
    }

    @Test
    fun `default analyzer constructor applies default settings`() {
        // By default restrictPrintlnArgs = true, so complex println should be flagged
        val expr = BinaryExpression(lit(1), tok("+", CommonTypes.OPERATORS), lit(2))
        val stmts = listOf(PrintStatement(expr))
        val diags = DefaultAnalyzer().analyze(Program(stmts)) // Use default constructor
        assertEquals(1, diags.size)
        assertEquals(
            "println must take only a literal or identifier",
            diags[0].message,
        )
    }

    @Test
    fun `yaml config loader honors settings`() {
        // Create a .yml config that disables println restriction and sets snake_case
        val yamlText =
            """
            identifierStyle: SNAKE_CASE
            restrictPrintlnArgs: false
            """.trimIndent()
        val tempConfig =
            File.createTempFile("analyzer", ".yml").apply {
                writeText(yamlText)
                deleteOnExit()
            }

        // my_var is valid snake_case and println with complex expr allowed
        val expr = BinaryExpression(lit(1), tok("+", CommonTypes.OPERATORS), lit(2))
        val stmts =
            listOf(
                DeclarationStatement(tok("my_var"), tok("Int"), lit(3)),
                PrintStatement(expr),
            )
        val diags = DefaultAnalyzer(tempConfig.absolutePath).analyze(Program(stmts))
        assertTrue(diags.isEmpty())
    }

    @Test
    fun `diagnostics include correct positions`() {
        val stmts =
            listOf(
                DeclarationStatement(
                    tok("My_var", CommonTypes.IDENTIFIER, Position(1, 5)),
                    tok("Int", CommonTypes.DATA_TYPES, Position(1, 10)),
                    lit(0, Position(1, 15)),
                ),
                AssignmentStatement(
                    tok("AnotherVar", CommonTypes.IDENTIFIER, Position(2, 3)),
                    lit(42, Position(2, 15)),
                ),
            )
        val diags = runAnalyzer(stmts)

        assertEquals(2, diags.size)

        assertEquals("Identifier 'My_var' does not match CAMEL_CASE", diags[0].message)
        assertEquals(Position(1, 5), diags[0].position)

        assertEquals("Identifier 'AnotherVar' does not match CAMEL_CASE", diags[1].message)
        assertEquals(Position(2, 3), diags[1].position)
    }
}
