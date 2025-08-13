class Position(override val row: Int, override val column: Int) : Coordinates {

    override fun getRow(): Int {
        return row
    }

    override fun getColumn(): Int {
        return column
    }
}