package parser.expression.binary

import parser.expression.TokenToExpression

interface ParseBinaryFactory {
    fun create(): ParseBinary

    fun createCustom(tokenToExpression: TokenToExpression): ParseBinary
}