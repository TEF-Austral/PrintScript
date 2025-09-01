package executor.statement

import executor.expression.DefaultExpressionExecutor
import result.InterpreterResult
import node.AssignmentStatement
import node.Statement
import utils.areTypesCompatible
import variable.Variable

class AssignmentStatementExecutor(
    private val variables: MutableMap<String, Variable>,
    private val defaultExpressionExecutor: DefaultExpressionExecutor,
) : SpecificStatementExecutor {
    override fun canHandle(statement: Statement): Boolean = statement is AssignmentStatement

    override fun execute(statement: Statement): InterpreterResult {
        return try {
            val assignmentStatement = statement as AssignmentStatement
            val identifier = assignmentStatement.getIdentifier()

            val existingVariable =
                variables[identifier]
                    ?: return InterpreterResult(false, "Error: Variable '$identifier' not declared", null)

            val expressionResult = defaultExpressionExecutor.execute(assignmentStatement.getValue())
            if (!expressionResult.interpretedCorrectly) {
                return expressionResult
            }

            val newValue =
                expressionResult.interpreter ?: return InterpreterResult(
                    false,
                    "Error: No value to assign",
                    null,
                )

            if (!areTypesCompatible(existingVariable.getType(), newValue.getType())) {
                return InterpreterResult(
                    false,
                    "Error: Type mismatch. Cannot assign type '${newValue.getType()}' to variable '$identifier' of type '${existingVariable.getType()}'",
                    null,
                )
            }

            variables[identifier] = Variable(existingVariable.getType(), newValue.getValue())

            InterpreterResult(true, "Assignment executed successfully", null)
        } catch (e: Exception) {
            InterpreterResult(false, "Error executing assignment statement: ${e.message}", null)
        }
    }
}
