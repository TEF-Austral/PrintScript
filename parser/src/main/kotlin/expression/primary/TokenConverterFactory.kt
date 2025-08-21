package parser.expression.primary

import parser.expression.TokenToExpression

object TokenConverterFactory {

    private val expressionBuilders: List<ExpressionBuilder> = listOf(
        IdentifierBuilder,
        LiteralBuilder,
        DelimitersBuilder,
    )

    fun createDefaultRegistry(): TokenToExpression {
        return ExpressionRegistry(expressionBuilders)
    }

    fun createCustomRegistry(expressionBuilders: List<ExpressionBuilder>): TokenToExpression {
        return ExpressionRegistry(expressionBuilders)
    }
}