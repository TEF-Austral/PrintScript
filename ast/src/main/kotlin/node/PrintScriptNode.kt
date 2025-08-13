package node

import Token

class PrintScriptNode(private val token: Token, private val children: List<Node>) : Node {
    override fun getValue() : Token {
        return token
    }
    override fun getChildren() : List<Node> {
        return children
    }
}