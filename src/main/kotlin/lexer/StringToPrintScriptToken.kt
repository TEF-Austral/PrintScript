package lexer

import token.Coordinates
import token.Token
import token.TokenType

class StringToPrintScriptToken(typeMap: Map<String, TokenType>) : TokenConverter {
  private val registry = TokenConverterRegistry(
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
      ClassToToken,
      ElseToToken,
      FunctionToToken,
      ForToToken,
      IfToToken,
      PrintlnToToken,
      ReturnToToken,
      WhileToToken,
      LetToToken,
      NumberToToken,
      StringToToken,
      NumberLiteralToToken,
      StringLiteralToToken
    )
  )

  override fun convert(input: String, position: Coordinates): Token {
    return registry.convert(input, position)
  }
}
