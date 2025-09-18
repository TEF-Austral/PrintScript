import input.InputProvider
import result.InterpreterResult
import type.CommonTypes
import variable.Variable

class InputCollector : InputProvider {
    override fun input(name: String): InterpreterResult =
        InterpreterResult(
            true,
            name,
            Variable(
                CommonTypes.STRING_LITERAL,
                "Hello World!",
            ),
            null,
        )
}
