class PrintScriptParser(private val parsers: List<Parser>) : Parser {

    override fun parse(tokens: List<Token>): AST {
        for (parser in parsers) {
            parser.parse(tokens) // esto se agrega al nodo root ponele
        }
    }
}

object BinaryOperationParser : Parser {
    override fun parse(tokens: List<Token>): AST {
        TODO("Not yet implemented")
    }
}

object DeclarationParser : Parser {
    override fun parse(tokens: List<Token>): AST {
        TODO("Not yet implemented")
    }
}

object AssignmentParser : Parser {
    override fun parse(tokens: List<Token>): AST {
        TODO("Not yet implemented")
    }
}

object PrintParser : Parser {
    override fun parse(tokens: List<Token>): AST {
        TODO("Not yet implemented")
    }
}