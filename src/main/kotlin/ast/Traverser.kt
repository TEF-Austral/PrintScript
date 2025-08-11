package ast

sealed interface Traverser {
    fun traverse(ast: AST): List<Node>
}

class InOrderTraverser() : Traverser {
    override fun traverse(ast: AST): List<Node> {
        return traverse(ast.getRoot())
    }

    private fun traverse(node : Node?): List<Node> {
        if (node == null) {
            return emptyList()
        }
        val allLeftNodes = traverse(node.left())
        val allRightNodes = traverse(node.right())
        return allLeftNodes + node + allRightNodes
    }
}