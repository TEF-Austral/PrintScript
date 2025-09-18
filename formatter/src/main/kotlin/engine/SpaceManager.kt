import formatter.config.FormatConfig
import formatter.util.isAssignment
import formatter.util.isDelimiterColon
import type.CommonTypes

class SpaceManager(
    private val config: FormatConfig,
) {

    fun getSpaceBetween(
        prevToken: Token?,
        currentToken: Token,
        nextToken: Token?,
    ): String {
        if (prevToken == null) return ""

        val prevValue = prevToken.getValue().trim()
        val currValue = currentToken.getValue().trim()

        // Special cases that should NEVER have space

        // Function calls: no space between function name and (
        if (prevToken.getType() == CommonTypes.IDENTIFIER && currValue == "(") {
            return ""
        }
        if (prevToken.getType() == CommonTypes.PRINT && currValue == "(") {
            return ""
        }

        // No space after opening parenthesis
        if (prevValue == "(") return ""

        // No space before closing parenthesis or semicolon
        if (currValue == ")" || currValue == ";") return ""

        // Handle colon spacing (already handled by ColonRule)
        if (isDelimiterColon(prevToken)) {
            return "" // ColonRule handles its own spacing
        }

        // Handle assignment spacing
        if (isAssignment(currentToken)) {
            return when (config.spaceAroundAssignment) {
                true -> " "
                false -> ""
                null -> " " // Default: space before assignment
            }
        }

        // Assignment already handled its trailing space
        if (isAssignment(prevToken)) {
            return ""
        }

        // Operators already handle their trailing space
        if (prevToken.getType() == CommonTypes.OPERATORS) {
            return ""
        }

        // For enforceSingleSpace configuration
        if (config.enforceSingleSpace == true) {
            return " "
        }

        // Default: add space between most other tokens
        return " "
    }
}
