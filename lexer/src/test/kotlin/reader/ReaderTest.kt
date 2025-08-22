package reader

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class ReaderTest {
    @Test
    fun `FileReader should read content from a valid file`() {
        val filePath = "src/test/resources/script.txt"
        val expectedContent = File(filePath).readText()

        val reader = FileReader(filePath)
        val actualContent = reader.read()
        print(expectedContent)

        assertEquals(expectedContent, actualContent)
    }

    @Test
    fun `FileReader should return an empty String for a non-existent file`() {
        val nonExistentFilePath = "non/existent/file.txt"
        val reader = FileReader(nonExistentFilePath)

        assertEquals("", reader.read())
    }
}
