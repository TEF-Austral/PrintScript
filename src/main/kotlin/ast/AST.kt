package ast

sealed interface AST {
    fun getRoot(): Node
    fun traverse(): List<Node>
}

class PrintScriptAST(private val traverser: Traverser, private val root: Node): AST {
    override fun traverse(): List<Node> {
        return traverser.traverse(this)
    }
    override fun getRoot(): Node {return root}
}