interface Lexer {
    fun tokenize(reader: Reader): List<Token>
}
