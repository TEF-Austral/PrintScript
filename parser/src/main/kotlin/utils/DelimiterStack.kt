package parser.utils

import Token

class DelimiterStack(
    private val stack: List<Token> = emptyList(),
) {
    private val openingDelimiters = setOf("(", "{", "[")

    private val closingDelimiters = setOf(")", "}", "]")

    private val delimiterPairs =
        mapOf(
            ")" to "(",
            "}" to "{",
            "]" to "[",
        )

    fun add(token: Token): DelimiterStack =
        when {
            isOpeningDelimiter(token.getValue()) -> DelimiterStack(stack + token)
            isClosingDelimiter(token.getValue()) -> {
                if (stack.isEmpty()) {
                    DelimiterStack(stack + token)
                } else {
                    val lastOpening = stack.last()
                    val expectedOpening = delimiterPairs[token.getValue()]
                    if (lastOpening.getValue() == expectedOpening) {
                        DelimiterStack(stack.dropLast(1))
                    } else {
                        DelimiterStack(stack + token)
                    }
                }
            }
            else -> this
        }

    fun isValidDelimiterState(): Boolean = stack.isEmpty()

    fun getValidationError(): String? =
        when {
            stack.isEmpty() -> null
            else -> {
                val unmatchedTokens =
                    stack.joinToString(", ") {
                        "${it.getValue()} at ${it.getCoordinates().getRow()}:${it.getCoordinates().getColumn()}"
                    }
                "Unmatched delimiters: $unmatchedTokens"
            }
        }

    private fun isOpeningDelimiter(value: String): Boolean = value in openingDelimiters

    private fun isClosingDelimiter(value: String): Boolean = value in closingDelimiters
}
