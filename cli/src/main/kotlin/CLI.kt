import builder.DefaultNodeBuilder
import converter.StringToTokenConverterFactory
import factory.DefaultInterpreterFactory
import flags.CliFlags
import formatter.FormatterService
import parser.factory.VOnePointZeroParserFactory
import splitter.SplitterFactory
import node.Program

class CLI {
    fun execute(
        flag: CliFlags,
        srcCodePath: String,
        analyzerConfigFilePath: String? = null,
        formatterConfigFilePath: String? = null,
    ): String =
        when (flag) {
            CliFlags.FORMATTING ->
                handleFormatting(srcCodePath, formatterConfigFilePath)

            CliFlags.ANALYZING ->
                handleAnalyzing(srcCodePath, analyzerConfigFilePath)

            CliFlags.VALIDATION ->
                handleValidation(srcCodePath, analyzerConfigFilePath, formatterConfigFilePath)

            CliFlags.EXECUTION ->
                handleExecution(srcCodePath)
        }

    private fun parseSourceCode(srcCodePath: String): Program {
        val lexer =
            DefaultLexer(SplitterFactory.createSplitter(), StringToTokenConverterFactory.createDefaultsTokenConverter())
        val tokenList = lexer.tokenize(getDefaultReader(srcCodePath))
        val nodeBuilder = DefaultNodeBuilder()
        val parser = VOnePointZeroParserFactory().createParser(tokenList, nodeBuilder)
        return parser.parse().getProgram()
    }

    private fun handleFormatting(
        srcCodePath: String,
        formatterConfigFilePath: String?,
    ): String {
        val program = parseSourceCode(srcCodePath)
        val formatter = FormatterService()
        return formatter.formatToString(program, formatterConfigFilePath ?: "formatter_config.json")
    }

    private fun handleAnalyzing(
        srcCodePath: String,
        analyzerConfigFilePath: String?,
    ): String {
        val program = parseSourceCode(srcCodePath)
        val analyzer = DefaultAnalyzer(analyzerConfigFilePath)
        val diagnostics = analyzer.analyze(program)
        return if (diagnostics.isEmpty()) {
            "No issues found"
        } else {
            diagnostics.joinToString("\n") { it.message } // "${it.position.toString()} -
        }
    }

    private fun handleValidation(
        srcCodePath: String,
        analyzerConfigFilePath: String?,
        formatterConfigFilePath: String?,
    ): String {
        val analysisResult = handleAnalyzing(srcCodePath, analyzerConfigFilePath)
        val formattingResult = handleFormatting(srcCodePath, formatterConfigFilePath)

        return """
            --- ANALYSIS REPORT---
            $analysisResult

            ---FORMATTING PREVIEW---
            $formattingResult
            """.trimIndent()
    }

    private fun handleExecution(srcCodePath: String): String {
        val program = parseSourceCode(srcCodePath)
        val interpreter = DefaultInterpreterFactory.createDefaultInterpreter()
        val result = interpreter.interpret(program)

        return if (result.interpretedCorrectly) {
            "Program executed successfully"
        } else {
            "Program executed with errors: ${result.message}"
        }
    }

    private fun getDefaultReader(path: String): Reader = FileReader(path)
}
