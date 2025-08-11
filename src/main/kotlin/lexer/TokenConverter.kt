package lexer

import java.math.BigInteger
import token.Coordinates
import token.PrintScriptToken
import token.Token
import token.TokenType

sealed interface TokenConverter {
    fun convert(input: String,position: Coordinates): Token
}

data class TokenConverterRegistry(val list :List<StringToTokenConverter>): TokenConverter {

    override fun convert(input: String, position: Coordinates): Token {
        for(converter in list){
            if(converter.canHandle(input)){
                return converter.convert(input, position)
            }
        }
        return PrintScriptToken(type = TokenType.IDENTIFIER, value = input, coordinates = position)
    }
}

sealed interface StringToTokenConverter : TokenConverter {

    fun canHandle(input: String): Boolean
}

object OpenParenthesisToToken : StringToTokenConverter {

    override fun canHandle(input: String): Boolean {
        return input == "("
    }

    override fun convert(input: String, position: Coordinates): Token {
       return PrintScriptToken(type = TokenType.OPEN_PARENTHESIS, value = input, coordinates = position)
    }

}

object CloseParenthesisToToken : StringToTokenConverter {

    override fun canHandle(input: String): Boolean {
        return input == ")"
    }

    override fun convert(input: String, position: Coordinates): Token {
       return PrintScriptToken(type = TokenType.CLOSE_PARENTHESIS, value = input, coordinates = position)
    }

}

object OpenBraceToToken : StringToTokenConverter {

    override fun canHandle(input: String): Boolean {
        return input == "{"
    }

    override fun convert(input: String, position: Coordinates): Token {
       return PrintScriptToken(type = TokenType.OPEN_BRACE, value = input, coordinates = position)
    }

}

object CloseBraceToToken : StringToTokenConverter {

    override fun canHandle(input: String): Boolean {
        return input == "}"
    }

    override fun convert(input: String, position: Coordinates): Token {
       return PrintScriptToken(type = TokenType.CLOSE_BRACE, value = input, coordinates = position)
    }

}

object CommaToToken : StringToTokenConverter {

    override fun canHandle(input: String): Boolean {
        return input == ","
    }

    override fun convert(input: String, position: Coordinates): Token {
       return PrintScriptToken(type = TokenType.COMMA, value = input, coordinates = position)
    }

}

object DotToToken : StringToTokenConverter {

    override fun canHandle(input: String): Boolean {
        return input == "."
    }

    override fun convert(input: String, position: Coordinates): Token {
       return PrintScriptToken(type = TokenType.DOT, value = input, coordinates = position)
    }

}

object SemicolonToToken : StringToTokenConverter {

    override fun canHandle(input: String): Boolean {
        return input == ";"
    }

    override fun convert(input: String, position: Coordinates): Token {
       return PrintScriptToken(type = TokenType.SEMICOLON, value = input, coordinates = position)
    }

}

object ColonToToken : StringToTokenConverter {

    override fun canHandle(input: String): Boolean {
        return input == ":"
    }

    override fun convert(input: String, position: Coordinates): Token {
       return PrintScriptToken(type = TokenType.COLON, value = input, coordinates = position)
    }

}

object QuestionMarkToToken : StringToTokenConverter {

    override fun canHandle(input: String): Boolean {
        return input == "?"
    }

    override fun convert(input: String, position: Coordinates): Token {
       return PrintScriptToken(type = TokenType.QUESTION_MARK, value = input, coordinates = position)
    }

}

object PlusToToken : StringToTokenConverter {

    override fun canHandle(input: String): Boolean {
        return input == "+"
    }

    override fun convert(input: String, position: Coordinates): Token {
       return PrintScriptToken(type = TokenType.PLUS, value = input, coordinates = position)
    }

}

object MinusToToken : StringToTokenConverter {

    override fun canHandle(input: String): Boolean {
        return input == "-"
    }

    override fun convert(input: String, position: Coordinates): Token {
       return PrintScriptToken(type = TokenType.MINUS, value = input, coordinates = position)
    }

}

object MultiplyToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean {
        return input == "*"
    }

    override fun convert(input: String, position: Coordinates): Token {
       return PrintScriptToken(type = TokenType.MULTIPLY, value = input, coordinates = position)
    }
}

object DivideToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean {
        return input == "/"
    }

    override fun convert(input: String, position: Coordinates): Token {
       return PrintScriptToken(type = TokenType.DIVIDE, value = input, coordinates = position)
    }
}

object AssignToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean {
        return input == "="
    }

    override fun convert(input: String, position: Coordinates): Token {
       return PrintScriptToken(type = TokenType.ASSIGN, value = input, coordinates = position)
    }
}

object EqualsToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean {
        return input == "=="
    }

    override fun convert(input: String, position: Coordinates): Token {
       return PrintScriptToken(type = TokenType.EQUALS, value = input, coordinates = position)
    }
}

object NotEqualsToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean {
        return input == "!="
    }

    override fun convert(input: String, position: Coordinates): Token {
       return PrintScriptToken(type = TokenType.NOT_EQUALS, value = input, coordinates = position)
    }
}

object GreaterThanToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean {
        return input == ">"
    }

    override fun convert(input: String, position: Coordinates): Token {
       return PrintScriptToken(type = TokenType.GREATER_THAN, value = input, coordinates = position)
    }
}

object GreaterThanOrEqualToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean {
        return input == ">="
    }

    override fun convert(input: String, position: Coordinates): Token {
       return PrintScriptToken(type = TokenType.GREATER_THAN_OR_EQUAL, value = input, coordinates = position)
    }
}

object LessThanToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean {
        return input == "<"
    }

    override fun convert(input: String, position: Coordinates): Token {
       return PrintScriptToken(type = TokenType.LESS_THAN, value = input, coordinates = position)
    }
}

object LessThanOrEqualToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean {
        return input == "<="
    }

    override fun convert(input: String, position: Coordinates): Token {
       return PrintScriptToken(type = TokenType.LESS_THAN_OR_EQUAL, value = input, coordinates = position)
    }
}

object AndToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean {
        return input == "&&"
    }

    override fun convert(input: String, position: Coordinates): Token {
       return PrintScriptToken(type = TokenType.AND, value = input, coordinates = position)
    }
}

object OrToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean {
        return input == "||"
    }

    override fun convert(input: String, position: Coordinates): Token {
       return PrintScriptToken(type = TokenType.OR, value = input, coordinates = position)
    }
}

object NotToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean {
        return input == "!"
    }

    override fun convert(input: String, position: Coordinates): Token {
       return PrintScriptToken(type = TokenType.NOT, value = input, coordinates = position)
    }
}

object ClassToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean {
        return input == "class"
    }

    override fun convert(input: String, position: Coordinates): Token {
       return PrintScriptToken(type = TokenType.CLASS, value = input, coordinates = position)
    }
}

object ElseToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean {
        return input == "else"
    }

    override fun convert(input: String, position: Coordinates): Token {
       return PrintScriptToken(type = TokenType.ELSE, value = input, coordinates = position)
    }
}

object FunctionToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean {
        return input == "function"
    }

    override fun convert(input: String, position: Coordinates): Token {
       return PrintScriptToken(type = TokenType.FUNCTION, value = input, coordinates = position)
    }
}

object ForToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean {
        return input == "for"
    }

    override fun convert(input: String, position: Coordinates): Token {
       return PrintScriptToken(type = TokenType.FOR, value = input, coordinates = position)
    }
}

object IfToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean {
        return input == "if"
    }

    override fun convert(input: String, position: Coordinates): Token {
       return PrintScriptToken(type = TokenType.IF, value = input, coordinates = position)
    }
}

object PrintlnToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean {
        return input == "println"
    }

    override fun convert(input: String, position: Coordinates): Token {
       return PrintScriptToken(type = TokenType.PRINT, value = input, coordinates = position)
    }
}

object ReturnToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean {
        return input == "return"
    }

    override fun convert(input: String, position: Coordinates): Token {
       return PrintScriptToken(type = TokenType.RETURN, value = input, coordinates = position)
    }
}

object WhileToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean {
        return input == "while"
    }

    override fun convert(input: String, position: Coordinates): Token {
       return PrintScriptToken(type = TokenType.WHILE, value = input, coordinates = position)
    }
}

object LetToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean {
        return input == "let"
    }

    override fun convert(input: String, position: Coordinates): Token {
       return PrintScriptToken(type = TokenType.LET, value = input, coordinates = position)
    }
}

// Hay que hacer la logica para que sooporte los literals

object NumberToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean {
        return input == "number"
    }

    override fun convert(input: String, position: Coordinates): Token {
       return PrintScriptToken(type = TokenType.NUMBER, value = input, coordinates = position)
    }
}

object StringToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean {
        return input == "string"
    }

    override fun convert(input: String, position: Coordinates): Token {
       return PrintScriptToken(type = TokenType.STRING, value = input, coordinates = position)
    }
}


object NumberLiteralToToken : StringToTokenConverter {

    override fun canHandle(input: String): Boolean {
        return input.toDoubleOrNull() != null || input.toIntOrNull() != null
    }

    override fun convert(input: String, position: Coordinates): Token {
        return PrintScriptToken(type = TokenType.NUMBER_LITERAL, value = input, coordinates = position)
    }

}

object StringLiteralToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean {
        return input.startsWith("'") && input.endsWith("'") || input.startsWith("\"") && input.endsWith("\"")
    }

    override fun convert(input: String, position: Coordinates): Token {
        return PrintScriptToken(type = TokenType.STRING_LITERAL, value = input, coordinates = position)
    }
}