package ast

import token.Token

sealed interface Node {
    fun getValue() : Token
    fun getChildren() : List<Node>
}

class PrintScriptNode(private val token: Token, private val children: List<Node>) : Node {
    override fun getValue() : Token {
        return token
    }
    override fun getChildren() : List<Node> {
        return children
    }
}