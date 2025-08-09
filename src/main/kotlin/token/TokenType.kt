package token

enum class TokenType {
    // Keywords
    IF,
    ELSE,
    WHILE,
    FOR,
    FUNCTION,
    RETURN,

    // Data types
    INT,
    FLOAT,
    STRING,
    BOOLEAN,

    // Asignment
    LET,
    CONST,

    // Operators
    PLUS,
    MINUS,
    MULTIPLY,
    DIVIDE,
    ASSIGN,

    // Delimiters
    SEMICOLON,
    COMMA,
    COLON,
    OPEN_PARENTHESIS,
    CLOSE_PARENTHESIS,
    OPEN_BRACE,
    CLOSE_BRACE,

    // Identifiers
    IDENTIFIER,

    // Literals
    INTEGER_LITERAL,
    FLOAT_LITERAL,
    STRING_LITERAL,

    // Comments
    SINGLE_LINE_COMMENT,
    MULTI_LINE_COMMENT,

    // Boolean literals
    TRUE,
    FALSE,


    // End of file
    EOF,

}