package token

enum class TokenType {

    // Delimiters
    OPEN_PARENTHESIS,
    CLOSE_PARENTHESIS,
    OPEN_BRACE,
    CLOSE_BRACE,
    COMMA,
    DOT,
    SEMICOLON,
    COLON,
    QUESTION_MARK,

    // Operators
    PLUS,
    MINUS,
    MULTIPLY,
    DIVIDE,

    // Assignment & Comparison
    ASSIGN,
    EQUALS,
    NOT_EQUALS,
    GREATER_THAN,
    GREATER_THAN_OR_EQUAL,
    LESS_THAN,
    LESS_THAN_OR_EQUAL,

    // Logical Operators
    AND,
    OR,
    NOT,

    // Keywords
    CLASS,
    ELSE,
    FUNCTION,
    FOR,
    IF,
    RETURN,
    WHILE,
    LET,
    PRINT,

    // Data type keywords
    NUMBER,
    STRING,
    BOOLEAN,

    // Literals
    IDENTIFIER,
    NUMBER_LITERAL,
    STRING_LITERAL,

}