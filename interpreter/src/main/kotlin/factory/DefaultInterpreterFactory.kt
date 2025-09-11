package factory

import DefaultInterpreter
import Interpreter
import data.DataBase
import emitter.Emitter
import type.Version

class DefaultInterpreterFactory : InterpreterFactory {

    override fun createWithVersion(version: Version): Interpreter =
        when (version) {
            Version.VERSION_1_0 -> InterpreterFactoryVersionOne.createDefaultInterpreter()
            Version.VERSION_1_1 -> InterpreterFactoryVersionOnePointOne.createDefaultInterpreter()
        }

    fun createWithVersionAndEmitter(
        version: Version,
        emitter: Emitter,
    ): Interpreter =
        when (version) {
            Version.VERSION_1_0 -> InterpreterFactoryVersionOne.createDefaultInterpreter(emitter)
            Version.VERSION_1_1 ->
                InterpreterFactoryVersionOnePointOne.createDefaultInterpreter(
                    emitter,
                )
        }

    override fun createCustomInterpreter(
        specificExpressionExecutors: List<executor.expression.SpecificExpressionExecutor>,
        specificStatementExecutor: List<executor.statement.SpecificStatementExecutor>,
        emitter: Emitter,
        database: DataBase,
    ): DefaultInterpreter =
        DefaultInterpreter(
            database = database,
            executor.expression.DefaultExpressionExecutor(specificExpressionExecutors),
            executor.statement.DefaultStatementExecutor(specificStatementExecutor),
        )
}
