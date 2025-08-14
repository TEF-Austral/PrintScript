import node.Node

sealed interface AST {
    fun getRoot(): List<Node>
    fun traverse(): List<Node>
}