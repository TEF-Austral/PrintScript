package parser.utils

import Token
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
