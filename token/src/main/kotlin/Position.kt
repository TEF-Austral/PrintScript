class Position(
    private val row: Int,
    private val column: Int,
) : Coordinates {
    override fun getRow(): Int = row

    override fun getColumn(): Int = column
}
