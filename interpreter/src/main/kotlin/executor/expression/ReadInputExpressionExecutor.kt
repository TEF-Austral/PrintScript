package executor.expression

import node.Expression
import node.ReadInputExpression
import result.InterpreterResult
import type.CommonTypes
import variable.Variable

class ReadInputExpressionExecutor : SpecificExpressionExecutor {
    override fun canHandle(expression: Expression): Boolean = expression is ReadInputExpression

    override fun execute(expression: Expression): InterpreterResult {
        val readInputExpression = expression as ReadInputExpression

        print(readInputExpression.printValue())

        val userInput = readln()

        val resultVariable = Variable(CommonTypes.STRING, userInput) // TODO, CAMBIAR TEMPORAL PERO NO SE ME OCURRE NADA

        return InterpreterResult(
            interpretedCorrectly = true,
            message = "Succes",
            interpreter = resultVariable,
        )
    }
}
