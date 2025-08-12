import ast.PrintScriptAST
import ast.PrintScriptNode
import ast.InOrderTraverser
import token.PrintScriptToken
import token.TokenType
import token.Position
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class PrintScriptASTTest {

  @Test
  fun `in-order traversal returns nodes in correct order`() {
    // create tokens
    val leftToken  = PrintScriptToken(TokenType.IDENTIFIER, "a", Position(1, 1))
    val rootToken  = PrintScriptToken(TokenType.IDENTIFIER, "b", Position(1, 2))
    val rightToken = PrintScriptToken(TokenType.IDENTIFIER, "c", Position(1, 3))

    // build nodes
    val leftNode  = PrintScriptNode(leftToken,  null,     null)
    val rightNode = PrintScriptNode(rightToken, null,     null)
    val rootNode  = PrintScriptNode(rootToken,  leftNode, rightNode)

    // create AST and traverse
    val ast = PrintScriptAST(InOrderTraverser(), rootNode)
    val values = ast.traverse().map { it.getValue().getValue() }

    assertEquals(listOf("a", "b", "c"), values)
  }
}
