package executor.statement

import executor.expression.DefaultExpressionExecutor
import node.AssignmentStatement
import node.Statement
import result.InterpreterResult
import utils.areTypesCompatible
import variable.Variable

class AssignmentStatementExecutor(
    private val variables: MutableMap<String, Variable>,
    private val defaultExpressionExecutor: DefaultExpressionExecutor,
) : SpecificStatementExecutor {
    override fun canHandle(statement: Statement): Boolean = statement is AssignmentStatement

    override fun execute(statement: Statement): InterpreterResult {
        return try {
            val assignStmt = statement as AssignmentStatement
            val identifier = assignStmt.getIdentifier()

            val existingVariable =
                getVariable(identifier)
                    ?: return createVariableNotFoundError(identifier)

            val expressionResult = evaluateExpression(assignStmt)
            if (!expressionResult.interpretedCorrectly) return expressionResult

            val newValue =
                getNewValueFromResult(expressionResult)
                    ?: return createNoValueError()

            assignValueIfCompatible(identifier, existingVariable, newValue)
        } catch (e: Exception) {
            createGenericError(e)
        }
    }

    private fun getVariable(identifier: String): Variable? = variables[identifier]

    private fun evaluateExpression(statement: AssignmentStatement): InterpreterResult = defaultExpressionExecutor.execute(statement.getValue())

    private fun getNewValueFromResult(result: InterpreterResult): Variable? = result.interpreter

    private fun assignValueIfCompatible(
        id: String,
        oldVar: Variable,
        newVal: Variable,
    ): InterpreterResult {
        if (!areTypesCompatible(oldVar.getType(), newVal.getType())) {
            return createTypeMismatchError(id, oldVar.getType(), newVal.getType())
        }

        variables[id] = Variable(oldVar.getType(), newVal.getValue())
        return createSuccessResult()
    }

    // --- Funciones auxiliares para crear resultados ---

    private fun createVariableNotFoundError(id: String) = InterpreterResult(false, "Error: Variable '$id' not declared", null)

    private fun createNoValueError() = InterpreterResult(false, "Error: No value to assign", null)

    private fun createTypeMismatchError(
        id: String,
        oldType: Any,
        newType: Any,
    ) = InterpreterResult(
        false,
        "Error: Type mismatch. Cannot assign type '$newType' to variable '$id' of type '$oldType'",
        null,
    )

    private fun createSuccessResult() = InterpreterResult(true, "Assignment executed successfully", null)

    private fun createGenericError(e: Exception) = InterpreterResult(false, "Error executing assignment statement: ${e.message}", null)
}
