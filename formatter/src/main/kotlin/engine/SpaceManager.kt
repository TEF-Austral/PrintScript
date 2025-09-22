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

        if (prevToken.getType() == CommonTypes.IDENTIFIER && currValue == "(") {
            return ""
        }
        if (prevToken.getType() == CommonTypes.PRINT && currValue == "(") {
            return ""
        }

        if (prevValue == "(") return if (config.enforceSingleSpace == true) " " else ""

        if (currValue == ")" || currValue == ";") return ""

        if (isDelimiterColon(prevToken)) {
            return ""
        }

        if (isAssignment(currentToken)) {
            return when (config.spaceAroundAssignment) {
                true -> " "
                false -> ""
                null -> " "
            }
        }

        if (isAssignment(prevToken)) {
            return ""
        }

        if (prevToken.getType() == CommonTypes.OPERATORS) {
            return ""
        }

        if (config.enforceSingleSpace == true) {
            return " "
        }
        return " "
    }
}
