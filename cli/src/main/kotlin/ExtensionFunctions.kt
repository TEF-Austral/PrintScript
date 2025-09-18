import factory.AnalyzerFactory.createAnalyzer
import factory.DefaultInterpreterFactory.createInterpreter
import formatter.factory.DefaultFormatterFactory.createFormatter
import parser.stream.ParserAstStream
import type.Version

fun CLI.handleFormatting(
    srcCodePath: String,
    formatterConfigFilePath: String?,
    version: Version,
): String {
    val configPath =
        formatterConfigFilePath ?: "src/test/resources/configuration/FormattingConfiguration.json"
    val tokens = tokeniseSourceCode(srcCodePath, version)
    return createFormatter(version).formatToString(tokens, parseFormatConfigFromFile(configPath))
}

fun CLI.handleAnalyzing(
    srcCodePath: String,
    analyzerConfigFilePath: String?,
    version: Version,
): String {
    val configPath =
        analyzerConfigFilePath ?: "src/test/resources/configuration/AnalyzerConfiguration.json"
    val analyzer = createAnalyzer(version, parseAnalyzerConfigFromFile(configPath))
    val diagnostics = analyzer.analyze(parseSourceCode(srcCodePath, version))
    return if (diagnostics.isEmpty()) {
        "No issues found"
    } else {
        diagnostics.joinToString("\n") { it.message }
    }
}

fun CLI.handleValidation(
    srcCodePath: String,
    analyzerConfigFilePath: String?,
    formatterConfigFilePath: String?,
    version: Version,
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

fun CLI.handleExecution(
    srcCodePath: String,
    version: Version,
): String {
    val interpreter = createInterpreter(version)
    val astStream = ParserAstStream(parseSourceCode(srcCodePath, version).getParser())
    val result = interpreter.interpret(astStream)
    return if (result.interpretedCorrectly) {
        "Program executed successfully"
    } else {
        "Program executed with errors: ${result.message}"
    }
}
