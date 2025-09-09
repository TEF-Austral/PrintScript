package executor.coercer

import type.CommonTypes
import variable.Variable

class StringToNumberConverter : ValueConverter {
    override fun canHandle(targetType: Any): Boolean = targetType == CommonTypes.NUMBER

    override fun convert(rawValue: String): Variable? =
        if (rawValue.contains('.')) {
            rawValue.toDoubleOrNull()?.let { doubleValue ->
                Variable(CommonTypes.NUMBER, doubleValue)
            }
        } else {
            rawValue.toIntOrNull()?.let { intValue ->
                Variable(CommonTypes.NUMBER, intValue)
            }
        }
}
