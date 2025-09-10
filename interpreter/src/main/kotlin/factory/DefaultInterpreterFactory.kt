package factory

import DefaultInterpreter
import data.DefaultDataBase
import emitter.PrintEmitter
import executor.coercer.StringToBooleanConverter
import executor.coercer.StringToNumberConverter
import executor.coercer.StringToStringConverter
import executor.coercer.TypeCoercer
import executor.expression.BinaryExpressionExecutor
import executor.expression.DefaultExpressionExecutor
import executor.expression.IdentifierExpressionExecutor
import executor.expression.LiteralExpressionExecutor
import executor.expression.ReadEnvExpressionExecutor
import executor.expression.ReadInputExpressionExecutor
import executor.expression.SpecificExpressionExecutor
import executor.operators.LogicalAnd
import executor.operators.LogicalOr
import executor.operators.Multiplication
import executor.operators.Subtraction
import executor.statement.AssignmentStatementExecutor
import executor.statement.ConstDeclarationStatementExecutor
import executor.statement.DeclarationStatementExecutor
import executor.statement.DefaultStatementExecutor
import executor.statement.ExpressionStatementExecutor
import executor.statement.IfStatementExecutor
import executor.statement.LetDeclarationStatement
import executor.statement.PrintStatementExecutor
import executor.statement.SpecificStatementExecutor
import executor.operators.Operator
import executor.operators.Sum
import executor.operators.Divide

object DefaultInterpreterFactory {
    private val dataBase: DefaultDataBase = DefaultDataBase()
    val operators: List<Operator> =
        listOf(Sum, Divide, Multiplication, Subtraction, LogicalOr, LogicalAnd)

    private val identifierAndLiteralExecutors: List<SpecificExpressionExecutor> =
        listOf(
            IdentifierExpressionExecutor(dataBase),
            LiteralExpressionExecutor(),
        )

    private val allSpecificExpressionExecutors: List<SpecificExpressionExecutor> =
        listOf(
            BinaryExpressionExecutor(expressions = identifierAndLiteralExecutors),
            IdentifierExpressionExecutor(dataBase),
            LiteralExpressionExecutor(),
            ReadInputExpressionExecutor(PrintEmitter()),
            ReadEnvExpressionExecutor(),
        )

    private val coercers: TypeCoercer =
        TypeCoercer(
            listOf(
                StringToNumberConverter(),
                StringToBooleanConverter(),
                StringToStringConverter(),
            ),
        )

    fun createDefaultInterpreter(): DefaultInterpreter {
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

    fun createCustomInterpreter(
        specificExpressionExecutors: List<SpecificExpressionExecutor>,
        specificStatementExecutor: List<SpecificStatementExecutor>,
    ): DefaultInterpreter =
        DefaultInterpreter(
            DefaultExpressionExecutor(specificExpressionExecutors),
            DefaultStatementExecutor(specificStatementExecutor),
        )
}
