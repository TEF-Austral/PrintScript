package converter.factory

import converter.AssignmentToToken
import converter.ComparisonToToken
import converter.ConditionalToToken
import converter.DataTypeToToken
import converter.DeclarationToToken
import converter.DelimiterToToken
import converter.FunctionToToken
import converter.LogicalOperatorToken
import converter.LoopToToken
import converter.NumberLiteralToToken
import converter.OperatorToToken
import converter.PrintToToken
import converter.ReturnToToken
import converter.StringLiteralToToken
import converter.StringToTokenConverter
import converter.TokenConverter
import converter.TokenConverterRegistry

object StringToTokenConverterFactory : TokenConverterFactory {

    override fun createDefaultsTokenConverter(): TokenConverter {
        return TokenConverterRegistry(
            listOf(
                // Keywords
                FunctionToToken,
                DataTypeToToken,
                NumberLiteralToToken,
                StringLiteralToToken,
                ReturnToToken,
                ConditionalToToken,
                LoopToToken,
                DeclarationToToken,
                PrintToToken,

                // Operators and Delimiters
                OperatorToToken,
                AssignmentToToken,
                ComparisonToToken,
                LogicalOperatorToken,
                DelimiterToToken,
            )
        )
    }


    fun createCustomTokenConverter(customConverters: List<StringToTokenConverter>): TokenConverter {
        return TokenConverterRegistry(customConverters)
    }
}