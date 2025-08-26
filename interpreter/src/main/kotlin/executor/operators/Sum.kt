package executor.operators

object Sum: Operator {

    override fun canHandle(symbol: String): Boolean {
        return symbol == "+"
    }

    override fun operate(left: Any, right: Any): Any {
        return if (left is String || right is String) {
            operateString(left, right)
        } else {
            sum(left, right)
        }
    }

    private fun operateString(left: Any, right: Any): String {
        return left.toString() + right.toString()
    }

    private fun sum(left: Any, right: Any): Any {
        return when (left) {
            is Int if right is Int -> left + right
            is Double if right is Double -> left + right
            is Float if right is Float -> left + right
            is Long if right is Long -> left + right
            is Number if right is Number -> left.toDouble() + right.toDouble()
            else -> "" // Todo Llamar al que tira errores
        }
    }

    /*
    "+" -> {
                when {
                    left is Int && right is Int -> left + right
                    else -> left.toString() + right.toString()
                }
            }
     */
}