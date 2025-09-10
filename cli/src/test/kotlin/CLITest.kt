import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class CLITest {
    @Test
    fun `CLI FORMATTING Test`() {
        val srcPath = "src/test/resources/cliFormattingTest.txt"
        val expectedPath = "src/test/resources/cliFormattingResult.txt"
        val formatterConfigPath = "src/test/resources/FormattingConfiguration.json"

        val cmdCli = CLI()
        val actualResult =
            cmdCli.handleFormatting(
                srcPath,
                formatterConfigPath,
                "1.0",
            )
        val expectedContent = File(expectedPath).readText()

        assertEquals(
            expectedContent.replace("\r\n", "\n"),
            actualResult.replace("\r\n", "\n"))
    }

    @Test
    fun `CLI ANALYZER Test`() {
        val srcPath = "src/test/resources/cliAnalyzerTest"
        val expectedPath = "src/test/resources/cliAnalyzerResult.txt"
        val analyzerConfigPath = "src/test/resources/AnalyzerConfiguration.json"

        val cmdCli = CLI()
        val actualResult =
            cmdCli.handleAnalyzing(
                srcPath,
                analyzerConfigPath,
                "1.0",
            )
        val expectedContent = File(expectedPath).readText()

        assertEquals(expectedContent, actualResult)
    }

    @Test
    fun `CLI VALIDATION Test`() {
        val srcPath = "src/test/resources/cliAnalyzerTest"
        val expectedPath = "src/test/resources/cliValidationResult"
        val formatterConfigPath = "src/test/resources/FormattingConfiguration.json"
        val analyzerConfigPath = "src/test/resources/AnalyzerConfiguration.json"

        val cmdCli = CLI()
        val actualResult =
            cmdCli.handleValidation(
                srcPath,
                analyzerConfigPath,
                formatterConfigPath,
                "1.0",
            )
        val expectedContent = File(expectedPath).readText()

        assertEquals(
            expectedContent.replace(" ", "").replace("\r\n", "\n"),
            actualResult.replace(" ", "").replace("\r\n", "\n"),
        )
    }

    @Test
    fun `CLI Execution Test`() {
        val srcPath = "src/test/resources/cliExecutionTest"

        val cmdCli = CLI()
        val actualResult = cmdCli.handleExecution(srcPath)

        assertEquals("Program executed successfully", actualResult)
    }

    // NEW COMMAND LINE INTERFACE TESTS

    @Test
    fun `Format Command Test`() {
        val srcPath = "src/test/resources/cliFormattingTest.txt"
        val formatterConfigPath = "src/test/resources/FormattingConfiguration.json"
        val expectedPath = "src/test/resources/cliFormattingResult.txt"

        val outputStream = ByteArrayOutputStream()
        val originalOut = System.out
        System.setOut(PrintStream(outputStream))

        try {
            val formatCommand = FormatCommand()
            // Simulate command line arguments
            formatCommand.main(
                arrayOf(
                    srcPath,
                    "-c",
                    formatterConfigPath,
                    "-v",
                    "1.0",
                ),
            )

            val actualOutput = outputStream.toString().trim()
            val expectedContent = File(expectedPath).readText().trim()
            assertEquals(
                expectedContent.replace("\r\n", "\n"),
                actualOutput.replace("\r\n", "\n"))
        } finally {
            System.setOut(originalOut)
        }
    }

    @Test
    fun `Format Command Test Without Config`() {
        val srcPath = "src/test/resources/cliFormattingTest.txt"

        val outputStream = ByteArrayOutputStream()
        val originalOut = System.out
        System.setOut(PrintStream(outputStream))

        try {
            val formatCommand = FormatCommand()
            formatCommand.main(arrayOf(srcPath, "-v", "1.0"))

            val actualOutput = outputStream.toString().trim()
            // Should use default formatter config
            assertTrue(actualOutput.isNotEmpty())
        } finally {
            System.setOut(originalOut)
        }
    }

    @Test
    fun `Analyze Command Test`() {
        val srcPath = "src/test/resources/cliAnalyzerTest"
        val analyzerConfigPath = "src/test/resources/AnalyzerConfiguration.json"
        val expectedPath = "src/test/resources/cliAnalyzerResult.txt"

        val outputStream = ByteArrayOutputStream()
        val originalOut = System.out
        System.setOut(PrintStream(outputStream))

        try {
            val analyzeCommand = AnalyzeCommand()
            analyzeCommand.main(
                arrayOf(
                    srcPath,
                    "-c",
                    analyzerConfigPath,
                    "-v",
                    "1.0",
                ),
            )

            val actualOutput = outputStream.toString().trim()
            val expectedContent = File(expectedPath).readText().trim()
            assertEquals(expectedContent, actualOutput)
        } finally {
            System.setOut(originalOut)
        }
    }

    @Test
    fun `Analyze Command Test Without Config`() {
        val srcPath = "src/test/resources/cliAnalyzerTest"

        val outputStream = ByteArrayOutputStream()
        val originalOut = System.out
        System.setOut(PrintStream(outputStream))

        try {
            val analyzeCommand = AnalyzeCommand()
            analyzeCommand.main(arrayOf(srcPath, "-v", "1.0"))

            val actualOutput = outputStream.toString().trim()
            assertTrue(actualOutput.isNotEmpty())
        } finally {
            System.setOut(originalOut)
        }
    }

    @Test
    fun `Validate Command Test`() {
        val srcPath = "src/test/resources/cliAnalyzerTest"
        val analyzerConfigPath = "src/test/resources/AnalyzerConfiguration.json"
        val formatterConfigPath = "src/test/resources/FormattingConfiguration.json"
        val expectedPath = "src/test/resources/cliValidationResult"

        val outputStream = ByteArrayOutputStream()
        val originalOut = System.out
        System.setOut(PrintStream(outputStream))

        try {
            val validateCommand = ValidateCommand()
            validateCommand.main(
                arrayOf(
                    srcPath,
                    "-ac",
                    analyzerConfigPath,
                    "-fc",
                    formatterConfigPath,
                    "-v",
                    "1.0",
                ),
            )

            val actualOutput = outputStream.toString().trim()
            val expectedContent = File(expectedPath).readText().trim()
            assertEquals(
                expectedContent
                    .replace(" ", "")
                    .replace("\r\n", "\n"),
                actualOutput.replace(" ", "").replace("\r\n", "\n"),
            )
        } finally {
            System.setOut(originalOut)
        }
    }

    @Test
    fun `Execute Command Test`() {
        val srcPath = "src/test/resources/cliExecutionTest"

        val outputStream = ByteArrayOutputStream()
        val originalOut = System.out
        System.setOut(PrintStream(outputStream))

        try {
            val executeCommand = ExecuteCommand()
            executeCommand.main(arrayOf(srcPath))

            val actualOutput = outputStream.toString().trim()
            assertEquals(
                "42\nProgram executed successfully",
                actualOutput.trim().replace("\r\n", "\n"),
            )
        } finally {
            System.setOut(originalOut)
        }
    }

    @Test
    fun `Main CLI Test with Help`() {
        val outputStream = ByteArrayOutputStream()
        val originalOut = System.out
        System.setOut(PrintStream(outputStream))

        try {
            val cli = CLI()
            cli.main(arrayOf())

            val actualOutput = outputStream.toString()
            assertTrue(actualOutput.contains("Welcome to the Source Code CLI Tool!"))
            assertTrue(actualOutput.contains("Available commands:"))
            assertTrue(actualOutput.contains("format   - Format source code"))
            assertTrue(actualOutput.contains("analyze  - Analyze source code for issues"))
            assertTrue(actualOutput.contains("validate - Validate source code (analyze + format)"))
            assertTrue(actualOutput.contains("execute  - Execute source code"))
        } finally {
            System.setOut(originalOut)
        }
    }

    @Test
    fun `Main CLI Test with Format Subcommand`() {
        val srcPath = "src/test/resources/cliFormattingTest.txt"
        val outputStream = ByteArrayOutputStream()
        val originalOut = System.out
        System.setOut(PrintStream(outputStream))

        try {
            main(arrayOf("format", srcPath, "-v", "1.0"))
            val actualOutput = outputStream.toString().trim()
            assertTrue(actualOutput.isNotEmpty())
        } finally {
            System.setOut(originalOut)
        }
    }

    @Test
    fun `Main CLI Test with Analyze Subcommand`() {
        val srcPath = "src/test/resources/cliAnalyzerTest"
        val outputStream = ByteArrayOutputStream()
        val originalOut = System.out
        System.setOut(PrintStream(outputStream))

        try {
            main(arrayOf("analyze", srcPath, "-v", "1.0"))
            val actualOutput = outputStream.toString().trim()
            assertTrue(actualOutput.isNotEmpty())
        } finally {
            System.setOut(originalOut)
        }
    }

    @Test
    fun `Main CLI Test with Validate Subcommand`() {
        val srcPath = "src/test/resources/cliAnalyzerTest"
        val outputStream = ByteArrayOutputStream()
        val originalOut = System.out
        System.setOut(PrintStream(outputStream))

        try {
            main(arrayOf("validate", srcPath, "-v", "1.0"))
            val actualOutput = outputStream.toString().trim()
            assertTrue(actualOutput.contains("--- ANALYSIS REPORT---"))
            assertTrue(actualOutput.contains("---FORMATTING PREVIEW---"))
        } finally {
            System.setOut(originalOut)
        }
    }

    @Test
    fun `Main CLI Test with Execute Subcommand`() {
        val srcPath = "src/test/resources/cliExecutionTest"
        val outputStream = ByteArrayOutputStream()
        val originalOut = System.out
        System.setOut(PrintStream(outputStream))

        try {
            main(arrayOf("execute", srcPath))
            val actualOutput = outputStream.toString().trim()
            assertEquals(
                "42\nProgram executed successfully",
                actualOutput.trim().replace("\r\n", "\n"),
            )
        } finally {
            System.setOut(originalOut)
        }
    }

    @Test
    fun `Format Command Default Version Test`() {
        val srcPath = "src/test/resources/cliFormattingTest.txt"

        val outputStream = ByteArrayOutputStream()
        val originalOut = System.out
        System.setOut(PrintStream(outputStream))

        try {
            val formatCommand = FormatCommand()
            formatCommand.main(arrayOf(srcPath)) // No version specified, should use default "1.0"

            val actualOutput = outputStream.toString().trim()
            assertTrue(actualOutput.isNotEmpty())
        } finally {
            System.setOut(originalOut)
        }
    }

    @Test
    fun `Analyze Command Default Version Test`() {
        val srcPath = "src/test/resources/cliAnalyzerTest"

        val outputStream = ByteArrayOutputStream()
        val originalOut = System.out
        System.setOut(PrintStream(outputStream))

        try {
            val analyzeCommand = AnalyzeCommand()
            analyzeCommand.main(arrayOf(srcPath)) // No version specified, should use default "1.0"

            val actualOutput = outputStream.toString().trim()
            assertTrue(actualOutput.isNotEmpty())
        } finally {
            System.setOut(originalOut)
        }
    }

    @Test
    fun `Validate Command Default Version Test`() {
        val srcPath = "src/test/resources/cliAnalyzerTest"

        val outputStream = ByteArrayOutputStream()
        val originalOut = System.out
        System.setOut(PrintStream(outputStream))

        try {
            val validateCommand = ValidateCommand()
            validateCommand.main(arrayOf(srcPath)) // No version specified, should use default "1.0"

            val actualOutput = outputStream.toString().trim()
            assertTrue(actualOutput.contains("--- ANALYSIS REPORT---"))
            assertTrue(actualOutput.contains("---FORMATTING PREVIEW---"))
        } finally {
            System.setOut(originalOut)
        }
    }
}
