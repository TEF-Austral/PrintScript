package executor.coercer

import type.CommonTypes
import variable.Variable

class StringToStringConverter : ValueConverter {
    override fun canHandle(targetType: Any): Boolean = targetType == CommonTypes.STRING

    override fun convert(rawValue: String): Variable = Variable(CommonTypes.STRING_LITERAL, rawValue)
}
