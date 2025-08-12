package token

sealed interface TokenConverterFactory {

    fun createDefaultsTokenConverter(): TokenConverter

}

object StringToTokenConverterFactory : TokenConverterFactory {

    override fun createDefaultsTokenConverter(): TokenConverter {
        return TokenConverterRegistry(listOf(
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
        ))
    }

    fun createCustomTokenConverter(customConverters: List<StringToTokenConverter>): TokenConverter {
        return TokenConverterRegistry(customConverters)
    }
}