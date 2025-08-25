import executor.Executor
import node.Program

class Interpreter(private val executor: Executor) {
    fun interpret(program: Program) {
        executor.execute(program)
    }
}