package formatter.rules

import formatter.config.FormatConfig
import node.ASTNode
import kotlin.collections.plus
import kotlin.text.matches

object RuleRegistry {
    private val baseRules =
        listOf(
            AssignmentRule(),
            ExpressionStatementRule(),
            PrintStatementRule(),
            EmptyStatementRule(),
            BinaryExpressionRule(),
            StringLiteralExpressionRule(),
            LiteralExpressionRule(),
            IdentifierExpressionRule(),
        )

    val rulesV10: List<FormatRule> by lazy {
        val list = mutableListOf<FormatRule>()
        list += DeclarationRule(setOf("let"), list)
        list += baseRules
        list
    }

    private val rulesV11Body =
        listOf(
            IfStatementRule(),
            ReadInputExpressionRule(),
            ReadEnvExpressionRule(),
        )

    val rulesV11: List<FormatRule> by lazy {
        val list = mutableListOf<FormatRule>()
        list += DeclarationRule(setOf("let", "const"), list)
        list += baseRules
        list += rulesV11Body
        list
    }

    // helper: return active rules according to the provided config
    fun getActive(
        rules: List<FormatRule>,
        config: FormatConfig,
    ): List<FormatRule> {
        val defaults =
            setOf(
                RuleId.Declaration,
                RuleId.Assignment,
                RuleId.ExpressionStatement,
                RuleId.PrintStatement,
                RuleId.EmptyStatement,
                RuleId.BinaryExpression,
                RuleId.StringLiteralExpression,
                RuleId.LiteralExpression,
                RuleId.IdentifierExpression,
                RuleId.IfStatement,
                RuleId.ReadInputExpression,
                RuleId.ReadEnvExpression,
            )
        return if (config.enabledRules.isEmpty()) {
            rules
        } else {
            rules.filter { it.id in defaults || it.id in config.enabledRules }
        }
    }

    // helper: pick first matching rule from the active rules (for nested node formatting)
    fun firstMatching(
        node: ASTNode,
        config: FormatConfig,
        rules: List<FormatRule>,
    ): FormatRule {
        val active = getActive(rules, config)
        return active.first { it.matches(node) }
    }
}
