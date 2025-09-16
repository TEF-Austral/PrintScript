import formatter.config.FormatConfig
import formatter.config.YamlFormatConfigParser
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertNull

class YamlFormatConfigParserTest {
    private val parser = YamlFormatConfigParser()

    @Test
    fun `parse full yaml config`() {
        val yaml =
            """
            spaceBeforeColon: true
            spaceAfterColon: false
            spaceAroundAssignment: false
            blankLinesAfterPrintln: 2
            indentSize: 6
            """.trimIndent()
        val cfg = parser.parse(yaml)

        assertTrue(cfg.spaceBeforeColon == true)
        assertFalse(cfg.spaceAfterColon == true)
        assertFalse(cfg.spaceAroundAssignment == true)
        assertEquals(2, cfg.blankLinesAfterPrintln)
        assertEquals(6, cfg.indentSize)
    }

    @Test
    fun `defaults applied when missing`() {
        val yaml = ""
        val cfg = parser.parse(yaml)

        assertNull(cfg.spaceBeforeColon)
        assertNull(cfg.spaceAfterColon)
        assertNull(cfg.spaceAroundAssignment)
        assertEquals(0, cfg.blankLinesAfterPrintln)
        assertEquals(FormatConfig.DEFAULT_SIZE, cfg.indentSize)
    }
}
