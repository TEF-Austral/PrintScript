import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PrintScriptASTTest {

    @Test
    fun testInOrderTraversal() {
        val leftToken = PrintScriptToken(TokenType.NUMBER_LITERAL, "1", Position(1, 1))
        val rightToken = PrintScriptToken(TokenType.NUMBER_LITERAL, "3", Position(1, 3))
        val rootToken = PrintScriptToken(TokenType.PLUS, "+", Position(1, 2))

        val leftNode = PrintScriptNode(leftToken, emptyList())
        val rightNode = PrintScriptNode(rightToken, emptyList())
        val rootNode = PrintScriptNode(rootToken, listOf(leftNode, rightNode))

        val traverser = InOrderTraverser()
        val ast = PrintScriptAST(traverser, rootNode)

        val nodes = ast.traverse()
        Assertions.assertEquals(3, nodes.size)
        Assertions.assertEquals("1", nodes[0].getValue().getValue())
        Assertions.assertEquals("+", nodes[1].getValue().getValue())
        Assertions.assertEquals("3", nodes[2].getValue().getValue())
    }

    @Test
    fun testInOrderTraversalLargerTree() {
        val n1 = PrintScriptNode(PrintScriptToken(TokenType.NUMBER_LITERAL, "1", Position(1, 1)), emptyList())
        val n2 = PrintScriptNode(PrintScriptToken(TokenType.NUMBER_LITERAL, "2", Position(1, 2)), emptyList())
        val n3 = PrintScriptNode(PrintScriptToken(TokenType.NUMBER_LITERAL, "3", Position(1, 3)), emptyList())
        val n4 = PrintScriptNode(PrintScriptToken(TokenType.NUMBER_LITERAL, "4", Position(1, 4)), emptyList())

        val plus = PrintScriptNode(PrintScriptToken(TokenType.PLUS, "+", Position(1, 5)), listOf(n1, n2))
        val minus = PrintScriptNode(PrintScriptToken(TokenType.MINUS, "-", Position(1, 6)), listOf(n3, n4))
        val multiply = PrintScriptNode(PrintScriptToken(TokenType.MULTIPLY, "*", Position(1, 7)), listOf(plus, minus))

        val traverser = InOrderTraverser()
        val ast = PrintScriptAST(traverser, multiply)

        val nodes = ast.traverse()
        Assertions.assertEquals(7, nodes.size)
        Assertions.assertEquals("1", nodes[0].getValue().getValue())
        Assertions.assertEquals("+", nodes[1].getValue().getValue())
        Assertions.assertEquals("2", nodes[2].getValue().getValue())
        Assertions.assertEquals("*", nodes[3].getValue().getValue())
        Assertions.assertEquals("3", nodes[4].getValue().getValue())
        Assertions.assertEquals("-", nodes[5].getValue().getValue())
        Assertions.assertEquals("4", nodes[6].getValue().getValue())
    }
}