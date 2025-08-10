package lexer.reader

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import java.nio.file.Path
import java.io.IOException
import kotlin.io.path.writeText

class ReaderTest {

    @TempDir
    lateinit var tempDir: Path

    @Test
    fun `read returns file content`() {
        val file = tempDir.resolve("sample.txt")
        file.writeText("Hello, PrintScript!")
        val reader = TxtReader(file.toString())

        val content = reader.read()

        assertEquals("Hello, PrintScript!", content)
    }

    @Test
    fun `read non existing file throws IOException`() {
        val missingPath = tempDir.resolve("missing.txt").toString()
        val reader = TxtReader(missingPath)

        assertThrows(IOException::class.java) {
            reader.read()
        }
    }
}