package factory

import DefaultInterpreter
import data.DefaultDataBase
import emitter.PrintEmitter
import executor.coercer.StringToBooleanConverter
import executor.coercer.StringToNumberConverter
import executor.coercer.StringToStringConverter
import executor.coercer.TypeCoercer
import executor.expression.BinaryExpressionExecutor
import executor.expression.IdentifierExpressionExecutor
import executor.expression.LiteralExpressionExecutor
import executor.expression.ReadEnvExpressionExecutor
import executor.expression.ReadInputExpressionExecutor
import executor.expression.SpecificExpressionExecutor
import executor.operators.Divide
import executor.operators.LogicalAnd
import executor.operators.LogicalOr
import executor.operators.Multiplication
import executor.operators.Operator
import executor.operators.Subtraction
import executor.operators.Sum

object InterpreterFactoryVersionOnePointOne {

    private val dataBase: DefaultDataBase = DefaultDataBase()

    val operators: List<Operator> =
        listOf(Sum, Divide, Multiplication, Subtraction, LogicalOr, LogicalAnd)

    private val identifierAndLiteralExecutors: List<SpecificExpressionExecutor> =
        listOf(
            IdentifierExpressionExecutor(),
            LiteralExpressionExecutor(),
        )

    private val allSpecificExpressionExecutors: List<SpecificExpressionExecutor> =
        listOf(
            BinaryExpressionExecutor(expressions = identifierAndLiteralExecutors),
            IdentifierExpressionExecutor(),
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

    fun createDefaultInterpreter(): DefaultInterpreter =
        InterpreterBuilder.build(dataBase, coercers, allSpecificExpressionExecutors)
}
