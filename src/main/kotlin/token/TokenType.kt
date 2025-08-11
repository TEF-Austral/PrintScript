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
    NUMBER,
    BIGINT,
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
    NUMERIC_LITERAL,
    BIGINT_LITERAL,
    BOOLEAN_LITERAL,
    STRING_LITERAL,

    //Import
    IMPORT,


    // Comments
    SINGLE_LINE_COMMENT,
    MULTI_LINE_COMMENT,

    // Boolean literals
    TRUE,
    FALSE,


    // End of file
    EOF,

}