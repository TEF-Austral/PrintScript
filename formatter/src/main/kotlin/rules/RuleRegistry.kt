package formatter.rules

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
}
