package ast

import token.Token

sealed interface Node {
    fun getValue() : Token
    fun left() : Node?
    fun right() : Node?
}

class PrintScriptNode(private val token: Token, private val leftNode: Node?, private val rightNode: Node?) : Node {
    override fun getValue() : Token {
        return token
    }
    override fun left() : Node? {
        return leftNode
    }
    override fun right() : Node? {
        return rightNode
    }
}