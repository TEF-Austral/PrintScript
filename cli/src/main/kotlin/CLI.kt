import builder.DefaultNodeBuilder
import converter.StringToTokenConverterFactory
import factory.AnalyzerFactory
import factory.DefaultInterpreterFactory
import flags.CliFlags
import formatter.FormatterService
import parser.factory.V1Point0ParserFactory
import splitter.SplitterFactory
import node.Program

class CLI {
    fun execute(
        flag: CliFlags,
        srcCodePath: String,
        analyzerConfigFilePath: String? = null,
        formatterConfigFilePath: String? = null,
        version: String = "1.0"
    ): String =
        when (flag) {
            CliFlags.FORMATTING ->
                handleFormatting(srcCodePath, formatterConfigFilePath, version)

            CliFlags.ANALYZING ->
                handleAnalyzing(srcCodePath, analyzerConfigFilePath, version)

            CliFlags.VALIDATION ->
                handleValidation(srcCodePath, analyzerConfigFilePath, formatterConfigFilePath, version)

            CliFlags.EXECUTION ->
                handleExecution(srcCodePath)
        }

    private fun parseSourceCode(srcCodePath: String): Program {
        val lexer =
            DefaultLexer(
                SplitterFactory.createSplitter(),
                StringToTokenConverterFactory.createDefaultsTokenConverter(),
            )
        val tokenList = lexer.tokenize(getDefaultReader(srcCodePath))
        val nodeBuilder = DefaultNodeBuilder()
        val parser = V1Point0ParserFactory().createParser(tokenList, nodeBuilder)
        return parser.parse().getProgram()
    }

    private fun handleFormatting(
        srcCodePath: String,
        formatterConfigFilePath: String?,
        version: String,
    ): String {
        val program = parseSourceCode(srcCodePath)
        val formatter = FormatterService()
        return formatter.formatToString(
            program,
            version,
            formatterConfigFilePath ?: "formatter_config.json",
        )
    }

    private fun handleAnalyzing(
        srcCodePath: String,
        analyzerConfigFilePath: String?,
        version: String,
    ): String {
        val program = parseSourceCode(srcCodePath)
        val analyzer = AnalyzerFactory.create(version, analyzerConfigFilePath)
        val diagnostics = analyzer.analyze(program)
        return if (diagnostics.isEmpty()) {
            "No issues found"
        } else {
            diagnostics.joinToString("\n") { it.message }
        }
    }

    private fun handleValidation(
        srcCodePath: String,
        analyzerConfigFilePath: String?,
        formatterConfigFilePath: String?,
        version: String,
    ): String {
        val analysisResult = handleAnalyzing(srcCodePath, analyzerConfigFilePath, version)
        val formattingResult = handleFormatting(srcCodePath, formatterConfigFilePath, version)

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
