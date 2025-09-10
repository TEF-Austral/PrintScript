package parser.result

import node.Program

sealed interface FinalResult : ParserResult {
    fun getProgram(): Program
}
