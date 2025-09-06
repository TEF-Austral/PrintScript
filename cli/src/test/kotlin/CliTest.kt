import flags.CliFlags
import java.io.File // <--- Importante agregar esto
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class CliTest {
    @Test
    fun `CLI FORMATTING Test`() {
        // Rutas a los archivos de prueba
        val srcPath = "src/test/resources/cliFormattingTest.txt"
        val expectedPath = "src/test/resources/cliFormattingResult.txt"
        val formatterConfigPath = "src/test/resources/FormattingConfiguration.json"

        // 1. Ejecutar el formateador para obtener el resultado actual (un String)
        val actualResult = CLI().execute(CliFlags.FORMATTING, srcPath, null, formatterConfigPath)

        // 2. Leer el contenido del archivo con el resultado esperado (otro String)
        val expectedContent = File(expectedPath).readText()

        // 3. Comparar los dos strings (contenido vs. contenido)
        assertEquals(expectedContent, actualResult)
    }

    @Test
    fun `CLI ANALYZER Test`() {
        // Rutas a los archivos de prueba
        val srcPath = "src/test/resources/cliAnalyzerTest"
        val expectedPath = "src/test/resources/cliAnalyzerResult.txt"
        // Corregido: el nombre del archivo de configuración
        val analyzerConfigPath = "src/test/resources/AnalyzerConfiguration.json"

        // 1. Ejecutar el analizador para obtener el resultado actual (un String)
        val actualResult = CLI().execute(CliFlags.ANALYZING, srcPath, analyzerConfigPath, null)

        // 2. Leer el contenido del archivo con el resultado esperado (otro String)
        val expectedContent = File(expectedPath).readText()

        // 3. Comparar los dos strings (contenido vs. contenido)
        assertEquals(expectedContent, actualResult)
    }

    @Test
    fun `CLI VALIDATION Test`() {
        // Rutas a los archivos de prueba
        val srcPath = "src/test/resources/cliAnalyzerTest"
        val expectedPath = "src/test/resources/cliValidationResult"
        val formatterConfigPath = "src/test/resources/FormattingConfiguration.json"
        val analyzerConfigPath = "src/test/resources/AnalyzerConfiguration.json"

        val actualResult = CLI().execute(CliFlags.VALIDATION, srcPath, analyzerConfigPath, formatterConfigPath)

        val expectedContent = File(expectedPath).readText()

        assertEquals(expectedContent, actualResult)
    }

    @Test
    fun `CLI Execution Test`() {
        // Rutas a los archivos de prueba
        val srcPath = "src/test/resources/cliExecutionTest"

        val actualResult = CLI().execute(CliFlags.EXECUTION, srcPath, null, null)

        assertEquals("Program executed successfully", actualResult)
    }
}
