package token

interface Coordinates {
    fun getLine(): Int
    fun getColumn(): Int
}

class Position(private val line: Int, private val column: Int) : Coordinates {
    override fun getLine(): Int {
        return line
    }

    override fun getColumn(): Int {
        return column
    }
}