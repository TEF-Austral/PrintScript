package ast

sealed interface Traverser {
    fun traverse(ast: AST): List<Node>
}

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