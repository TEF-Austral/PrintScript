import builder.DefaultNodeBuilder
import config.AnalyzerConfig
import config.AnalyzerConfigLoader.loadAnalyzerConfig
import coordinates.Position
import diagnostic.Diagnostic
import factory.AnalyzerFactory.createAnalyzer
import node.BinaryExpression
import node.LiteralExpression
import node.PrintStatement
import node.Program
import node.ReadInputExpression
import node.Statement
import node.ExpressionStatement
import node.IdentifierExpression
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import parser.factory.VOnePointOneParserFactory
import parser.result.CompleteProgram
import type.CommonTypes
import java.io.File
import kotlin.test.Test
import type.Version

class AnalyzerOnePointOneTests {

    private fun tokenListToStream(list: List<Token>): TokenStream = MockTokenStream(list)

    private val parser =
        VOnePointOneParserFactory().createParser(
            tokenListToStream(emptyList()),
            DefaultNodeBuilder(),
        )

    private fun runAnalyzer(
        stmts: List<Statement>,
        config: AnalyzerConfig = AnalyzerConfig(),
        version: Version = Version.VERSION_1_1,
    ): List<Diagnostic> {
        val tempConfigFile =
            File.createTempFile("analyzer", ".json").apply {
                writeText(
                    """
                    {
                      "identifierStyle":"${config.identifierStyle}",
                      "restrictPrintlnArgs":${config.restrictPrintlnArgs},
                      "restrictReadInputArgs":${config.restrictReadInputArgs}
                    }
                    """.trimIndent(),
                )
                deleteOnExit()
            }
        return createAnalyzer(version, loadAnalyzerConfig(tempConfigFile.absolutePath))
            .analyze(CompleteProgram(parser, Program(stmts)))
    }

    private fun tok(
        text: String,
        type: CommonTypes = CommonTypes.IDENTIFIER,
        position: Position = Position(0, 0),
    ) = PrintScriptToken(type, text, position)

    private fun lit(
        value: Int,
        position: Position = Position(0, 0),
    ) = LiteralExpression(tok(value.toString(), CommonTypes.NUMBER_LITERAL, position), position)

    @Test
    fun `default 1_1_0 enforces println and readInput rules`() {
        val stmts =
            listOf(
                PrintStatement(
                    BinaryExpression(
                        lit(1),
                        tok("+", CommonTypes.OPERATORS),
                        lit(2),
                        Position(0, 0),
                    ),
                    Position(0, 0),
                ),
                ExpressionStatement(
                    ReadInputExpression(
                        BinaryExpression(
                            lit(1),
                            tok("+", CommonTypes.OPERATORS),
                            lit(2),
                            Position(1, 0),
                        ),
                        Position(1, 0),
                    ),
                    Position(1, 0),
                ),
            )
        val cfg = AnalyzerConfig(restrictPrintlnArgs = true, restrictReadInputArgs = true)
        val diagnostics = runAnalyzer(stmts, cfg)
        assertEquals(2, diagnostics.size)
        assertEquals("println must take only a literal or identifier", diagnostics[0].message)
        assertEquals("readInput must take only a literal or identifier", diagnostics[1].message)
    }

    @Test
    fun `1_1_x respects println override but enforces readInput`() {
        val cfg = AnalyzerConfig(restrictPrintlnArgs = false, restrictReadInputArgs = true)
        val stmts =
            listOf(
                PrintStatement(
                    BinaryExpression(
                        lit(1),
                        tok("+", CommonTypes.OPERATORS),
                        lit(2),
                        Position(0, 0),
                    ),
                    Position(0, 0),
                ),
                ExpressionStatement(
                    ReadInputExpression(
                        BinaryExpression(
                            lit(1),
                            tok("+", CommonTypes.OPERATORS),
                            lit(2),
                            Position(1, 0),
                        ),
                        Position(1, 0),
                    ),
                    Position(1, 0),
                ),
            )
        val diagnostics = runAnalyzer(stmts, cfg)
        assertEquals(1, diagnostics.size)
        assertEquals("readInput must take only a literal or identifier", diagnostics[0].message)
    }

    @Test
    fun `1_1_x allows valid println and readInput args`() {
        val stmts =
            listOf(
                PrintStatement(lit(42), Position(0, 0)),
                ExpressionStatement(
                    ReadInputExpression(
                        IdentifierExpression(
                            PrintScriptToken(CommonTypes.IDENTIFIER, "myVar", Position(1, 0)),
                            Position(1, 0),
                        ),
                        Position(1, 0),
                    ),
                    Position(1, 0),
                ),
            )
        assertTrue(runAnalyzer(stmts).isEmpty())
    }

    @Test
    fun `version 1_1_0 enforces same rules as 1_0_0`() {
        val stmts =
            listOf(
                PrintStatement(
                    BinaryExpression(
                        lit(1),
                        tok("+", CommonTypes.OPERATORS),
                        lit(2),
                        Position(0, 0),
                    ),
                    Position(0, 0),
                ),
                ExpressionStatement(
                    ReadInputExpression(
                        BinaryExpression(
                            lit(1),
                            tok("+", CommonTypes.OPERATORS),
                            lit(2),
                            Position(1, 0),
                        ),
                        Position(1, 0),
                    ),
                    Position(1, 0),
                ),
            )
        val cfg = AnalyzerConfig(restrictPrintlnArgs = true, restrictReadInputArgs = true)
        val diagnostics = runAnalyzer(stmts, cfg, version = Version.VERSION_1_1)
        assertEquals(2, diagnostics.size)
        assertEquals("println must take only a literal or identifier", diagnostics[0].message)
        assertEquals("readInput must take only a literal or identifier", diagnostics[1].message)
    }
}
