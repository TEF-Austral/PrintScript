import node.Node
import traverser.Traverser

class PrintScriptAST(private val traverser: Traverser, private val root: List<Node>) : AST {

    override fun traverse(): List<Node> {
        return traverser.traverse(this)
    }

    override fun getRoot(): List<Node> {
        return root
    }
}