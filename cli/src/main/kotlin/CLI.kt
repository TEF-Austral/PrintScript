import flags.CliFlags
import formatter.Formatter
import org.example.Analyzer
import parser.Parser

class CLI(
    private val linter: Analyzer,
    private val formatter: Formatter,
    private val interpreter: Interpreter,
    private val parser: Parser,
    private val lexer: Lexer
){
    fun execute(flags: CliFlags,configFileRoutes: String,){

    }

}
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
