package node

import node.statement.Statement

class Program(private val statements: List<Statement>) : ASTNode {   //Root del AST, pero usando Composite (creo)

    fun getStatements(): List<Statement> = statements

}