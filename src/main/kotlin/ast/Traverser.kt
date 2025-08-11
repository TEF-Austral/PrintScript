package ast

sealed interface Traverser {
    fun traverse(ast: AST): List<Node>
}

class InOrderTraverser() : Traverser {
    override fun traverse(ast: AST): List<Node> {
        TODO("Not yet implemented")
    }
}