package factory

import DefaultInterpreter
import Interpreter
import type.Version

class DefaultInterpreterFactory : InterpreterFactory {

    override fun createWithVersion(version: Version): Interpreter =
        when (version) {
            Version.VERSION_1_0 -> InterpreterFactoryVersionOne.createDefaultInterpreter()
            Version.VERSION_1_1 -> InterpreterFactoryVersionOnePointOne.createDefaultInterpreter()
        }

    override fun createCustomInterpreter(
        specificExpressionExecutors: List<executor.expression.SpecificExpressionExecutor>,
        specificStatementExecutor: List<executor.statement.SpecificStatementExecutor>,
        emitter: emitter.Emitter,
    ): DefaultInterpreter =
        DefaultInterpreter(
            executor.expression.DefaultExpressionExecutor(specificExpressionExecutors),
            executor.statement.DefaultStatementExecutor(specificStatementExecutor),
        )
}
