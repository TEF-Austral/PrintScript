package converter.factory

import converter.AndToToken
import converter.AssignToToken
import converter.CloseBraceToToken
import converter.CloseParenthesisToToken
import converter.ColonToToken
import converter.CommaToToken
import converter.DivideToToken
import converter.DotToToken
import converter.ElseToToken
import converter.EqualsToToken
import converter.ForToToken
import converter.FunctionToToken
import converter.GreaterThanOrEqualToToken
import converter.GreaterThanToToken
import converter.IfToToken
import converter.LessThanOrEqualToToken
import converter.LessThanToToken
import converter.LetToToken
import converter.MinusToToken
import converter.MultiplyToToken
import converter.NotEqualsToToken
import converter.NotToToken
import converter.NumberLiteralToToken
import converter.NumberToToken
import converter.OpenBraceToToken
import converter.OpenParenthesisToToken
import converter.OrToToken
import converter.PlusToToken
import converter.PrintlnToToken
import converter.QuestionMarkToToken
import converter.ReturnToToken
import converter.SemicolonToToken
import converter.StringLiteralToToken
import converter.StringToToken
import converter.StringToTokenConverter
import converter.TokenConverter
import converter.TokenConverterRegistry
import converter.WhileToToken

object StringToTokenConverterFactory : TokenConverterFactory {

    override fun createDefaultsTokenConverter(): TokenConverter {
        return TokenConverterRegistry(
            listOf(
                OpenParenthesisToToken,
                CloseParenthesisToToken,
                OpenBraceToToken,
                CloseBraceToToken,
                CommaToToken,
                DotToToken,
                SemicolonToToken,
                ColonToToken,
                QuestionMarkToToken,
                PlusToToken,
                MinusToToken,
                MultiplyToToken,
                DivideToToken,
                AssignToToken,
                EqualsToToken,
                NotEqualsToToken,
                GreaterThanToToken,
                GreaterThanOrEqualToToken,
                LessThanToToken,
                LessThanOrEqualToToken,
                AndToToken,
                OrToToken,
                NotToToken,
                ElseToToken,
                FunctionToToken,
                ForToToken,
                IfToToken,
                ReturnToToken,
                WhileToToken,
                LetToToken,
                PrintlnToToken,
                NumberToToken,
                StringToToken,
                NumberLiteralToToken,
                StringLiteralToToken
            )
        )
    }

    fun createCustomTokenConverter(customConverters: List<StringToTokenConverter>): TokenConverter {
        return TokenConverterRegistry(customConverters)
    }
}