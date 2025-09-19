package scanner

import node.AssignmentStatement
import node.DeclarationStatement
import node.ExpressionStatement
import node.IfStatement
import node.PrintStatement
import node.Program
import node.Statement
import scanner.ExpressionChecks
import rules.ProgramSummary

class StatementScanner {
    var hasIdentifier: Boolean = false
        private set
    var hasPrintStatement: Boolean = false
        private set
    var hasReadInput: Boolean = false
        private set

    fun scan(program: Program): ProgramSummary {
        for (s in program.getStatements()) {
            if (hasIdentifier && hasPrintStatement && hasReadInput) break
            scanStatement(s)
        }
        return ProgramSummary(hasIdentifier, hasPrintStatement, hasReadInput)
    }

    private fun scanStatement(stmt: Statement) {
        when (stmt) {
            is DeclarationStatement -> {
                hasIdentifier = true
                stmt.getInitialValue()?.let { expr ->
                    if (ExpressionChecks.containsIdentifier(expr)) hasIdentifier = true
                    if (ExpressionChecks.containsReadInput(expr)) hasReadInput = true
                }
            }
            is AssignmentStatement -> {
                hasIdentifier = true
                if (ExpressionChecks.containsReadInput(stmt.getValue())) hasReadInput = true
                if (ExpressionChecks.containsIdentifier(stmt.getValue())) hasIdentifier = true
            }
            is PrintStatement -> {
                hasPrintStatement = true
                val expr = stmt.getExpression()
                if (ExpressionChecks.containsIdentifier(expr)) hasIdentifier = true
                if (ExpressionChecks.containsReadInput(expr)) hasReadInput = true
            }
            is ExpressionStatement -> {
                val expr = stmt.getExpression()
                if (ExpressionChecks.containsIdentifier(expr)) hasIdentifier = true
                if (ExpressionChecks.containsReadInput(expr)) hasReadInput = true
            }
            is IfStatement -> {
                val cond = stmt.getCondition()
                if (ExpressionChecks.containsIdentifier(cond)) hasIdentifier = true
                if (ExpressionChecks.containsReadInput(cond)) hasReadInput = true
                scanStatement(stmt.getConsequence())
                stmt.getAlternative()?.let { scanStatement(it) }
            }
            else -> {}
        }
    }
}
