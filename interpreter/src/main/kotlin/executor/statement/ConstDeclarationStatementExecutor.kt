package executor.statement

import data.DataBase
import executor.expression.DefaultExpressionExecutor
import node.DeclarationStatement
import node.Statement
import result.InterpreterResult
import type.CommonTypes
import utils.areTypesCompatible
import variable.Variable

class ConstDeclarationStatementExecutor(
    private val dataBase: DataBase,
    private val defaultExpressionExecutor: DefaultExpressionExecutor,
) : SpecificStatementExecutor {
    override fun canHandle(statement: Statement): Boolean = statement is DeclarationStatement && statement.getDeclarationType() == CommonTypes.CONST

    override fun execute(statement: Statement): InterpreterResult {
        if (statement !is DeclarationStatement) {
            return InterpreterResult(false, "Error: statement is not of declaration type", null)
        }

        val identifier = statement.getIdentifier()

        // Check if the constant has already been declared.
        if (dataBase.isConstant(identifier) || dataBase.getVariables().containsKey(identifier)) {
            return InterpreterResult(false, "Constant '$identifier' has already been declared", null)
        }

        val initialValue =
            statement.getInitialValue()
                ?: return InterpreterResult(false, "Constant '$identifier' must be initialized with a value", null)

        val expressionResult = defaultExpressionExecutor.execute(initialValue)

        if (!expressionResult.interpretedCorrectly) {
            return expressionResult
        }

        val value = expressionResult.interpreter
        if (value?.getValue() == null) {
            return InterpreterResult(false, "Constant '$identifier' cannot be initialized to null", null)
        }

        val expectedType = statement.getDataType()
        val actualType = value.getType()

        if (!areTypesCompatible(expectedType, actualType)) {
            return InterpreterResult(
                false,
                "Type error: expected '$expectedType' but got '$actualType' for constant '$identifier'",
                null,
            )
        }

        val constant = Variable(actualType, value.getValue())
        dataBase.addConstant(identifier, constant)

        return InterpreterResult(true, "Constant '$identifier' was successfully declared", constant)
    }
}
