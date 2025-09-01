package org.example

class CLI(
    private val src: String,
)
/*
        private val splitter = SplitterFactory.createSplitter()
        private val tokenConverter = StringToTokenConverterFactory.createDefaultsTokenConverter()
        val mutableMap: MutableMap<String, Any> = mutableMapOf()

        val listForBinaryExpressionExecutor: List<SpecificExpressionExecutor> =
            listOf(
                EmptyExpressionExecutor(),
                IdentifierExpressionExecutor(mutableMap),
                LiteralExpressionExecutor(),
            )

        val specificExpressionExecutors: List<SpecificExpressionExecutor> =
            listOf(
                BinaryExpressionExecutor(expressions = listForBinaryExpressionExecutor),
                EmptyExpressionExecutor(),
                IdentifierExpressionExecutor(mutableMap),
                LiteralExpressionExecutor(),
            )

        val specificStatementExecutor: List<SpecificStatementExecutor> =
            listOf(
                DeclarationStatementExecutor(mutableMap, DefaultExpressionExecutor(specificExpressionExecutors)),
                AssignmentStatementExecutor(mutableMap, DefaultExpressionExecutor(specificExpressionExecutors)),
                ExpressionStatementExecutor(DefaultExpressionExecutor(specificExpressionExecutors)),
                PrintStatementExecutor(DefaultExpressionExecutor(specificExpressionExecutors)),
            )

        fun validation() {
            TODO("Implement this function to complete the task")
        }

        fun execution(): InterpreterResult {
            val parseResult: ParseResult.Success<Program> = RecursiveParserFactory().createParser(
                Lexer(splitter, tokenConverter).tokenize(Stream(src)),
                DefaultNodeBuilder()).parse().first as ParseResult.Success
            return Interpreter(
                DefaultExpressionExecutor(specificExpressionExecutors),
                DefaultStatementExecutor(specificStatementExecutor),).interpret(parseResult.value)
        }

        fun formatting() {
            TODO("Implement this function to complete the task")
        }

 */
