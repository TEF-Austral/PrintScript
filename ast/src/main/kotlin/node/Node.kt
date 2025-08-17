package node

import Token


sealed interface ASTNode {

}

sealed interface Statement : ASTNode // Las declaraciones no retornan valores, solo ejecutan acciones

sealed interface Expression : ASTNode // Las expresiones retornan valores, pueden ser usadas en declaraciones


class LiteralExpression(private val token: Token) : Expression {

    fun getValue(): String = token.getValue()

}

class IdentifierExpression(private val token: Token) : Expression {

    fun getName(): String = token.getValue()

}

class BinaryExpression(
    private val left: Expression,
    private val operator: Token,
    private val right: Expression
) : Expression {

    fun getLeft(): Expression = left
    fun getOperator(): Token = operator
    fun getRight(): Expression = right

}


class VariableDeclarationStatement(
    private val identifier: Token,
    private val dataType: Token,
    private val initialValue: Expression? = null
) : Statement {

    fun getIdentifier(): String = identifier.getValue()
    fun getDataType(): String = dataType.getValue()
    fun getInitialValue(): Expression? = initialValue

}

class AssignmentStatement(
    private val identifier: Token,
    private val value: Expression
) : Statement {

    fun getIdentifier(): String = identifier.getValue()
    fun getValue(): Expression = value

}

class PrintStatement(private val expression: Expression) : Statement {

    fun getExpression(): Expression = expression
}

class ExpressionStatement(private val expression: Expression) : Statement {

    fun getExpression(): Expression = expression

}

class Program(private val statements: List<Statement>) : ASTNode {   //Root del AST, pero usando Composite (creo)

    fun getStatements(): List<Statement> = statements

}

class EmptyExpression : Expression {
    override fun toString(): String = "EmptyExpression"
}

class EmptyStatement : Statement {
    override fun toString(): String = "EmptyStatement"
}