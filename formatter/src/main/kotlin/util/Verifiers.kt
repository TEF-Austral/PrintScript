package formatter.util

import Token
import type.CommonTypes

fun isDelimiterColon(token: Token): Boolean =
    token.getValue().contains(":") && token.getType() == CommonTypes.DELIMITERS

fun isDelimiterSemicolon(token: Token): Boolean =
    token.getValue().contains(";") && token.getType() == CommonTypes.DELIMITERS

fun isAssignment(token: Token): Boolean =
    token.getValue().contains("=") && token.getType() == CommonTypes.ASSIGNMENT

fun isOperator(token: Token): Boolean = token.getType() == CommonTypes.OPERATORS

fun isPrintln(token: Token): Boolean =
    token.getType() == CommonTypes.PRINT &&
        token.getValue().contains("println")

fun isDelimiterParentheses(token: Token): Boolean =
    token.getType() == CommonTypes.DELIMITERS &&
        (token.getValue().contains("(") || token.getValue().contains(")"))

fun isDelimiterOpenBrace(token: Token): Boolean =
    token.getType() == CommonTypes.DELIMITERS && token.getValue().contains("{")

fun isConditionIf(token: Token): Boolean =
    (
        token.getType() == CommonTypes.CONDITIONALS &&
            token.getValue().trim() == "if"
    )

fun isDelimiterCloseBrace(token: Token): Boolean =
    token.getType() == CommonTypes.DELIMITERS && token.getValue().contains("}")
