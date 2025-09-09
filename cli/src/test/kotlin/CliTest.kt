import flags.CliFlags
import java.io.File // <--- Importante agregar esto
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class CliTest {
    @Test
    fun `CLI FORMATTING Test`() {
        val srcPath = "src/test/resources/cliFormattingTest.txt"
        val expectedPath = "src/test/resources/cliFormattingResult.txt"
        val formatterConfigPath = "src/test/resources/FormattingConfiguration.json"

        val actualResult =
            CLI().execute(
                CliFlags.FORMATTING,
                srcPath,
                null,
                formatterConfigPath,
            )
        val expectedContent = File(expectedPath).readText()

        assertEquals(expectedContent, actualResult)
    }

    @Test
    fun `CLI ANALYZER Test`() {
        val srcPath = "src/test/resources/cliAnalyzerTest"
        val expectedPath = "src/test/resources/cliAnalyzerResult.txt"
        val analyzerConfigPath = "src/test/resources/AnalyzerConfiguration.json"

        val actualResult =
            CLI().execute(
                CliFlags.ANALYZING,
                srcPath,
                analyzerConfigPath,
                null,
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

        val actualResult =
            CLI().execute(
                CliFlags.VALIDATION,
                srcPath,
                analyzerConfigPath,
                formatterConfigPath,
                "1.0",
            )
        val expectedContent = File(expectedPath).readText()

        assertEquals(expectedContent, actualResult)
    }

    @Test
    fun `CLI Execution Test`() {
        val srcPath = "src/test/resources/cliExecutionTest"

        val actualResult =
            CLI().execute(
                CliFlags.EXECUTION,
                srcPath,
                null,
                null,
            )

        assertEquals("Program executed successfully", actualResult)
    }
}
