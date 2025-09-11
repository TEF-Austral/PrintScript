package executor.expression

import data.DataBase
import emitter.Emitter
import node.Expression
import node.ReadInputExpression
import result.InterpreterResult
import type.CommonTypes
import variable.Variable

class ReadInputExpressionExecutor(
    private val emitter: Emitter,
) : SpecificExpressionExecutor {
    override fun canHandle(expression: Expression): Boolean = expression is ReadInputExpression

    override fun execute(
        expression: Expression,
        database: DataBase,
    ): InterpreterResult {
        val readInputExpression = expression as ReadInputExpression

        emitter.stringEmit(readInputExpression.printValue())

        val userInput = readln()

        val resultVariable = Variable(CommonTypes.STRING, userInput)

        return InterpreterResult(
            interpretedCorrectly = true,
            message = "Succes",
            interpreter = resultVariable,
        )
    }
}
