package token

interface Coordinates {
    fun getRow(): Int
    fun getColumn(): Int
}

class Position(private val row: Int, private val column: Int) : Coordinates {
    override fun getRow(): Int {
        return row
    }

    override fun getColumn(): Int {
        return column
    }
}