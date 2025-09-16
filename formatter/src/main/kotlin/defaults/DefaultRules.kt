package formatter.defaults

import formatter.config.FormatterConstants
import formatter.engine.Rule
import formatter.rules.SpaceUtil
import type.CommonTypes

object DefaultRules {

    fun build(): List<Rule> =
        listOf(
            Rule(
                name = "If",
                applies = { t, _ ->
                    t.getType() == CommonTypes.CONDITIONALS && t.getValue().trim() == "if"
                },
                apply = { t, ctx ->
                    ctx.ensureSpaceBetween(t)
                    ctx.out.append(t.getValue())
                    ctx.expectingIfBrace = true
                },
            ),
            Rule(
                name = "OpenBrace",
                applies = { t, _ -> t.getValue().trim() == "{" },
                apply = { t, ctx ->
                    if (ctx.expectingIfBrace) {
                        ctx.handleIfBrace()
                        ctx.expectingIfBrace = false
                    } else {
                        ctx.ensureSpaceBetween(t)
                    }
                    ctx.out.append('{')
                    ctx.indentLevel++
                    ctx.out.append('\n')
                    ctx.newLineAdded = true
                },
            ),
            Rule(
                name = "CloseBrace",
                applies = { t, _ -> t.getValue().trim() == "}" },
                apply = { _, ctx ->
                    ctx.indentLevel--
                    if (ctx.newLineAdded) {
                        ctx.addIndentation()
                        ctx.newLineAdded = false
                    }
                    ctx.out.append('}')
                    if (!ctx.isEndAfterThisToken()) {
                        ctx.out.append('\n')
                        ctx.newLineAdded = true
                    }
                },
            ),
            Rule(
                name = "Semicolon",
                applies = { t, _ -> t.getValue().trim() == ";" },
                apply = { _, ctx ->
                    if (ctx.config.enforceSingleSpace == true) {
                        ctx.out.trimTrailingSpaces()
                    }
                    ctx.out.append(';')
                    ctx.out.append('\n')
                    ctx.newLineAdded = true

                    if (ctx.isPrintlnStatement) {
                        val blankLines =
                            ctx.config.blankLinesAfterPrintln.coerceIn(
                                FormatterConstants.MIN_BLANK_LINES_AFTER_PRINTLN,
                                FormatterConstants.MAX_BLANK_LINES_AFTER_PRINTLN,
                            )
                        repeat(blankLines) { ctx.out.append('\n') }
                        ctx.isPrintlnStatement = false
                    }
                },
            ),
            Rule(
                name = "Println",
                applies = { t, _ ->
                    t.getType() == CommonTypes.PRINT && t.getValue().contains("println")
                },
                apply = { t, ctx ->
                    ctx.ensureSpaceBetween(t)
                    ctx.out.append(t.getValue())
                    ctx.isPrintlnStatement = true
                },
            ),
            Rule(
                name = "Colon",
                applies = { t, _ -> t.getValue().trim() == ":" },
                apply = { t, ctx ->
                    if (ctx.config.enforceSingleSpace == true) {
                        ctx.out.trimTrailingSpaces()
                        ctx.out.appendSpaceIfNeeded()
                        ctx.out.append(':')
                        ctx.out.append(' ')
                    } else {
                        when (ctx.config.spaceBeforeColon) {
                            true -> ctx.out.appendSpaceIfNeeded()
                            false -> ctx.out.trimTrailingSpaces()
                            null -> {}
                        }
                        ctx.out.append(t.getValue())
                        when (ctx.config.spaceAfterColon) {
                            true -> ctx.out.append(' ')
                            false, null -> {}
                        }
                    }
                },
            ),
            Rule(
                name = "Assignment",
                applies = { t, _ -> t.getType() == CommonTypes.ASSIGNMENT },
                apply = { t, ctx ->
                    ctx.out.trimTrailingSpaces()
                    val (spaceBefore, spaceAfter) =
                        when {
                            ctx.config.enforceSingleSpace == true -> true to true
                            ctx.config.spaceAroundAssignment != null -> {
                                val s = ctx.config.spaceAroundAssignment
                                s to s
                            }

                            else -> null to null
                        }
                    val rebuilt =
                        SpaceUtil.rebuild(
                            raw = t.getValue(),
                            symbol = "=",
                            spaceBefore = spaceBefore,
                            spaceAfter = spaceAfter,
                        )
                    ctx.out.append(rebuilt)
                },
            ),
            Rule(
                name = "Operator",
                applies = { t, _ -> t.getType() == CommonTypes.OPERATORS },
                apply = { t, ctx ->
                    if (ctx.config.enforceSingleSpace == true) {
                        ctx.out.trimTrailingSpaces()
                        ctx.out.appendSpaceIfNeeded()
                        ctx.out.append(t.getValue())
                        ctx.out.append(' ')
                    } else {
                        when (ctx.config.spaceAroundOperators) {
                            true -> ctx.out.appendSpaceIfNeeded()
                            false -> ctx.out.trimTrailingSpaces()
                            null -> {}
                        }
                        ctx.out.append(t.getValue())
                        when (ctx.config.spaceAroundOperators) {
                            true -> ctx.out.append(' ')
                            false, null -> {}
                        }
                    }
                },
            ),
            Rule(
                name = "LeftParen",
                applies = { t, _ -> t.getValue().trim() == "(" },
                apply = { t, ctx ->
                    ctx.ensureSpaceBetween(t)
                    ctx.out.append('(')
                },
            ),
            Rule(
                name = "RightParen",
                applies = { t, _ -> t.getValue().trim() == ")" },
                apply = { _, ctx ->
                    if (ctx.config.enforceSingleSpace == true) {
                        ctx.out.appendSpaceIfNeeded()
                    }
                    ctx.out.append(')')
                },
            ),
            Rule(
                name = "StringLiteral",
                applies = { t, _ -> t.getType() == CommonTypes.STRING_LITERAL },
                apply = { t, ctx ->
                    val prev = ctx.previousToken
                    appendWithSpacing(
                        prevValue = prev?.getValue(),
                        prevType = prev?.getType(),
                        ensureSpace = { ctx.ensureSpaceBetween(t) },
                        append = {
                            val value = t.getValue()
                            ctx.out.append("\"$value\"")
                        },
                    )
                },
            ),
            Rule(
                name = "NumberLiteral",
                applies = { t, _ -> t.getType() == CommonTypes.NUMBER_LITERAL },
                apply = { t, ctx ->
                    val prev = ctx.previousToken
                    appendWithSpacing(
                        prevValue = prev?.getValue(),
                        prevType = prev?.getType(),
                        ensureSpace = { ctx.ensureSpaceBetween(t) },
                        append = { ctx.out.append(t.getValue()) },
                    )
                },
            ),
            Rule(
                name = "BooleanLiteral",
                applies = { t, _ -> t.getType() == CommonTypes.BOOLEAN_LITERAL },
                apply = { t, ctx ->
                    val prev = ctx.previousToken
                    appendWithSpacing(
                        prevValue = prev?.getValue(),
                        prevType = prev?.getType(),
                        ensureSpace = { ctx.ensureSpaceBetween(t) },
                        append = { ctx.out.append(t.getValue()) },
                    )
                },
            ),
            Rule(
                name = "Fallback",
                applies = { _, _ -> true },
                apply = { t, ctx ->
                    val prev = ctx.previousToken
                    appendWithSpacing(
                        prevValue = prev?.getValue(),
                        prevType = prev?.getType(),
                        ensureSpace = { ctx.ensureSpaceBetween(t) },
                        append = { ctx.out.append(t.getValue()) },
                    )
                },
            ),
        )

    private fun needsLeadingSpace(
        prevValue: String?,
        prevType: CommonTypes?,
    ): Boolean =
        prevValue?.replace(" ", "") != ":" &&
            prevType != CommonTypes.ASSIGNMENT &&
            prevType != CommonTypes.OPERATORS

    private inline fun appendWithSpacing(
        prevValue: String?,
        prevType: CommonTypes?,
        ensureSpace: () -> Unit,
        append: () -> Unit,
    ) {
        if (needsLeadingSpace(prevValue, prevType)) {
            ensureSpace()
        }
        append()
    }

    private fun StringBuilder.trimTrailingSpaces() {
        while (isNotEmpty() && last() == ' ') {
            deleteCharAt(lastIndex)
        }
    }

    private fun StringBuilder.appendSpaceIfNeeded() {
        if (isNotEmpty() && !last().isWhitespace()) {
            append(' ')
        }
    }
}
