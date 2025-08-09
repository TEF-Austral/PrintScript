package lexer.reader

import java.io.File

sealed interface Reader {
    fun read(): String
}

class TxtReader(private val path: String) : Reader {

    // \t y \n
    override fun read(): String {
        val filePath = path
        return File(filePath).readText()
    }

}