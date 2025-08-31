package formatter.rules

object RuleRegistry {
    val rules: List<FormatRule> =
        listOf(
            DeclarationRule(),
            AssignmentRule(),
            ExpressionStatementRule(),
            PrintStatementRule(),
            EmptyStatementRule(),
            BinaryExpressionRule(),
            LiteralExpressionRule(),
            IdentifierExpressionRule(),
        )
}
