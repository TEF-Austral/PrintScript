import flags.CliFlags
import formatter.FormatterService
import analyzer.Analyzer
import parser.Parser
import parser.factory.RecursiveParserFactory

class CLI(
    private val linter: Analyzer,
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
            CliFlags.FORMATTING -> handleFormatting(formatterConfigFilePath, srcCodePath, formatter, parser, lexer)
            CliFlags.ANALYZING -> handleAnalyzing(analyzerConfigFilePath, srcCodePath, linter)
            CliFlags.VALIDATION -> handleValidation(analyzerConfigFilePath, linter, formatterConfigFilePath, formatter)
            CliFlags.EXECUTION -> handleExecution(srcCodePath, interpreter, parser, lexer)
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
        analyzerConfigFilePath: String?,
        srcCodePath: String,
        linter: Analyzer,
    ): String = "TODO implement this"

    private fun handleValidation(
        analyzerConfigFilePath: String?,
        linter: Analyzer,
        formatterConfigFilePath: String?,
        formatter: FormatterService,
    ): String {
        val formatingResult = handleFormatting(formatterConfigFilePath, "src/test/resources/test_code.psl", formatter, parser, lexer)
        val linterResult = handleAnalyzing(analyzerConfigFilePath, "src/test/resources/test_code.psl", linter)
        return formatingResult + linterResult
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
