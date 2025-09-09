package executor.coercer

import result.InterpreterResult

interface ITypeCoercer {
    fun coerce(
        rawValue: String,
        targetType: Any,
    ): InterpreterResult
}
