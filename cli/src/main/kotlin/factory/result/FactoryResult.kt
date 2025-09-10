package factory.result

import Analyzer
import Interpreter
import Lexer
import formatter.Formatter
import parser.ParserInterface
import type.Version

interface FactoryResult {
    fun getVersion(): Version

    fun getLexer(): Lexer

    fun getParser(): ParserInterface

    fun getInterpreter(): Interpreter

    fun getFormatter(): Formatter

    fun getAnalyzer(): Analyzer
}
