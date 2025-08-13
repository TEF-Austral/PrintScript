package node

import Token

sealed interface Node {
    fun getValue() : Token
    fun getChildren() : List<Node>
}