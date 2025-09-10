package executor.coercer

import type.CommonTypes
import variable.Variable

class StringToBooleanConverter : ValueConverter {
    override fun canHandle(targetType: Any): Boolean = targetType == CommonTypes.BOOLEAN

    override fun convert(rawValue: String): Variable? =
        when (rawValue.lowercase()) {
            "true" -> Variable(CommonTypes.BOOLEAN_LITERAL, true)
            "false" -> Variable(CommonTypes.BOOLEAN_LITERAL, false)
            else -> null
        }
}
