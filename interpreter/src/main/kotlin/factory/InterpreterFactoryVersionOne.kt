package factory

import DefaultInterpreter
import data.DefaultDataBase
import executor.coercer.StringToBooleanConverter
import executor.coercer.StringToNumberConverter
import executor.coercer.StringToStringConverter
import executor.coercer.TypeCoercer
import executor.expression.BinaryExpressionExecutor
import executor.expression.IdentifierExpressionExecutor
import executor.expression.LiteralExpressionExecutor
import executor.expression.SpecificExpressionExecutor
import executor.operators.Divide
import executor.operators.Multiplication
import executor.operators.Operator
import executor.operators.Subtraction
import executor.operators.Sum

object InterpreterFactoryVersionOne {

    private val dataBase: DefaultDataBase = DefaultDataBase()

    val operators: List<Operator> =
        listOf(Sum, Divide, Multiplication, Subtraction)

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
