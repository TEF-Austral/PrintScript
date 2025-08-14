class PrintScriptParserFactory : ParserFactory {
    override fun createMainParser(): Parser {
        return PrintScriptParser(
            listOf(
                BinaryOperationParser,
                DeclarationParser,
                AssignmentParser,
                PrintParser
            )
        )
    }
}