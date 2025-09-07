package executor.statement

import data.DataBase
import executor.expression.DefaultExpressionExecutor
import node.DeclarationStatement
import node.Statement
import result.InterpreterResult
import utils.areTypesCompatible
import variable.Variable

class DeclarationStatementExecutor(
    private val dataBase: DataBase,
    private val defaultExpressionExecutor: DefaultExpressionExecutor,
) : SpecificStatementExecutor {
    override fun canHandle(statement: Statement): Boolean = statement is DeclarationStatement

    override fun execute(statement: Statement): InterpreterResult {
        return try {
            val declarationStatement = statement as DeclarationStatement
            val identifier = declarationStatement.getIdentifier()
            val declaredType = declarationStatement.getDataType()
            val initialValueExpression = declarationStatement.getInitialValue()

            val variable: Variable

            if (initialValueExpression != null) {
                val expressionResult = defaultExpressionExecutor.execute(initialValueExpression)
                if (!expressionResult.interpretedCorrectly) {
                    return expressionResult
                }

                val initialValue =
                    expressionResult.interpreter
                        ?: return InterpreterResult(false, "Error: Expression did not yield a value", null)

                if (!areTypesCompatible(declaredType, initialValue.getType())) {
                    return InterpreterResult(
                        false,
                        "Error: Type mismatch. Cannot assign value of type '${initialValue.getType()}' to variable '$identifier' declared as '$declaredType'",
                        null,
                    )
                }
                variable = Variable(declaredType, initialValue.getValue())
            } else {
                variable = Variable(declaredType, null)
            }

            dataBase.addVariable(identifier, variable)

            InterpreterResult(true, "Declaration executed successfully", variable)
        } catch (e: Exception) {
            InterpreterResult(false, "Error executing declaration statement: ${e.message}", null)
        }
    }
}
