package converter

import converter.specific.AssignmentToToken
import converter.specific.ComparisonToToken
import converter.specific.ConditionalToToken
import converter.specific.DataTypeToToken
import converter.specific.DeclarationToToken
import converter.specific.DelimiterToToken
import converter.specific.FunctionToToken
import converter.specific.LogicalOperatorToken
import converter.specific.LoopToToken
import converter.specific.NumberLiteralToToken
import converter.specific.OperatorToToken
import converter.specific.PrintToToken
import converter.specific.ReturnToToken
import converter.specific.StringLiteralToToken
import converter.specific.StringToTokenConverter

object StringToTokenConverterFactory {

    fun createDefaultsTokenConverter(): TokenConverter {
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