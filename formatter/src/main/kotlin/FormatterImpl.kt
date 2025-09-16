package formatter

import Token
import TokenStream
import formatter.config.FormatConfig
import formatter.config.FormatterConstants
import type.CommonTypes
import java.io.Writer

class FormatterImpl : Formatter {

    override fun formatToString(
        src: TokenStream,
        config: FormatConfig,
    ): String {
        val builder = StringBuilder()
        formatTokens(src, config, builder)
        return builder.toString().trimEnd()
    }

    override fun formatToWriter(
        src: TokenStream,
        config: FormatConfig,
        writer: Writer,
    ) {
        val formatted = formatToString(src, config)
        writer.write(formatted)
        writer.flush()
    }

    private fun formatTokens(
        stream: TokenStream,
        config: FormatConfig,
        builder: StringBuilder,
    ) {
        var currentStream = stream
        var indentLevel = 0
        var previousToken: Token? = null
        var expectingIfBrace = false
        var isPrintlnStatement = false
        var newLineAdded = true
        val skipNextIndentation = false

        while (!currentStream.isAtEnd()) {
            val result = currentStream.next() ?: break
            val token = result.token
            currentStream = result.nextStream

            if (token.getType() == CommonTypes.EMPTY) {
                continue
            }

            if (newLineAdded &&
                !token
                    .getValue()
                    .trimStart()
                    .trimEnd()
                    .matches(Regex("}"))
            ) {
                addIndentation(builder, indentLevel, config)
                newLineAdded = false
            }

            when {
                token.getType() == CommonTypes.CONDITIONALS &&
                    token.getValue().trimStart().trimEnd() == "if" -> {
                    handleTokenSpacing(token, previousToken, builder, config)
                    builder.append(token.getValue())
                    expectingIfBrace = true
                }

                token.getValue().trimStart().trimEnd() == "{" -> {
                    if (expectingIfBrace) {
                        handleIfBrace(builder, config, indentLevel)
                        expectingIfBrace = false
                        builder.append("{")
                        indentLevel++
                        builder.append("\n")
                        newLineAdded = true
                    } else {
                        handleTokenSpacing(token, previousToken, builder, config)
                        builder.append("{")
                        indentLevel++
                        builder.append("\n")
                        newLineAdded = true
                    }
                }

                token.getValue().trimStart().trimEnd() == "}" -> {
                    indentLevel--

                    if (newLineAdded) {
                        addIndentation(builder, indentLevel, config)
                        newLineAdded = false
                    }
                    builder.append("}")

                    // Add newline after closing brace if not at end
                    if (!currentStream.isAtEnd()) {
                        builder.append("\n")
                        newLineAdded = true
                    }
                }

                token.getValue().trimStart().trimEnd() == ";" -> {
                    builder.append(";")
                    builder.append("\n")
                    newLineAdded = true

                    if (isPrintlnStatement) {
                        val blankLines =
                            config.blankLinesAfterPrintln.coerceIn(
                                FormatterConstants.MIN_BLANK_LINES_AFTER_PRINTLN,
                                FormatterConstants.MAX_BLANK_LINES_AFTER_PRINTLN,
                            )
                        repeat(blankLines) {
                            builder.append("\n")
                        }
                        isPrintlnStatement = false
                    }
                }

                token.getType() == CommonTypes.PRINT && token.getValue().contains("println") -> {
                    handleTokenSpacing(token, previousToken, builder, config)
                    builder.append(token.getValue())
                    isPrintlnStatement = true
                }

                token.getValue().trimStart().trimEnd() == ":" -> {
                    // Handle space before colon
                    if (config.enforceSingleSpace == true) {
                        // Always add single space before colon when enforcing
                        if (builder.isNotEmpty() && !builder.last().isWhitespace()) {
                            builder.append(" ")
                        }
                    } else {
                        when (config.spaceBeforeColon) {
                            true -> {
                                if (builder.isNotEmpty() && !builder.last().isWhitespace()) {
                                    builder.append(" ")
                                }
                            }
                            false -> {
                                while (builder.isNotEmpty() && builder.last() == ' ') {
                                    builder.deleteCharAt(builder.length - 1)
                                }
                            }
                            null -> {}
                        }
                    }

                    builder.append(token.getValue())

                    // Handle space after colon
                    if (config.enforceSingleSpace == true) {
                        // Always add single space after colon when enforcing
                        builder.append(" ")
                    } else {
                        when (config.spaceAfterColon) {
                            true -> builder.append(" ")
                            false -> {}
                            null -> {}
                        }
                    }
                }

                token.getType() == CommonTypes.ASSIGNMENT -> {
                    // Handle space before =
                    if (config.enforceSingleSpace == true) {
                        if (builder.isNotEmpty() && !builder.last().isWhitespace()) {
                            builder.append(" ")
                        }
                    } else {
                        when (config.spaceAroundAssignment) {
                            true -> {
                                if (builder.isNotEmpty() && !builder.last().isWhitespace()) {
                                    builder.append(" ")
                                }
                            }
                            false -> {
                                while (builder.isNotEmpty() && builder.last() == ' ') {
                                    builder.deleteCharAt(builder.length - 1)
                                }
                            }
                            null -> {}
                        }
                    }

                    builder.append(token.getValue())

                    // Handle space after =
                    if (config.enforceSingleSpace == true) {
                        builder.append(" ")
                    } else {
                        when (config.spaceAroundAssignment) {
                            true -> builder.append(" ")
                            false -> {}
                            null -> {}
                        }
                    }
                }

                token.getType() == CommonTypes.OPERATORS -> {
                    when (config.spaceAroundOperators) {
                        true -> {
                            if (builder.isNotEmpty() && !builder.last().isWhitespace()) {
                                builder.append(" ")
                            }
                        }
                        false -> {
                            while (builder.isNotEmpty() && builder.last() == ' ') {
                                builder.deleteCharAt(builder.length - 1)
                            }
                        }
                        null -> {}
                    }

                    builder.append(token.getValue())

                    when (config.spaceAroundOperators) {
                        true -> builder.append(" ")
                        false -> {}
                        null -> {}
                    }
                }

                token.getValue().trimStart().trimEnd() == "(" -> {
                    if (config.enforceSingleSpace == true) {
                        // Add space before ( except after function names
                        if (previousToken?.getType() != CommonTypes.PRINT &&
                            previousToken != null &&
                            !newLineAdded
                        ) {
                            builder.append(" ")
                        }
                    } else {
                        handleTokenSpacing(token, previousToken, builder, config)
                    }
                    builder.append("(")

                    // Add space after ( when enforcing single space
                    if (config.enforceSingleSpace == true) {
                        builder.append(" ")
                    }
                }

                token.getValue().trimStart().trimEnd() == ")" -> {
                    // Add space before ) when enforcing single space
                    if (config.enforceSingleSpace == true) {
                        if (builder.isNotEmpty() &&
                            !builder.last().isWhitespace() &&
                            builder.last() != '('
                        ) {
                            builder.append(" ")
                        }
                    }
                    builder.append(")")
                }

                token.getType() == CommonTypes.STRING_LITERAL -> {
                    if (config.enforceSingleSpace != true) {
                        if (previousToken?.getValue()?.replace(" ", "") != ":" &&
                            previousToken?.getType() != CommonTypes.ASSIGNMENT &&
                            previousToken?.getType() != CommonTypes.OPERATORS
                        ) {
                            handleTokenSpacing(token, previousToken, builder, config)
                        }
                    }
                    val value = token.getValue()
                    builder.append("\"$value\"")
                }

                token.getType() == CommonTypes.NUMBER_LITERAL -> {
                    if (config.enforceSingleSpace != true) {
                        if (previousToken?.getValue()?.replace(" ", "") != ":" &&
                            previousToken?.getType() != CommonTypes.ASSIGNMENT &&
                            previousToken?.getType() != CommonTypes.OPERATORS
                        ) {
                            handleTokenSpacing(token, previousToken, builder, config)
                        }
                    }
                    builder.append(token.getValue())
                }

                token.getType() == CommonTypes.BOOLEAN_LITERAL -> {
                    if (config.enforceSingleSpace != true) {
                        if (previousToken?.getValue()?.replace(" ", "") != ":" &&
                            previousToken?.getType() != CommonTypes.ASSIGNMENT &&
                            previousToken?.getType() != CommonTypes.OPERATORS
                        ) {
                            handleTokenSpacing(token, previousToken, builder, config)
                        }
                    }
                    builder.append(token.getValue())
                }

                else -> {
                    if (config.enforceSingleSpace != true) {
                        if (previousToken?.getValue()?.replace(" ", "") != ":" &&
                            previousToken?.getType() != CommonTypes.ASSIGNMENT &&
                            previousToken?.getType() != CommonTypes.OPERATORS
                        ) {
                            handleTokenSpacing(token, previousToken, builder, config)
                        }
                    }
                    builder.append(token.getValue())
                }
            }

            previousToken = token
        }

        val result = builder.toString()
        val lines = result.lines()
        builder.clear()

        for ((index, line) in lines.withIndex()) {
            val trimmedLine = line.trimStart()
            val indentSpaces = line.length - trimmedLine.length

            // Only apply multi-space regex if not enforcing single space
            val processedLine =
                if (config.enforceSingleSpace != true) {
                    trimmedLine.replace(FormatterConstants.MULTI_SPACE_REGEX, " ")
                } else {
                    trimmedLine
                }

            builder.append(" ".repeat(indentSpaces))
            builder.append(processedLine)

            if (index < lines.size - 1) {
                builder.append("\n")
            }
        }

        while (builder.isNotEmpty() &&
            builder.last() == '\n' &&
            builder.length > 1 &&
            builder[builder.length - 2] == '\n'
        ) {
            builder.deleteCharAt(builder.length - 1)
        }
    }

