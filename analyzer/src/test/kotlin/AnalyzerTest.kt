import builder.DefaultNodeBuilder
import config.AnalyzerConfig
import config.AnalyzerConfigLoader.loadAnalyzerConfig
import node.Statement
import coordinates.Position
import diagnostic.Diagnostic
import factory.AnalyzerFactory.createAnalyzer
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
import parser.factory.VOnePointOneParserFactory
import parser.result.CompleteProgram
import checkers.IdentifierStyle
import java.io.File
import type.Version

class AnalyzerTest {

    private fun tokenListToStream(list: List<Token>): TokenStream = MockTokenStream(list)

    private val parser =
        VOnePointOneParserFactory().createParser(
            tokenListToStream(emptyList()),
            DefaultNodeBuilder(),
        )

    private fun runAnalyzer(
        stmts: List<Statement>,
        config: AnalyzerConfig = AnalyzerConfig(IdentifierStyle.CAMEL_CASE),
        version: Version = Version.VERSION_1_0,
    ): List<Diagnostic> {
        val program = Program(stmts)
        val analyzer = createAnalyzer(version, config)
        return analyzer.analyze(CompleteProgram(parser, program))
    }

    private fun tok(
        text: String,
        type: CommonTypes = CommonTypes.IDENTIFIER,
        position: Position = Position(0, 0),
    ) = PrintScriptToken(type, text, position)

    private fun lit(
        value: Int,
        position: Position = Position(0, 0),
    ) = LiteralExpression(
        PrintScriptToken(CommonTypes.NUMBER_LITERAL, value.toString(), position),
        Position(0, 0),
    )

    @Test
    fun `valid camelCase identifiers produce no diagnostics`() {
        val stmts =
            listOf(
                DeclarationStatement(
                    tok("let", CommonTypes.LET),
                    tok("myVar"),
                    tok("Int"),
                    lit(123),
                    Position(0, 0),
                ),
                AssignmentStatement(tok("anotherVar"), lit(456), Position(0, 0)),
                PrintStatement(IdentifierExpression(tok("myVar"), Position(0, 0)), Position(0, 0)),
            )
        assertTrue(runAnalyzer(stmts).isEmpty())
    }

    @Test
    fun `invalid camelCase identifier is flagged`() {
        val stmts =
            listOf(
                DeclarationStatement(
                    tok("let", CommonTypes.LET),
                    tok("My_var"),
                    tok("Int"),
                    lit(0),
                    Position(0, 0),
                ),
            )
        val diagnostics = runAnalyzer(stmts)
        assertEquals(1, diagnostics.size)
        assertEquals(
            "Identifier 'My_var' does not match CAMEL_CASE",
            diagnostics[0].message,
        )
    }

    @Test
    fun `println with complex expression is flagged when restricted`() {
        val cfg = AnalyzerConfig(restrictPrintlnArgs = true)
        val expr = BinaryExpression(lit(1), tok("+", CommonTypes.OPERATORS), lit(2), Position(0, 0))
        val stmts = listOf(PrintStatement(expr, Position(0, 0)))
        val diagnostics = runAnalyzer(stmts, cfg)
        assertEquals(1, diagnostics.size)
        assertEquals(
            "println must take only a literal or identifier",
            diagnostics[0].message,
        )
    }

    @Test
    fun `version 1_0_x allows complex println when restriction off`() {
        val cfg = AnalyzerConfig(restrictPrintlnArgs = false)
        val expr =
            BinaryExpression(
                lit(1),
                tok("+", CommonTypes.OPERATORS),
                lit(2),
                Position(0, 0),
            )
        val stmts = listOf(PrintStatement(expr, Position(0, 0)))

        val diagnostics = runAnalyzer(stmts, cfg)
        assertTrue(diagnostics.isEmpty())
    }

    @Test
    fun `snake_case style rejects camelCase names`() {
        val cfg = AnalyzerConfig(identifierStyle = IdentifierStyle.SNAKE_CASE)
        val stmts =
            listOf(
                DeclarationStatement(
                    tok("let", CommonTypes.LET),
                    tok("myVar"),
                    tok("Int"),
                    lit(3),
                    Position(0, 0),
                ),
            )
        val diagnostics = runAnalyzer(stmts, cfg)
        assertEquals(1, diagnostics.size)
        assertEquals(
            "Identifier 'myVar' does not match SNAKE_CASE",
            diagnostics[0].message,
        )
    }

