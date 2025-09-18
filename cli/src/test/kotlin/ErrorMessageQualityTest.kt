import factory.DefaultInterpreterFactory.createWithVersionAndEmitterAndInputProvider
import input.TerminalInputProvider
import parser.stream.ParserAstStream
import type.Version
import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ErrorMessageQualityTest {

    @Test
    fun testErrorMessageClarity() {
        val errorCases =
            mapOf(
                "undefined variable" to "println(undefinedVariable);",
                "type mismatch" to "let x: number = \"string\";",
                "missing semicolon" to "let x: number = 5\nprintln(x);",
                "const reassignment" to "const x: number = 5;\nx = 10;",
                "division by zero" to "let result: number = 10 / 0;",
                "malformed expression" to "let x: number = 5 + ;",
                "unmatched parentheses" to "let result: number = (5 + 3;",
                "invalid operator" to "let result: number = 5 & 3;",
            )

        errorCases.forEach { (errorType, code) ->
            testErrorMessage(errorType, code, Version.VERSION_1_1)
        }
    }

    private fun testErrorMessage(
        errorType: String,
        code: String,
        version: Version,
    ) {
        val printCollector = PrintCollector()
        val interpreter =
            createWithVersionAndEmitterAndInputProvider(
                version,
                printCollector,
                TerminalInputProvider(),
            )

        val tempFile = createTempFile(code)

        try {
            val sourceCodeResult = CLI().parseSourceCode(tempFile.absolutePath, version)
            val astStream = ParserAstStream(sourceCodeResult.getParser())
            val result = interpreter.interpret(astStream)

            assertFalse(
                result.interpretedCorrectly,
                "Expected error for case '$errorType' but interpretation succeeded",
            )
            assertTrue(
                result.message.isNotEmpty(),
                "Error message should not be empty for case '$errorType'",
            )
        } finally {
            tempFile.delete()
        }
    }

    private fun createTempFile(content: String): File {
        val tempFile = File.createTempFile("error_test_", ".txt")
        tempFile.writeText(content)
        return tempFile
    }
}
