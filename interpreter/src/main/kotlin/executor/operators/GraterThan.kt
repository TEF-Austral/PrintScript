package executor.operators

import result.InterpreterResult
import variable.Variable
import type.CommonTypes

object GraterThan : Operator {
    override fun canHandle(symbol: String): Boolean = symbol == ">"

    override fun operate(
        left: Variable,
        right: Variable,
    ): InterpreterResult {
        // 1. Intenta comparar como números.
        val numericResult = compareAsDouble(left, right)
        if (numericResult != null) {
            val resultVar = Variable(CommonTypes.BOOLEAN_LITERAL, numericResult)
            return InterpreterResult(true, "Success GreaterThan (Numeric)", resultVar)
        }

        // 2. Intenta comparar como booleanos.
        val booleanResult = compareAsBoolean(left, right)
        if (booleanResult != null) {
            val resultVar = Variable(CommonTypes.BOOLEAN_LITERAL, booleanResult)
            return InterpreterResult(true, "Success GreaterThan (Boolean)", resultVar)
        }

        // 3. Intenta comparar si ambos son explícitamente del tipo STRING.
        val stringResult = compareIfBothAreStrings(left, right)
        if (stringResult != null) {
            val resultVar = Variable(CommonTypes.BOOLEAN_LITERAL, stringResult)
            return InterpreterResult(true, "Success GreaterThan (String)", resultVar)
        }

        // 4. Si ninguna de las comparaciones anteriores fue posible, falla.
        return InterpreterResult(
            false,
            "Type mismatch: Incompatible types for > operation between ${left.getType()} and ${right.getType()}.",
            null,
        )
    }

    /**
     * Compara los valores si ambos pueden ser convertidos a Double.
     */
    private fun compareAsDouble(
        left: Variable,
        right: Variable,
    ): Boolean? {
        val leftDouble = left.getValue()?.toString()?.toDoubleOrNull()
        val rightDouble = right.getValue()?.toString()?.toDoubleOrNull()
        return if (leftDouble != null && rightDouble != null) {
            leftDouble > rightDouble
        } else {
            null
        }
    }

    private fun compareAsBoolean(
        left: Variable,
        right: Variable,
    ): Boolean? {
        val leftBool = toBooleanOrNull(left.getValue())
        val rightBool = toBooleanOrNull(right.getValue())
        return if (leftBool != null && rightBool != null) {
            leftBool && !rightBool
        } else {
            null
        }
    }

    private fun compareIfBothAreStrings(
        left: Variable,
        right: Variable,
    ): Boolean? {
        if (left.getType() == CommonTypes.STRING_LITERAL && right.getType() == CommonTypes.STRING_LITERAL) {
            val leftStr = left.getValue()?.toString() ?: ""
            val rightStr = right.getValue()?.toString() ?: ""
            return leftStr > rightStr
        }
        return null
    }

    private fun toBooleanOrNull(value: Any?): Boolean? {
        val stringValue = value?.toString()
        return when {
            stringValue.equals("true", ignoreCase = true) -> true
            stringValue.equals("false", ignoreCase = true) -> false
            else -> null
        }
    }
}
