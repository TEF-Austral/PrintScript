import builder.DefaultNodeBuilder
import builder.NodeBuilder
import coordinates.Coordinates
import coordinates.Position
import node.ASTNode
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import parser.ParserInterface
import stream.AstStreamResult
import java.util.NoSuchElementException
import node.EmptyExpression
import node.Program
import parser.Parser
import parser.result.CompleteProgram
import parser.result.ExpressionBuiltResult
import parser.result.ExpressionResult
import parser.result.FinalResult
import parser.result.NextResult
import parser.statement.StatementParser
import parser.statement.binary.DefaultParseBinary
import parser.statement.expression.ExpressionParsingBuilder
import parser.statement.expression.TokenToExpression
import parser.stream.ParserAstStream

class DummyNode : ASTNode {
    override fun getCoordinates(): Coordinates {
        return Position(0, 0)
    }
}

class DummyToken : TokenToExpression {

    override fun parse(parser: Parser, current: Token): ExpressionResult {
        return ExpressionBuiltResult(parser, EmptyExpression())
    }

}

class DummyParser(private val nodes: List<ASTNode>) : ParserInterface {
    private var index = 0
    override fun isAtEnd(): Boolean = index >= nodes.size

    override fun parse(): FinalResult {
        return CompleteProgram(this, Program(emptyList(),Position(0,0)))
    }

    override fun getExpressionParser(): ExpressionParsingBuilder {
        return ExpressionParsingBuilder(DummyToken(), DefaultParseBinary(DummyToken()))
    }

    override fun getNodeBuilder(): NodeBuilder {
        return DefaultNodeBuilder()
    }

    override fun getStatementParser(): StatementParser {
        return StatementParser(emptyList())
    }

    override fun hasNext(): Boolean {
        return !isAtEnd()
    }

    override fun next(): NextResult {
        return NextResult(DummyNode(), true, "", this)
    }


    override fun peak(): Token? {
        return if (isAtEnd()) {
            null
        } else {
            nodes[index] as Token
        }
    }
}

class ParserAstStreamNoMockTest {

    @Test
    fun peakReturnsNextNodeWhenNotAtEnd() {
        val nodes = listOf(DummyNode(), DummyNode())
        val stream = ParserAstStream(DummyParser(nodes))
        val result = stream.peak()
        assertTrue(result is DummyNode)
    }

    @Test
    fun peakThrowsExceptionWhenAtEnd() {
        val stream = ParserAstStream(DummyParser(emptyList()))
        assertThrows(NoSuchElementException::class.java) {
            stream.peak()
        }
    }

    @Test
    fun nextReturnsAstStreamResultWhenNotAtEnd() {
        val nodes = listOf(DummyNode())
        val stream = ParserAstStream(DummyParser(nodes))
        val result = stream.next()
        assertTrue(result.node is DummyNode)
    }

    @Test
    fun nextThrowsExceptionWhenAtEnd() {
        val stream = ParserAstStream(DummyParser(emptyList()))
        assertThrows(NoSuchElementException::class.java) {
            stream.next()
        }
    }

    @Test
    fun isAtEndReturnsTrueWhenParserIsAtEnd() {
        val stream = ParserAstStream(DummyParser(emptyList()))
        assertTrue(stream.isAtEnd())
    }

    @Test
    fun isAtEndReturnsFalseWhenParserIsNotAtEnd() {
        val stream = ParserAstStream(DummyParser(listOf(DummyNode())))
        assertFalse(stream.isAtEnd())
    }
}