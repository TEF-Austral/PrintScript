package factory

import Interpreter
import executor.expression.BinaryExpressionExecutor
import executor.expression.DefaultExpressionExecutor
import executor.expression.IdentifierExpressionExecutor
import executor.expression.LiteralExpressionExecutor
import executor.expression.SpecificExpressionExecutor
import executor.statement.AssignmentStatementExecutor
import executor.statement.DeclarationStatementExecutor
import executor.statement.DefaultStatementExecutor
import executor.statement.ExpressionStatementExecutor
import executor.statement.PrintStatementExecutor
import executor.statement.SpecificStatementExecutor
import variable.Variable

object DefaultInterpreterFactory {
    val mutableMap: MutableMap<String, Variable> = mutableMapOf()

    val listForBinaryExpressionExecutor: List<SpecificExpressionExecutor> =
        listOf(
            IdentifierExpressionExecutor(mutableMap),
            LiteralExpressionExecutor(),
        )

    val specificExpressionExecutors: List<SpecificExpressionExecutor> =
        listOf(
            BinaryExpressionExecutor(expressions = listForBinaryExpressionExecutor),
            IdentifierExpressionExecutor(mutableMap),
            LiteralExpressionExecutor(),
        )

    val specificStatementExecutor: List<SpecificStatementExecutor> =
        listOf(
            DeclarationStatementExecutor(mutableMap, DefaultExpressionExecutor(specificExpressionExecutors)),
            AssignmentStatementExecutor(mutableMap, DefaultExpressionExecutor(specificExpressionExecutors)),
            ExpressionStatementExecutor(DefaultExpressionExecutor(specificExpressionExecutors)),
            PrintStatementExecutor(DefaultExpressionExecutor(specificExpressionExecutors)),
        )

    fun createDefaultInterpreter(): Interpreter =
        Interpreter(
            DefaultExpressionExecutor(specificExpressionExecutors),
            DefaultStatementExecutor(specificStatementExecutor),
        )

    fun createCustomInterpreter(
        specificExpressionExecutors: List<SpecificExpressionExecutor>,
        specificStatementExecutor: List<SpecificStatementExecutor>,
    ): Interpreter =
        Interpreter(
            DefaultExpressionExecutor(specificExpressionExecutors),
            DefaultStatementExecutor(specificStatementExecutor),
        )
}
