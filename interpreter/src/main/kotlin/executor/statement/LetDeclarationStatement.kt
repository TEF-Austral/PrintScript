package executor.statement

import data.DataBase
import executor.coercer.ITypeCoercer
import executor.expression.DefaultExpressionExecutor
import node.CoercibleExpression
import node.DeclarationStatement
import node.Statement
import result.InterpreterResult
import type.CommonTypes
import utils.areTypesCompatible
import variable.Variable

class LetDeclarationStatement(
    private val dataBase: DataBase,
    private val defaultExpressionExecutor: DefaultExpressionExecutor,
    private val typeCoercer: ITypeCoercer,
) : SpecificStatementExecutor {
    override fun canHandle(statement: Statement): Boolean = statement is DeclarationStatement && statement.getDeclarationType() == CommonTypes.LET

    override fun execute(statement: Statement): InterpreterResult {
        val declarationStatement = statement as DeclarationStatement
        val identifier = declarationStatement.getIdentifier()
        val declaredType = declarationStatement.getDataType()
        val initialValueExpression = declarationStatement.getInitialValue()

        val finalVariable: Variable =
            if (initialValueExpression != null) {
                // 1. Ejecutar la expresión de la derecha
                val expressionResult = defaultExpressionExecutor.execute(initialValueExpression)
                if (!expressionResult.interpretedCorrectly) return expressionResult

                val initialValue = expressionResult.interpreter ?: return InterpreterResult(false, "Expression yielded no value", null)

                // 2. Determinar si se necesita coerción o compatibilidad de tipos
                if (initialValueExpression is CoercibleExpression) {
                    val rawValue = initialValue.getValue().toString()
                    val coercionResult = typeCoercer.coerce(rawValue, declaredType)
                    if (!coercionResult.interpretedCorrectly) return coercionResult
                    coercionResult.interpreter!!
                } else {
                    // Lógica estándar para el resto de expresiones
                    if (!areTypesCompatible(declaredType, initialValue.getType())) {
                        return InterpreterResult(false, "Type mismatch: Expected '$declaredType', got '${initialValue.getType()}'", null)
                    }
                    Variable(declaredType, initialValue.getValue())
                }
            } else {
                Variable(declaredType, null)
            }

        dataBase.addVariable(identifier, finalVariable)
        return InterpreterResult(true, "Declaration successful", finalVariable)
    }
}
