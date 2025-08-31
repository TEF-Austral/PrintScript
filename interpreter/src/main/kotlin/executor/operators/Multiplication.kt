package executor.operators

import variable.Variable
import type.CommonTypes

object Multiplication : Operator {
    override fun canHandle(symbol: String): Boolean = symbol == "*"

    override fun operate(
        left: Variable,
        right: Variable,
    ): Variable {
        val intResult = multiplyAsInt(left, right)
        if (intResult != null) return intResult

        val doubleResult = multiplyAsDouble(left, right)
        if (doubleResult != null) return doubleResult

        val stringResult = multiplyIntString(left, right)
        if (stringResult != null) return stringResult

        return Variable(CommonTypes.STRING_LITERAL, null)
    }

    private fun multiplyAsInt(
        left: Variable,
        right: Variable,
    ): Variable? {
        val leftInt = left.getValue()?.toString()?.toIntOrNull()
        val rightInt = right.getValue()?.toString()?.toIntOrNull()
        return if (leftInt != null && rightInt != null) {
            Variable(CommonTypes.NUMBER_LITERAL, leftInt * rightInt)
        } else {
            null
        }
    }

    private fun multiplyAsDouble(
        left: Variable,
        right: Variable,
    ): Variable? {
        val leftDouble = left.getValue()?.toString()?.toDoubleOrNull()
        val rightDouble = right.getValue()?.toString()?.toDoubleOrNull()
        return if (leftDouble != null && rightDouble != null) {
            Variable(CommonTypes.NUMBER_LITERAL, leftDouble * rightDouble)
        } else {
            null
        }
    }

    private fun multiplyIntString(
        left: Variable,
        right: Variable,
    ): Variable? {
        val leftInt = left.getValue()?.toString()?.toIntOrNull()
        val rightStr = right.getValue()?.toString()
        if (leftInt != null && rightStr != null) {
            return Variable(CommonTypes.STRING_LITERAL, rightStr.repeat(leftInt))
        }
        val rightInt = right.getValue()?.toString()?.toIntOrNull()
        val leftStr = left.getValue()?.toString()
        if (rightInt != null && leftStr != null) {
            return Variable(CommonTypes.STRING_LITERAL, leftStr.repeat(rightInt))
        }
        return null
    }

    // TODO se podria cmabair la ultima funcion, quiero ver que ande primero
}
