package executor.operators

object Sum : Operator {
    override fun canHandle(symbol: String): Boolean = symbol == "+"

    override fun operate(
        left: String,
        right: String,
    ): String {
        val toInt = toInt(left, right)
        if (toInt != null) return toInt.toString()
        val toDouble = toDouble(left, right)
        if (toDouble != null) return toDouble.toString()
        return left + right
    }

    private fun toInt(
        left: String,
        right: String,
    ): Int? {
        if (left.toIntOrNull() != null && right.toIntOrNull() != null) {
            return left.toInt() + right.toInt()
        }
        return null
    }

    private fun toDouble(
        left: String,
        right: String,
    ): Double? {
        if (left.toDoubleOrNull() != null && right.toDoubleOrNull() != null) {
            return left.toDouble() + right.toDouble()
        }
        return null
    }
}
