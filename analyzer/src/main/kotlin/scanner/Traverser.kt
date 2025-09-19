package scanner

import node.Expression
import node.Statement
import node.AssignmentStatement
import node.BinaryExpression
import node.DeclarationStatement
import node.EmptyExpression
import node.ExpressionStatement
import node.IdentifierExpression
import node.IfStatement
import node.LiteralExpression
import node.PrintStatement
import node.ReadEnvExpression
import node.ReadInputExpression

fun Expression.forEachExpression(action: (Expression) -> Unit) {
    action(this)
    when (this) {
        is BinaryExpression -> {
            getLeft().forEachExpression(action)
            getRight().forEachExpression(action)
        }

        is IdentifierExpression,
        is LiteralExpression,
        is ReadInputExpression,
        is ReadEnvExpression,
        is EmptyExpression,
        -> {}
        else -> {}
    }
}

fun Statement.forEachExpression(action: (Expression) -> Unit) {
    when (this) {
        is DeclarationStatement -> getInitialValue()?.forEachExpression(action)
        is AssignmentStatement -> getValue().forEachExpression(action)
        is PrintStatement -> getExpression().forEachExpression(action)
        is ExpressionStatement -> getExpression().forEachExpression(action)
        is IfStatement -> {
            getCondition().forEachExpression(action)
            getConsequence().forEachExpression(action)
            getAlternative()?.forEachExpression(action)
        }
        else -> {}
    }
}
