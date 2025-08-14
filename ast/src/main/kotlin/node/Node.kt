package node

import Token
import TokenType

sealed interface Node {

    fun getType(): NodeTypes

}


// TODO, PROBLEMA DE NODO O TOKEN EN BINARY OPERATION Y PRINT NODE

// + * / -

//

data class BinaryOperationNode(val operator: Token, val num1: Binaryoperation, val num2: Token) : Node {


}

data class DeclarationNode(val dataType: TokenType, val identifier: Token, val value: AssignmentNode?) : Node {

    override fun getType(): NodeTypes {
        return NodeTypes.DECLARATION

    }

}

data class AssignmentNode(val identifier: Token, val value: Token) : Node {
    override fun getType(): NodeTypes {
        return NodeTypes.ASSIGNMENT
    }

}

data class PrintNode(val value: Token) : Node {
    override fun getType(): NodeTypes {
        return NodeTypes.PRINT
    }

}


