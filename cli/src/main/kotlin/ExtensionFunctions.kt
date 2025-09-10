import factory.AnalyzerFactory
import factory.DefaultInterpreterFactory
import formatter.FormatterService

fun CLI.handleFormatting(
    srcCodePath: String,
    formatterConfigFilePath: String?,
    version: String,
): String {
    val program = parseSourceCode(srcCodePath)
    val formatter = FormatterService()

    val configPath =
        formatterConfigFilePath ?: run {
            val defaultConfig = """{
  "spaceBeforeColon": true,
  "spaceAfterColon": true,
  "spaceAroundAssignment": true,
  "blankLinesBeforePrintln": 1
}"""

            val tempFile = kotlin.io.path.createTempFile("FormattingConfiguration", ".json")
            tempFile.toFile().writeText(defaultConfig)

            tempFile.toString()
        }

    return formatter.formatToString(program, version, configPath)
}

fun CLI.handleAnalyzing(
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

fun CLI.handleValidation(
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

fun CLI.handleExecution(srcCodePath: String): String {
    val program = parseSourceCode(srcCodePath)
    val interpreter = DefaultInterpreterFactory.createDefaultInterpreter()
    val result = interpreter.interpret(program)

    return if (result.interpretedCorrectly) {
        "Program executed successfully"
    } else {
        "Program executed with errors: ${result.message}"
    }
}
