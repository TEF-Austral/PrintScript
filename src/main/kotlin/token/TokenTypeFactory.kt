package token

class TokenTypeFactory() {

    fun createDefaultMap(): Map<String, TokenType> {
        return mapOf(
            // Delimiters
            "(" to TokenType.OPEN_PARENTHESIS,
            ")" to TokenType.CLOSE_PARENTHESIS,
            "{" to TokenType.OPEN_BRACE,
            "}" to TokenType.CLOSE_BRACE,
            "," to TokenType.COMMA,
            "." to TokenType.DOT,
            ";" to TokenType.SEMICOLON,
            ":" to TokenType.COLON,
            "?" to TokenType.QUESTION_MARK,

            // Operators
            "+" to TokenType.PLUS,
            "-" to TokenType.MINUS,
            "*" to TokenType.MULTIPLY,
            "/" to TokenType.DIVIDE,
            "%" to TokenType.MODULE,
            "**" to TokenType.POWER,
            "++" to TokenType.INCREMENT,
            "--" to TokenType.DECREMENT,
            "->" to TokenType.ARROW_RIGHT,
            "<-" to TokenType.ARROW_LEFT,

            // Assignment & Comparison
            "=" to TokenType.ASSIGN,
            "==" to TokenType.EQUALS,
            "!=" to TokenType.NOT_EQUALS,
            ">" to TokenType.GREATER_THAN,
            ">=" to TokenType.GREATER_THAN_OR_EQUAL,
            "<" to TokenType.LESS_THAN,
            "<=" to TokenType.LESS_THAN_OR_EQUAL,

            // Logical Operators
            "&&" to TokenType.AND,
            "||" to TokenType.OR,
            "!" to TokenType.NOT,

            // Keywords
            "class" to TokenType.CLASS,
            "else" to TokenType.ELSE,
            "extends" to TokenType.EXTENDS,
            "function" to TokenType.FUNCTION,
            "for" to TokenType.FOR,
            "if" to TokenType.IF,
            "return" to TokenType.RETURN,
            "while" to TokenType.WHILE,
            "break" to TokenType.BREAK,
            "continue" to TokenType.CONTINUE,
            "switch" to TokenType.SWITCH,
            "case" to TokenType.CASE,
            "default" to TokenType.DEFAULT,
            "try" to TokenType.TRY,
            "catch" to TokenType.CATCH,
            "finally" to TokenType.FINALLY,
            "let" to TokenType.LET,
            "const" to TokenType.CONST,
            "import" to TokenType.IMPORT,
            "export" to TokenType.EXPORT,
            "print" to TokenType.PRINT,

            // Data type keywords
            "number" to TokenType.NUMBER,
            "bigint" to TokenType.BIGINT,
            "string" to TokenType.STRING,
            "boolean" to TokenType.BOOLEAN,

            // Literals
            "true" to TokenType.BOOLEAN_LITERAL,
            "false" to TokenType.BOOLEAN_LITERAL,
            "null" to TokenType.NULL_LITERAL,
        )
    }

    fun withNewTypes(customTypes: Map<String, TokenType>): Map<String, TokenType> {
        val defaultMap = createDefaultMap().toMutableMap()
        customTypes.forEach { (key, value) ->
            defaultMap[key] = value
        }
        return defaultMap
    }


}