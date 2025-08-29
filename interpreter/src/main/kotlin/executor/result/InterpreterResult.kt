package executor.result

data class InterpreterResult(
    val interpretedCorrectly: Boolean,
    val message: String,
    val interpreter: Any? = null
)
