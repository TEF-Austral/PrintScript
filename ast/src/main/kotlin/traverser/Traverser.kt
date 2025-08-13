package traverser

import AST
import node.Node

sealed interface Traverser {
    fun traverse(ast: AST): List<Node>
}