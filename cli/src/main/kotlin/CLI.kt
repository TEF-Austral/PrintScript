import flags.CliFlags
import formatter.FormatterService
import analyzer.Analyzer
import parser.Parser
import parser.factory.RecursiveParserFactory

class CLI(
    private val formatter: FormatterService,
    private val interpreter: Interpreter,
    private val parser: Parser,
    private val lexer: Lexer,
) {
    fun execute(
        flag: CliFlags,
        srcCodePath: String,
        analyzerConfigFilePath: String? = null,
        formatterConfigFilePath: String? = null,
    ): String =
        when (flag) {
            CliFlags.FORMATTING ->
                handleFormatting(formatterConfigFilePath, srcCodePath, formatter, parser, lexer)

            CliFlags.ANALYZING ->
                handleAnalyzing(srcCodePath, analyzerConfigFilePath)

            CliFlags.VALIDATION ->
                handleValidation(srcCodePath, analyzerConfigFilePath, formatterConfigFilePath)

            CliFlags.EXECUTION ->
                handleExecution(srcCodePath, interpreter, parser, lexer)
        }

    private fun handleFormatting(
        formatterConfigFilePath: String?,
        srcCodePath: String,
        formatter: FormatterService,
        parser: Parser,
        lexer: Lexer,
    ): String {
        val tokens = lexer.tokenize(getDefaultReader(srcCodePath))
        val program = RecursiveParserFactory().withNewTokens(tokens, parser).parse().getProgram()
        return formatter.formatToString(program, formatterConfigFilePath ?: "formatter_config.json")
    }

    private fun handleAnalyzing(
        srcCodePath: String,
        analyzerConfigFilePath: String?,
    ): String {
        val analyzer = Analyzer(analyzerConfigFilePath)
        val tokens = lexer.tokenize(getDefaultReader(srcCodePath))
        val program =
            RecursiveParserFactory()
                .withNewTokens(tokens, parser)
                .parse()
                .getProgram()

        val diagnostics = analyzer.analyze(program)
        return if (diagnostics.isEmpty()) {
            "No issues found"
        } else {
            diagnostics.joinToString("\n") { "${it.position}: ${it.message}" }
        }
    }

    private fun handleValidation(
        srcCodePath: String,
        analyzerConfigFilePath: String?,
        formatterConfigFilePath: String?,
    ): String {
        val formatted =
            handleFormatting(
                formatterConfigFilePath,
                srcCodePath,
                formatter,
                parser,
                lexer,
            )
        val analyzed =
            handleAnalyzing(
                srcCodePath,
                analyzerConfigFilePath,
            )
        return formatted + analyzed
    }

    private fun handleExecution(
        srcCodePath: String,
        interpreter: Interpreter,
        parser: Parser,
        lexer: Lexer,
    ): String {
        val tokens = lexer.tokenize(getDefaultReader(srcCodePath))
        val program = RecursiveParserFactory().withNewTokens(tokens, parser).parse().getProgram()
        val result = interpreter.interpret(program)
        if (result.interpretedCorrectly){
            return "Program executed successfully"
        }
        return "Program executed with errors: ${result.message}"
    }

    private fun getDefaultReader(path: String): Reader = FileReader(path)
}
