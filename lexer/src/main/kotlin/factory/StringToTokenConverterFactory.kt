package factory

import converter.TokenConverter
import converter.TokenConverterRegistry
import converter.specific.AssignmentToToken
import converter.specific.ComparisonToToken
import converter.specific.ConditionalToToken
import converter.specific.ConstDeclarationToToken
import converter.specific.DataTypeToToken
import converter.specific.DelimiterToToken
import converter.specific.FunctionToToken
import converter.specific.LetDeclarationToToken
import converter.specific.LogicalOperatorToken
import converter.specific.LoopToToken
import converter.specific.NumberLiteralToToken
import converter.specific.OperatorToToken
import converter.specific.PrintToToken
import converter.specific.ReadEnvToToken
import converter.specific.ReadInputToToken
import converter.specific.ReturnToToken
import converter.specific.StringLiteralToToken
import converter.specific.StringToTokenConverter
import type.CommonTypes

object StringToTokenConverterFactory : ConverterFactory{
    fun createDefaultsTokenConverter(): TokenConverter =
        TokenConverterRegistry(
            listOf(
                FunctionToToken,
                DataTypeToToken(),
                NumberLiteralToToken,
                StringLiteralToToken,
                ReturnToToken,
                ConditionalToToken,
                LoopToToken,
                LetDeclarationToToken,
                PrintToToken,
                OperatorToToken,
                AssignmentToToken,
                ComparisonToToken,
                LogicalOperatorToken,
                DelimiterToToken,
                ConstDeclarationToToken,
                ReadInputToToken,
                ReadEnvToToken,
            ),
        )

    fun createCustomTokenConverter(customConverters: List<StringToTokenConverter>): TokenConverter =
        TokenConverterRegistry(customConverters)

    override fun createVersionOne(): TokenConverter =
        TokenConverterRegistry(
            listOf(
                DataTypeToToken(),
                NumberLiteralToToken,
                StringLiteralToToken,
                LetDeclarationToToken,
                PrintToToken,
                OperatorToToken,
                AssignmentToToken,
                DelimiterToToken,
            ),
        )

    override fun createVersionOnePointOne(): TokenConverter =
        TokenConverterRegistry(
            listOf(
                DataTypeToToken(
                    mapOf(
                        "Number" to CommonTypes.NUMBER,
                        "number" to CommonTypes.NUMBER,
                        "String" to CommonTypes.STRING,
                        "string" to CommonTypes.STRING,
                        "Boolean" to CommonTypes.BOOLEAN,
                        "boolean" to CommonTypes.BOOLEAN,
                    ),
                ),
                NumberLiteralToToken,
                StringLiteralToToken,
                ReturnToToken,
                ConditionalToToken,
                LetDeclarationToToken,
                PrintToToken,
                OperatorToToken,
                AssignmentToToken,
                ComparisonToToken,
                LogicalOperatorToken,
                DelimiterToToken,
                ConstDeclarationToToken,
                ReadInputToToken,
                ReadEnvToToken,
            ),
        )

    override fun createCustom(customConverters: List<StringToTokenConverter>): TokenConverter =
        TokenConverterRegistry(customConverters)
}
