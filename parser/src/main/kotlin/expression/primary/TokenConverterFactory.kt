package parser.expression.primary

import parser.expression.TokenToExpression

object TokenConverterFactory {
    private val expressionBuilders: List<ExpressionBuilder> =
        listOf(
            IdentifierBuilder,
            LiteralBuilder,
            DelimitersBuilder,
        )

    fun createDefaultRegistry(): TokenToExpression = ExpressionRegistry(expressionBuilders)

    fun createCustomRegistry(expressionBuilders: List<ExpressionBuilder>): TokenToExpression = ExpressionRegistry(expressionBuilders)
}
