package token

class TokenTypeFactory() {

    fun createDefaultMap(): Map<String, TokenType> {
        return mapOf(
            "let" to TokenType.LET,
            "if" to TokenType.IF,
            "else" to TokenType.ELSE,
            "while" to TokenType.WHILE,
            "for" to TokenType.FOR,
            "return" to TokenType.RETURN,
            "function" to TokenType.FUNCTION,
            // "class" to TokenType.CL,
            // "import" to TokenType.IMPORT,
            // "export" to TokenType.EXPORT,
            "true" to TokenType.TRUE,
            "false" to TokenType.FALSE,
            "const" to TokenType.CONST,
            "+" to TokenType.PLUS,
            "-" to TokenType.MINUS,
            "*" to TokenType.MULTIPLY,
            "/" to TokenType.DIVIDE,
            "=" to TokenType.ASSIGN,
            ";" to TokenType.SEMICOLON,
            "," to TokenType.COMMA,
            ":" to TokenType.COLON,
            "(" to TokenType.OPEN_PARENTHESIS,
            ")" to TokenType.CLOSE_PARENTHESIS,
            "{" to TokenType.OPEN_BRACE,
            "}" to TokenType.CLOSE_BRACE,
        )
    }

    fun createCustomMap(customTokens: Map<String, TokenType>): Map<String, TokenType> {
        val defaultMap = createDefaultMap()
        return defaultMap + customTokens
    }
}