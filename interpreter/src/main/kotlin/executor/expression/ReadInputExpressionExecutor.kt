package executor.expression

import data.DataBase
import input.InputProvider
import node.Expression
import node.ReadInputExpression
import result.InterpreterResult
import type.CommonTypes
import variable.Variable

class ReadInputExpressionExecutor(
    private val inputProvider: InputProvider,
) : SpecificExpressionExecutor {
    override fun canHandle(expression: Expression): Boolean = expression is ReadInputExpression

    override fun execute(
        expression: Expression,
        database: DataBase,
    ): InterpreterResult {
        val readInputExpression = expression as ReadInputExpression

        val userInput = inputProvider.input(readInputExpression.printValue())

        return userInput
    }
}
