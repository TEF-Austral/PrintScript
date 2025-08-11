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
    MODULE,
    POWER,
    INCREMENT,
    DECREMENT,
    ARROW_RIGHT,
    ARROW_LEFT,

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
    EXTENDS,
    FUNCTION,
    FOR,
    IF,
    RETURN,
    WHILE,
    BREAK,
    CONTINUE,
    SWITCH,
    CASE,
    DEFAULT,
    TRY,
    CATCH,
    FINALLY,
    LET,
    CONST,
    IMPORT,
    EXPORT,
    PRINT,

    // Data type keywords
    NUMBER,
    BIGINT,
    STRING,
    BOOLEAN,

    // Literals
    IDENTIFIER,
    NUMBER_LITERAL,
    BIGINT_LITERAL,
    STRING_LITERAL,
    BOOLEAN_LITERAL,
    NULL_LITERAL,

    // Comments
    SINGLE_LINE_COMMENT,
    MULTI_LINE_COMMENT,

    // End of file
    EOF
}