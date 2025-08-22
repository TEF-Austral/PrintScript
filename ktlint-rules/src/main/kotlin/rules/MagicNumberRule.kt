package rules

import com.pinterest.ktlint.rule.engine.core.api.ElementType
import com.pinterest.ktlint.rule.engine.core.api.Rule
import com.pinterest.ktlint.rule.engine.core.api.RuleId
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.lexer.KtTokens

class MagicNumberRule :
    Rule(
        ruleId = RuleId("custom-rules:no-magic-numbers"),
        about =
            About(
                maintainer = "TEF Team",
            ),
    ) {
    private val allowedNumbers = setOf("0")

    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit,
    ) {
        if (node.elementType == ElementType.FLOAT_LITERAL ||
            node.elementType == ElementType.INTEGER_LITERAL
        ) {
            val value = node.text

            if (isNumericLiteral(value) && !isAllowedNumber(value)) {
                // Skip if it's in a const declaration
                if (!isInConstDeclaration(node)) {
                    emit(
                        node.startOffset,
                        "Magic number '$value' should be extracted to a named constant",
                        false,
                    )
                }
            }
        }
    }

    private fun isNumericLiteral(value: String): Boolean = value.matches(Regex("^-?\\d+(\\.\\d+)?[fFdDlL]?$"))

    private fun isAllowedNumber(value: String): Boolean {
        val cleanValue = value.replace(Regex("[fFdDlL]$"), "") // Remove type suffixes
        return allowedNumbers.contains(cleanValue)
    }

    private fun isInConstDeclaration(node: ASTNode): Boolean {
        var parent = node.treeParent
        while (parent != null) {
            if (parent.elementType == ElementType.PROPERTY) {
                // Check if property has const modifier
                val modifierList = parent.findChildByType(ElementType.MODIFIER_LIST)
                if (modifierList?.findChildByType(KtTokens.CONST_KEYWORD) != null) {
                    return true
                }
            }
            parent = parent.treeParent
        }
        return false
    }
}
