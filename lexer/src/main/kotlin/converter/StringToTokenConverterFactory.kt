package converter

import converter.specific.AssignmentToToken
import converter.specific.ComparisonToToken
import converter.specific.ConditionalToToken
import converter.specific.ConstDeclarationToToken
import converter.specific.DataTypeToToken
import converter.specific.LetDeclarationToToken
import converter.specific.DelimiterToToken
import converter.specific.FunctionToToken
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

object StringToTokenConverterFactory {
    fun createDefaultsTokenConverter(): TokenConverter =
        TokenConverterRegistry(
            listOf(
                FunctionToToken,
                DataTypeToToken,
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
                ReadEnvToToken
            ),
        )

    fun createCustomTokenConverter(customConverters: List<StringToTokenConverter>): TokenConverter = TokenConverterRegistry(customConverters)
}
