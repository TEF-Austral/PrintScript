package parser.expression.binary

import parser.expression.TokenToExpression
import parser.expression.primary.TokenConverterFactory

object ParseBinaryFactory {

    fun create(): ParseBinary {
        return DefaultParseBinary(TokenConverterFactory.createDefaultRegistry())
    }

    fun createCustom(tokenToExpression: TokenToExpression): ParseBinary {
        return DefaultParseBinary(tokenToExpression)
    }
}