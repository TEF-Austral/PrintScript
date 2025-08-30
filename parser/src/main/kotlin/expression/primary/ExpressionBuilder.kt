package parser.expression.primary

import parser.expression.TokenToExpression
import type.CommonTypes

interface ExpressionBuilder : TokenToExpression {
    fun canHandle(token: CommonTypes): Boolean
}
