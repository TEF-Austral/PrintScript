sealed interface Lexer {
    fun tokenize(): List<Token>
}