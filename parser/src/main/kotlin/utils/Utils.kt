package parser.utils

import Token
import parser.Parser
import parser.result.ParserResult
import type.CommonTypes

fun checkType(
    type: CommonTypes,
    current: Token?,
): Boolean {
    if (current == null) return false
    return current.getType() == type
}

fun isValidResultAndCurrentToken(result: ParserResult): Boolean = (result.isSuccess()) && result.getParser().peak() != null

fun verifyCurrentToken(
    types: List<CommonTypes>,
    parser: Parser,
): Boolean {
    for (type in types) {
        if (checkType(type, parser.peak())) {
            return true
        }
    }
    return false
}
fun isSemiColon(token: Token?): Boolean {
    if (token == null) return false
    return token.getType() == CommonTypes.DELIMITERS && token.getValue() == ";"
}

fun isOpeningParenthesis(parser: Parser): Boolean {
    val token = parser.peak()
    if (token == null) return false
    return token.getType() == CommonTypes.DELIMITERS && token.getValue() == "("
}

fun isClosingParenthesis(parser: Parser): Boolean {
    val token = parser.peak()
    if (token == null) return false
    return token.getType() == CommonTypes.DELIMITERS && token.getValue() == ")"
}
