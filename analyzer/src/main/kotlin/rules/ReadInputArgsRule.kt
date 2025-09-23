package rules

import config.AnalyzerConfig
import diagnostic.Diagnostic
import node.Program

class ReadInputArgsRule : Rule {
    override fun canHandle(config: AnalyzerConfig): Boolean = config.restrictReadInputArgs

    private val traverser = ReadInputTraverser(ArgValidator())

    override fun apply(program: Program): List<Diagnostic> =
        program.getStatements().flatMap { traverser.traverseStatement(it) }
}
