package executor.operators

object Divide : Operator {
    override fun canHandle(symbol: String): Boolean = symbol == "/"

    override fun operate(
        left: String,
        right: String,
    ): String {
        val toInt = toInt(left, right)
        if (toInt == null) {
            val toDouble = toDouble(left, right)
            return toDouble?.toString() ?: "0"
        }
        return if (toInt.first == 0) {
            toInt.second.toString()
        } else {
            toInt.first.toString()
        }
    }

    private fun toInt(
        left: String,
        right: String,
    ): Pair<Int, Double>? {
        val l = left.toIntOrNull()
        val r = right.toIntOrNull()
        if (l != null && r != null && r != 0) {
            if (l % r != 0) return Pair(0, l.toDouble() / r)
            return Pair(l / r, 0.0)
        }
        return null
    }

    private fun toDouble(
        left: String,
        right: String,
    ): Double? {
        val l = left.toDoubleOrNull()
        val r = right.toDoubleOrNull()
        if (l != null && r != null && r != 0.0) return l / r
        return null
    }
}