    @Test
    fun `expression statement with literal expression produces no diagnostics`() {
        val stmts = listOf(ExpressionStatement(lit(42), Position(0, 0)))
        assertTrue(runAnalyzer(stmts).isEmpty())
    }

    @Test
    fun `multiple invalid identifiers produce multiple diagnostics`() {
        val decl =
            DeclarationStatement(
                tok("let", CommonTypes.LET),
                tok("my_var"),
                tok("Int"),
                lit(1),
                Position(0, 0),
            )
        val assign = AssignmentStatement(tok("AnotherVar"), lit(2), Position(0, 0))
        val stmts = listOf(decl, assign)
        val diagnostics = runAnalyzer(stmts)
        assertEquals(2, diagnostics.size)
        assertEquals(
            "Identifier 'my_var' does not match CAMEL_CASE",
            diagnostics[0].message,
        )
        assertEquals(
            "Identifier 'AnotherVar' does not match CAMEL_CASE",
            diagnostics[1].message,
        )
    }

    @Test
    fun `snake_case style accepts snake case identifiers`() {
        val cfg = AnalyzerConfig(identifierStyle = IdentifierStyle.SNAKE_CASE)
        val decl =
            DeclarationStatement(
                tok("let", CommonTypes.LET),
                tok("my_var"),
                tok("Int"),
                lit(3),
                Position(0, 0),
            )
        assertTrue(runAnalyzer(listOf(decl), cfg).isEmpty())
    }

    @Test
    fun `default analyzer applies 1_0_0 settings via factory`() {
        val expr =
            BinaryExpression(
                lit(1),
                tok("+", CommonTypes.OPERATORS),
                lit(2),
                Position(0, 0),
            )
        val stmts = listOf(PrintStatement(expr, Position(0, 0)))
        val analyzer = createAnalyzer(Version.VERSION_1_0)
        val diagnostics = analyzer.analyze(CompleteProgram(parser, Program(stmts)))

        assertEquals(1, diagnostics.size)
        assertEquals(
            "println must take only a literal or identifier",
            diagnostics[0].message,
        )
    }

    @Test
    fun `yaml config loader honors settings`() {
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

        val expr = BinaryExpression(lit(1), tok("+", CommonTypes.OPERATORS), lit(2), Position(0, 0))
        val stmts =
            listOf(
                DeclarationStatement(
                    tok("let", CommonTypes.LET),
                    tok("my_var"),
                    tok("Int"),
                    lit(3),
                    Position(0, 0),
                ),
                PrintStatement(expr, Position(0, 0)),
            )

        val analyzer =
            createAnalyzer(Version.VERSION_1_0, loadAnalyzerConfig(tempConfig.absolutePath))
        val diagnostics = analyzer.analyze(CompleteProgram(parser, Program(stmts)))
        assertTrue(diagnostics.isEmpty())
    }

    @Test
    fun `diagnostics include correct positions`() {
        val stmts =
            listOf(
                DeclarationStatement(
                    tok("let", CommonTypes.LET),
                    tok("My_var", CommonTypes.IDENTIFIER, Position(1, 5)),
                    tok("Int", CommonTypes.DATA_TYPES, Position(1, 10)),
                    lit(0, Position(1, 15)),
                    Position(0, 0),
                ),
                AssignmentStatement(
                    tok("AnotherVar", CommonTypes.IDENTIFIER, Position(2, 3)),
                    lit(42, Position(2, 15)),
                    Position(0, 0),
                ),
            )
        val diagnostics = runAnalyzer(stmts)

        assertEquals(2, diagnostics.size)

        assertEquals("Identifier 'My_var' does not match CAMEL_CASE", diagnostics[0].message)
        assertEquals(Position(1, 5), diagnostics[0].position)

        assertEquals("Identifier 'AnotherVar' does not match CAMEL_CASE", diagnostics[1].message)
        assertEquals(Position(2, 3), diagnostics[1].position)
    }
}
