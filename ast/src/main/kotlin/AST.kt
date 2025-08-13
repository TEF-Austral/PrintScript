import node.Node

sealed interface AST {
    fun getRoot(): Node
    fun traverse(): List<Node>
}