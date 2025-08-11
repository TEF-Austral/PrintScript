package ast

sealed interface AST {
    fun addNode(node: Node): AST
    fun traverse(): List<Node>
}

class PrintScriptAST(private val traverser: Traverser): AST {
    override fun traverse(): List<Node> {
        return traverser.traverse(this)
    }

    override fun addNode(node: Node): AST {
        TODO("Not yet implemented")
    }
}