package parser.expression.primary

import type.CommonTypes
import parser.expression.TokenToExpression

interface ExpressionBuilder : TokenToExpression {
    fun canHandle(token: CommonTypes): Boolean
}
