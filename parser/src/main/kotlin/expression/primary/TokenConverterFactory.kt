package parser.expression.primary

import parser.expression.ExpressionBuilder
import parser.expression.TokenToExpression

sealed interface TokenConverterFactory {

    fun createDefaultRegistry(): TokenToExpression

    fun createCustomRegistry(expressionBuilders: List<ExpressionBuilder>): TokenToExpression
}