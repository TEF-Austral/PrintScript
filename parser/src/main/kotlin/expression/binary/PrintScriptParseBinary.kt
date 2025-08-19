package parser.expression.binary

import parser.expression.TokenToExpression
import parser.expression.primary.TokenToExpressionFactory

object PrintScriptParseBinary : ParseBinaryFactory {

    override fun create(): ParseBinary {
        return DefaultParseBinary(TokenToExpressionFactory.createDefaultRegistry())
    }

    override fun createCustom(tokenToExpression: TokenToExpression): ParseBinary {
        return DefaultParseBinary(tokenToExpression)
    }
}