    private fun handleTokenSpacing(
        currentToken: Token,
        previousToken: Token?,
        builder: StringBuilder,
        config: FormatConfig,
    ) {
        // When enforcing single space, always add space between tokens
        if (config.enforceSingleSpace == true) {
            if (previousToken != null && builder.isNotEmpty() && !builder.last().isWhitespace()) {
                builder.append(" ")
            }
        } else {
            // Original spacing logic
            addSpaceBetweenTokens(currentToken, previousToken, builder)
        }
    }

    private fun handleIfBrace(
        builder: StringBuilder,
        config: FormatConfig,
        indentLevel: Int,
    ) {
        when (config.ifBraceOnSameLine) {
            true -> builder.append(" ")
            false -> {
                builder.append("\n")
            }
            null -> {
                if (builder.isNotEmpty() && !builder.last().isWhitespace()) {
                    builder.append(" ")
                }
            }
        }
    }

    private fun addSpaceBetweenTokens(
        currentToken: Token,
        previousToken: Token?,
        builder: StringBuilder,
    ) {
        if (previousToken == null) return

        val skipSpace =
            when {
                previousToken.getValue().trimStart().trimEnd() == "(" -> true
                currentToken.getValue().trimStart().trimEnd() == ")" -> true
                currentToken.getValue().trimStart().trimEnd() == ";" -> true
                currentToken.getValue().trimStart().trimEnd() == ":" -> true
                currentToken.getType() == CommonTypes.ASSIGNMENT -> true
                previousToken.getType() == CommonTypes.PRINT &&
                    currentToken.getValue().trimStart().trimEnd() == "(" -> true
                else -> false
            }

        if (skipSpace) return

        if (builder.isNotEmpty() && !builder.last().isWhitespace()) {
            builder.append(" ")
        }
    }

    private fun addIndentation(
        builder: StringBuilder,
        level: Int,
        config: FormatConfig,
    ) {
        val spacesPerLevel = config.indentSize
        repeat(level * spacesPerLevel) {
            builder.append(" ")
        }
    }
}
