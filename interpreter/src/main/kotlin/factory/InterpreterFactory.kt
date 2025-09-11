package factory

import DefaultInterpreter
import Interpreter
import data.DataBase
import emitter.Emitter
import executor.expression.SpecificExpressionExecutor
import executor.statement.SpecificStatementExecutor
import type.Version

interface InterpreterFactory {

    fun createWithVersion(version: Version): Interpreter

    fun createCustomInterpreter(
        specificExpressionExecutors: List<SpecificExpressionExecutor>,
        specificStatementExecutor: List<SpecificStatementExecutor>,
        emitter: Emitter,
        database: DataBase,
    ): DefaultInterpreter
}
