package parser.expression.binary

import parser.expression.TokenToExpression
import parser.expression.primary.TokenConverterFactory

object ParseBinaryFactory {
    fun create(): ParseBinary = DefaultParseBinary(TokenConverterFactory.createDefaultRegistry())

    fun createCustom(tokenToExpression: TokenToExpression): ParseBinary = DefaultParseBinary(tokenToExpression)
}
