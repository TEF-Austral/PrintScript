package reader

import java.io.File
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ReaderTest {

    @Test
    fun `TxtReader should read content from a valid file`() {
        val filePath = "src/test/resources/script.txt"
        val expectedContent = File(filePath).readText()

        val reader = TxtReader(filePath)
        val actualContent = reader.read()
        print(expectedContent)

        assertEquals(expectedContent, actualContent)
    }

    @Test
    fun `TxtReader should return an empty String for a non-existent file`() {
        val nonExistentFilePath = "non/existent/file.txt"
        val reader = TxtReader(nonExistentFilePath)

        assertEquals("", reader.read())
    }
}