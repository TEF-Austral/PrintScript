package parser.expression.primary

import parser.expression.ExpressionBuilder
import parser.expression.TokenToExpression

object TokenToExpressionFactory: TokenConverterFactory {

    private val expressionBuilders: List<ExpressionBuilder> = listOf(
        IdentifierBuilder,
        LiteralBuilder,
        DelimitersBuilder,
    )

    override fun createDefaultRegistry(): TokenToExpression {
        return ExpressionRegistry(expressionBuilders)
    }

     override fun createCustomRegistry(expressionBuilders: List<ExpressionBuilder>): TokenToExpression {
        return ExpressionRegistry(expressionBuilders)
    }
}