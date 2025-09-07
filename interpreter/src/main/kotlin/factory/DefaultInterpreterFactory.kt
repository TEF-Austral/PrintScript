package factory

import DefaultInterpreter
import data.DefaultDataBase
import executor.expression.BinaryExpressionExecutor
import executor.expression.DefaultExpressionExecutor
import executor.expression.IdentifierExpressionExecutor
import executor.expression.LiteralExpressionExecutor
import executor.expression.SpecificExpressionExecutor
import executor.operators.LogicalAnd
import executor.operators.LogicalOr
import executor.operators.Multiplication
import executor.operators.Subtraction
import executor.statement.AssignmentStatementExecutor
import executor.statement.DeclarationStatementExecutor
import executor.statement.DefaultStatementExecutor
import executor.statement.ExpressionStatementExecutor
import executor.statement.IfStatementExecutor
import executor.statement.PrintStatementExecutor
import executor.statement.SpecificStatementExecutor
import executor.operators.Operator
import executor.operators.Sum
import executor.operators.Divide

object DefaultInterpreterFactory {
    val dataBase: DefaultDataBase = DefaultDataBase()

    val operators: List<Operator> = listOf(Sum, Divide, Multiplication, Subtraction, LogicalOr, LogicalAnd)

    val listForBinaryExpressionExecutor: List<SpecificExpressionExecutor> =
        listOf(
            IdentifierExpressionExecutor(dataBase),
            LiteralExpressionExecutor(),
        )

    val specificExpressionExecutors: List<SpecificExpressionExecutor> =
        listOf(
            BinaryExpressionExecutor(expressions = listForBinaryExpressionExecutor),
            IdentifierExpressionExecutor(dataBase),
            LiteralExpressionExecutor(),
        )

    val basicSpecificStatementExecutor: List<SpecificStatementExecutor> =
        listOf(
            DeclarationStatementExecutor(dataBase, DefaultExpressionExecutor(specificExpressionExecutors)),
            AssignmentStatementExecutor(dataBase, DefaultExpressionExecutor(specificExpressionExecutors)),
            ExpressionStatementExecutor(DefaultExpressionExecutor(specificExpressionExecutors)),
            PrintStatementExecutor(DefaultExpressionExecutor(specificExpressionExecutors)),
        )

    val completeSpecificStatementExecutor: List<SpecificStatementExecutor> =
        basicSpecificStatementExecutor +
            listOf(
                IfStatementExecutor(
                    DefaultExpressionExecutor(specificExpressionExecutors),
                    DefaultStatementExecutor(basicSpecificStatementExecutor),
                ),
            )

    fun createDefaultInterpreter(): DefaultInterpreter {
        val expressionExecutor = DefaultExpressionExecutor(specificExpressionExecutors)

        // --- El Truco para las Sentencias ---

        // 1. Crea una lista mutable que estará vacía al principio.
        val completeStatementExecutors = mutableListOf<SpecificStatementExecutor>()

        // 2. Crea la instancia ÚNICA y FINAL del director de orquesta.
        //    En este momento, su lista de especialistas está vacía, pero eso cambiará.
        val statementExecutor = DefaultStatementExecutor(completeStatementExecutors)

        // 3. Ahora, crea CADA especialista pasándole el director de orquesta principal.
        val declarationExecutor = DeclarationStatementExecutor(dataBase, expressionExecutor)
        val assignmentExecutor = AssignmentStatementExecutor(dataBase, expressionExecutor)
        val expressionStatementExecutor = ExpressionStatementExecutor(expressionExecutor)
        val printExecutor = PrintStatementExecutor(expressionExecutor)

        // El especialista 'if' también recibe al director principal. ¡Esta es la clave!
        val ifExecutor = IfStatementExecutor(expressionExecutor, statementExecutor)

        completeStatementExecutors.add(declarationExecutor)
        completeStatementExecutors.add(assignmentExecutor)
        completeStatementExecutors.add(expressionStatementExecutor)
        completeStatementExecutors.add(printExecutor)
        completeStatementExecutors.add(ifExecutor) // ¡Importante incluirlo!

        // 5. Crea el intérprete final con los componentes ya interconectados correctamente.
        return DefaultInterpreter(expressionExecutor, statementExecutor)
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
