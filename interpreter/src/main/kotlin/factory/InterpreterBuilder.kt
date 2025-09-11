package factory

import DefaultInterpreter
import data.DefaultDataBase
import emitter.PrintEmitter
import executor.coercer.TypeCoercer
import executor.expression.DefaultExpressionExecutor
import executor.expression.SpecificExpressionExecutor
import executor.statement.AssignmentStatementExecutor
import executor.statement.ConstDeclarationStatementExecutor
import executor.statement.DeclarationStatementExecutor
import executor.statement.DefaultStatementExecutor
import executor.statement.ExpressionStatementExecutor
import executor.statement.IfStatementExecutor
import executor.statement.LetDeclarationStatement
import executor.statement.PrintStatementExecutor
import executor.statement.SpecificStatementExecutor

internal object InterpreterBuilder {
    fun build(
        dataBase: DefaultDataBase,
        coercers: TypeCoercer,
        allSpecificExpressionExecutors: List<SpecificExpressionExecutor>,
    ): DefaultInterpreter {
        dataBase.clear()

        val expressionExecutor = DefaultExpressionExecutor(allSpecificExpressionExecutors)

        val statementSpecialists = mutableListOf<SpecificStatementExecutor>()
        val mainStatementExecutor = DefaultStatementExecutor(statementSpecialists)

        val constDeclaration =
            ConstDeclarationStatementExecutor(dataBase, expressionExecutor, coercers)
        val letDeclaration = LetDeclarationStatement(dataBase, expressionExecutor, coercers)

        val declarationExecutor =
            DeclarationStatementExecutor(
                dataBase,
                expressionExecutor,
                listOf(constDeclaration, letDeclaration),
            )

        val assignmentExecutor = AssignmentStatementExecutor(dataBase, expressionExecutor)
        val printExecutor = PrintStatementExecutor(expressionExecutor, PrintEmitter())
        val expressionStatementExecutor = ExpressionStatementExecutor(expressionExecutor)
        val ifExecutor = IfStatementExecutor(expressionExecutor, mainStatementExecutor)

        statementSpecialists.add(declarationExecutor)
        statementSpecialists.add(assignmentExecutor)
        statementSpecialists.add(printExecutor)
        statementSpecialists.add(ifExecutor)
        statementSpecialists.add(expressionStatementExecutor)

        return DefaultInterpreter(expressionExecutor, mainStatementExecutor)
    }
}
