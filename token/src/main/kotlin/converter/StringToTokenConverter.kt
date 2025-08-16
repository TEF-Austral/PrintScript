package converter

import Coordinates
import PrintScriptToken
import Token
import TokenType

sealed interface StringToTokenConverter : TokenConverter {
    fun canHandle(input: String): Boolean
}

object DelimiterToToken : StringToTokenConverter {
    override fun canHandle(input: String): Boolean {
        return input in listOf("(", ")", "{", "}", ",", ".", ";", ":", "?")
    }

    override fun convert(input: String, position: Coordinates): Token {
        return PrintScriptToken(type = TokenType.DELIMITERS, value = input, coordinates = position)
    }
}

object OperatorToToken : StringToTokenConverter {

    override fun canHandle(input: String): Boolean {
        return input in listOf("+", "-", "*", "/", "%")
    }

    override fun convert(input: String, position: Coordinates): Token {
        return PrintScriptToken(type = TokenType.OPERATORS, value = input, coordinates = position)
    }
}

object AssignmentToToken : StringToTokenConverter {

    override fun canHandle(input: String): Boolean {
        return input == "="
    }

    override fun convert(input: String, position: Coordinates): Token {
        return PrintScriptToken(type = TokenType.ASSIGNMENT, value = input, coordinates = position)
    }
}

object ComparisonToToken : StringToTokenConverter {

    override fun canHandle(input: String): Boolean {
        return input in listOf("==", "!=", "<", ">", "<=", ">=")
    }

    override fun convert(input: String, position: Coordinates): Token {
        return PrintScriptToken(type = TokenType.COMPARISON, value = input, coordinates = position)
    }
}

object LogicalOperatorToken : StringToTokenConverter {

    override fun canHandle(input: String): Boolean {
        return input in listOf("&&", "||")
    }

    override fun convert(input: String, position: Coordinates): Token {
        return PrintScriptToken(type = TokenType.LOGICAL_OPERATORS, value = input, coordinates = position)
    }
}

object ConditionalToToken : StringToTokenConverter {

    override fun canHandle(input: String): Boolean {
        return input in listOf("if", "else", "switch", "case")
    }

    override fun convert(input: String, position: Coordinates): Token {
        return PrintScriptToken(type = TokenType.CONDITIONALS, value = input, coordinates = position)
    }
}

object LoopToToken : StringToTokenConverter {

    override fun canHandle(input: String): Boolean {
        return input in listOf("for", "while", "do")
    }

    override fun convert(input: String, position: Coordinates): Token {
        return PrintScriptToken(type = TokenType.LOOPS, value = input, coordinates = position)
    }
}


object DataTypeToToken : StringToTokenConverter {

    override fun canHandle(input: String): Boolean {
        return input in listOf("number", "string")
    }

    override fun convert(input: String, position: Coordinates): Token {
        return PrintScriptToken(type = TokenType.DATA_TYPES, value = input, coordinates = position)
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

object FunctionToToken : StringToTokenConverter {

    override fun canHandle(input: String): Boolean {
        return input == "function"
    }

    override fun convert(input: String, position: Coordinates): Token {
        return PrintScriptToken(type = TokenType.FUNCTION, value = input, coordinates = position)
    }
}

object DeclarationToToken : StringToTokenConverter {

    override fun canHandle(input: String): Boolean {
        return input in listOf("let", "const", "var")
    }

    override fun convert(input: String, position: Coordinates): Token {
        return PrintScriptToken(type = TokenType.DECLARATION, value = input, coordinates = position)
    }
}

object PrintToToken : StringToTokenConverter {

    override fun canHandle(input: String): Boolean {
        return input == "println"
    }

    override fun convert(input: String, position: Coordinates): Token {
        return PrintScriptToken(type = TokenType.PRINT, value = input, coordinates = position)
    }
}

object StringLiteralToToken : StringToTokenConverter {

    override fun canHandle(input: String): Boolean {
        return input.startsWith("\"") && input.endsWith("\"") ||
                input.startsWith("'") && input.endsWith("'")
    }

    override fun convert(input: String, position: Coordinates): Token {
        return PrintScriptToken(type = TokenType.STRING_LITERAL, value = input, coordinates = position)
    }
}

object NumberLiteralToToken : StringToTokenConverter {

    override fun canHandle(input: String): Boolean {
        return input.toDoubleOrNull() != null
    }

    override fun convert(input: String, position: Coordinates): Token {
        return PrintScriptToken(type = TokenType.NUMBER_LITERAL, value = input, coordinates = position)
    }
}
