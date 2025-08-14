package traverser

import AST
import kotlin.collections.plus
import kotlin.compareTo
import node.Node

class InOrderTraverser : Traverser {

    override fun traverse(ast: AST): List<Node> {
        for (node in ast.getRoot()) {
            return traverse(node)
        }
        return emptyList()
    }

    private fun traverse(node: Node): List<Node> {
        val children = node.getChildren()
        val left = if (children.isNotEmpty()) traverse(children[0]) else emptyList()
        val right = if (children.size > 1) traverse(children[1]) else emptyList()
        return left + node + right
    }
}


