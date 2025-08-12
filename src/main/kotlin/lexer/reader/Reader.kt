package lexer.reader

import java.io.File

sealed interface Reader {
    fun read(): String
}

class TxtReader(private val path: String) : Reader {

    override fun read(): String {
        return try {
            File(path).readText()
        } catch (e: java.io.IOException) {
            ""
        }
    }
}

class MockReader(private val content: String) : Reader {
    override fun read(): String {
        return content
    }
}