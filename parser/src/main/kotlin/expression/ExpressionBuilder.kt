package parser.expression

import type.CommonTypes

sealed interface ExpressionBuilder : TokenToExpression {
    fun canHandle(token: CommonTypes): Boolean
}
