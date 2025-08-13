package reader

import java.io.File
import java.io.IOException

class TxtReader(private val path: String) : Reader {

    override fun read(): String {
        return try {
            File(path).readText()
        } catch (e: IOException) {
            ""
        }
    }
}