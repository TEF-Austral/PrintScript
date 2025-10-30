import builder.DefaultNodeBuilder
import coordinates.Position
import node.IfStatement
import node.PrintStatement
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import parser.factory.DefaultParserFactory
import parser.factory.VOnePointOneParserFactory
import parser.factory.VOnePointZeroParserFactory
import type.CommonTypes
import type.Version

class ParserFactoryTest {

    private val nodeBuilder = DefaultNodeBuilder()

    @Test
    fun `V1_0 factory parses simple declaration`() {
        val tokens =
            listOf(
                PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                PrintScriptToken(CommonTypes.IDENTIFIER, "x", Position(1, 5)),
                PrintScriptToken(CommonTypes.DELIMITERS, ":", Position(1, 7)),
                PrintScriptToken(CommonTypes.NUMBER, "NUMBER", Position(1, 9)),
                PrintScriptToken(CommonTypes.ASSIGNMENT, "=", Position(1, 16)),
                PrintScriptToken(CommonTypes.NUMBER_LITERAL, "5", Position(1, 18)),
                PrintScriptToken(CommonTypes.DELIMITERS, ";", Position(1, 19)),
            )
        val parser = VOnePointZeroParserFactory().createParser(MockTokenStream(tokens), nodeBuilder)
        val program = parser.parse().getProgram()
        assertEquals(1, program.getStatements().size)
    }

    @Test
    fun `Default factory with VERSION_1_0 uses V1_0 parser`() {
        val tokens =
            listOf(
                PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                PrintScriptToken(CommonTypes.IDENTIFIER, "a", Position(1, 5)),
                PrintScriptToken(CommonTypes.DELIMITERS, ":", Position(1, 6)),
                PrintScriptToken(CommonTypes.NUMBER, "NUMBER", Position(1, 8)),
                PrintScriptToken(CommonTypes.ASSIGNMENT, "=", Position(1, 15)),
                PrintScriptToken(CommonTypes.NUMBER_LITERAL, "1", Position(1, 17)),
                PrintScriptToken(CommonTypes.DELIMITERS, ";", Position(1, 18)),
            )
        val parser =
            DefaultParserFactory.createWithVersion(
                Version.VERSION_1_0,
                nodeBuilder,
                MockTokenStream(tokens),
            )
        val result = parser.parse()
        assertTrue(result.isSuccess())
        assertEquals(1, result.getProgram().getStatements().size)
    }

    @Test
    fun `Default factory with VERSION_1_1 supports if and read functions`() {
        val tokens =
            listOf(
                // if (true) { print("ok"); }
                PrintScriptToken(CommonTypes.CONDITIONALS, "if", Position(1, 1)),
                PrintScriptToken(CommonTypes.DELIMITERS, "(", Position(1, 3)),
                PrintScriptToken(CommonTypes.BOOLEAN_LITERAL, "true", Position(1, 4)),
                PrintScriptToken(CommonTypes.DELIMITERS, ")", Position(1, 8)),
                PrintScriptToken(CommonTypes.DELIMITERS, "{", Position(1, 9)),
                PrintScriptToken(CommonTypes.PRINT, "print", Position(1, 10)),
                PrintScriptToken(CommonTypes.DELIMITERS, "(", Position(1, 15)),
                PrintScriptToken(CommonTypes.STRING_LITERAL, "\"ok\"", Position(1, 16)),
                PrintScriptToken(CommonTypes.DELIMITERS, ")", Position(1, 20)),
                PrintScriptToken(CommonTypes.DELIMITERS, ";", Position(1, 21)),
                PrintScriptToken(CommonTypes.DELIMITERS, "}", Position(1, 22)),
            )
        val parser =
            DefaultParserFactory.createWithVersion(
                Version.VERSION_1_1,
                nodeBuilder,
                MockTokenStream(tokens),
            )
        val program = parser.parse().getProgram()
        assertEquals(1, program.getStatements().size)
        assertTrue(program.getStatements()[0] is IfStatement)
    }

    @Test
    fun `createDefault should be equivalent to VERSION_1_1`() {
        val tokens =
            listOf(
                PrintScriptToken(CommonTypes.PRINT, "print", Position(1, 1)),
                PrintScriptToken(CommonTypes.DELIMITERS, "(", Position(1, 6)),
                PrintScriptToken(CommonTypes.STRING_LITERAL, "\"Hello\"", Position(1, 7)),
                PrintScriptToken(CommonTypes.DELIMITERS, ")", Position(1, 14)),
                PrintScriptToken(CommonTypes.DELIMITERS, ";", Position(1, 15)),
            )
        val parserDefault = DefaultParserFactory.createDefault(nodeBuilder, MockTokenStream(tokens))
        val parserV11 =
            DefaultParserFactory.createWithVersion(
                Version.VERSION_1_1,
                nodeBuilder,
                MockTokenStream(tokens),
            )
        val resultDefault = parserDefault.parse()
        val resultV11 = parserV11.parse()
        assertTrue(resultDefault.isSuccess() && resultV11.isSuccess())
        assertEquals(
            resultV11.getProgram().getStatements().size,
            resultDefault.getProgram().getStatements().size,
        )
        assertTrue(resultDefault.getProgram().getStatements()[0] is PrintStatement)
    }

    @Test
    fun `createCustomParser wires provided components`() {
        // We obtain components from a fresh V1.1 parser to reuse tested builders
        val base =
            VOnePointOneParserFactory().createParser(
                MockTokenStream(emptyList()),
                nodeBuilder,
            )
        val expr = base.getExpressionParser()
        val stmt = base.getStatementParser()
        val tokens =
            listOf(
                PrintScriptToken(CommonTypes.PRINT, "print", Position(1, 1)),
                PrintScriptToken(CommonTypes.DELIMITERS, "(", Position(1, 6)),
                PrintScriptToken(CommonTypes.STRING_LITERAL, "\"X\"", Position(1, 7)),
                PrintScriptToken(CommonTypes.DELIMITERS, ")", Position(1, 10)),
                PrintScriptToken(CommonTypes.DELIMITERS, ";", Position(1, 11)),
            )
        val custom =
            DefaultParserFactory.createCustomParser(
                nodeBuilder,
                MockTokenStream(tokens),
                expr,
                stmt,
            )

        // Ensure the parser uses the same instances we provided
        assertSame(expr, custom.getExpressionParser())
        assertSame(stmt, custom.getStatementParser())

        // And that it can parse with them
        val result = custom.parse()
        assertTrue(result.isSuccess())
        assertTrue(result.getProgram().getStatements()[0] is PrintStatement)
    }
}
