import coordinates.Coordinates
import coordinates.Position
import diagnostic.Diagnostic
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class DiagnosticTest {
    @Test
    fun `stores message and coordinates correctly`() {
        val coords: Coordinates = Position(1, 2)
        val diag = Diagnostic("Test message", coords)

        assertEquals("Test message", diag.message)
        assertEquals(coords, diag.position)
    }

    @Test
    fun `equal diagnostics have same message and coordinates`() {
        val coords: Coordinates = Position(3, 4)
        val d1 = Diagnostic("msg", coords)
        val d2 = Diagnostic("msg", coords)

        assertEquals(d1, d2)
        assertEquals(d1.hashCode(), d2.hashCode())
    }

    @Test
    fun `diagnostics with different positions are not equal`() {
        val d1 = Diagnostic("msg", Position(1, 1))
        val d2 = Diagnostic("msg", Position(1, 2))

        assertNotEquals(d1, d2)
    }

    @Test
    fun `diagnostics with different messages are not equal`() {
        val d1 = Diagnostic("msg1", Position(1, 1))
        val d2 = Diagnostic("msg2", Position(1, 1))

        assertNotEquals(d1, d2)
    }
}
