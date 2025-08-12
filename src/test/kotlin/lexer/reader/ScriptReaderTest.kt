import lexer.reader.TxtReader
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import java.nio.file.Paths

class ScriptReaderTest {

  @Test
  fun `read returns content from classpath script txt`() {
    val resourceUrl = javaClass.classLoader.getResource("script.txt")!!
    val path = Paths.get(resourceUrl.toURI())
    val content = TxtReader(path.toString()).read().trimEnd()
    assertEquals("let x = 42;", content)
  }
}
