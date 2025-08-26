package executor.operators

object Multiplication: Operator {
    override fun canHandle(symbol: String): Boolean {
        return symbol == "*"
    }

    override fun operate(left: String, right: String): String {
        val toInt = toInt(left, right)
        if (toInt != 0) {
            return toInt.toString()
        }
        val toDouble = toDouble(left, right)
        if (toDouble != 0.0) {
            return toDouble.toString()
        }
        return "0"
    }

    private fun toInt(left: String, right: String): Int {
        val l = left.toIntOrNull()
        val r = right.toIntOrNull()
        if (l != null && r != null) {
            return l * r
        }
        return 0
    }

    private fun toDouble(left: String, right: String): Double {
        val l = left.toDoubleOrNull()
        val r = right.toDoubleOrNull()
        if (l != null && r != null) {
            return l * r
        }
        return 0.0
    }
}