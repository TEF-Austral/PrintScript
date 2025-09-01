package result

import variable.Variable

data class InterpreterResult(
    val interpretedCorrectly: Boolean,
    val message: String,
    val interpreter: Variable?,
)
