package factory.result

import Analyzer
import Interpreter
import Lexer
import formatter.Formatter
import parser.ParserInterface
import type.Version

class DefaultFactoryResult(
    lexerFactory: Lexer,
) : FactoryResult {
    override fun getVersion(): Version {
        TODO("Not yet implemented")
    }

    override fun getLexer(): Lexer {
        TODO("Not yet implemented")
    }

    override fun getParser(): ParserInterface {
        TODO("Not yet implemented")
    }

    override fun getInterpreter(): Interpreter {
        TODO("Not yet implemented")
    }

    override fun getFormatter(): Formatter {
        TODO("Not yet implemented")
    }

    override fun getAnalyzer(): Analyzer {
        TODO("Not yet implemented")
    }
}
