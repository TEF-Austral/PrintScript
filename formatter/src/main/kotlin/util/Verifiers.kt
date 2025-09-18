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
