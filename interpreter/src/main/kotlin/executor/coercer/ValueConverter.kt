package executor.coercer

import variable.Variable

sealed interface ValueConverter {
    fun canHandle(targetType: Any): Boolean

    fun convert(rawValue: String): Variable?
}
