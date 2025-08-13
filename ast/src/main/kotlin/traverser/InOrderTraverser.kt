package traverser

import AST
import node.Node
import kotlin.collections.plus

class InOrderTraverser : Traverser {
    override fun traverse(ast: AST): List<Node> {
        return traverse(ast.getRoot())
    }

    private fun traverse(node: Node): List<Node> {
        val children = node.getChildren()
        val left = if (children.isNotEmpty()) traverse(children[0]) else emptyList()
        val right = if (children.size > 1) traverse(children[1]) else emptyList()
        return left + node + right
    }
}