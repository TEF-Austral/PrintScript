sealed interface Parser {
    fun parse(tokens: List<Token>): AST
}