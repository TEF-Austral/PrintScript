import executor.Executor
import node.Program

class Interpreter(private val executor: Executor) {
    fun interpret(program: Program) {
        for (statements in program.getStatements()) {
            executor.execute(statements)
        }
    }
}