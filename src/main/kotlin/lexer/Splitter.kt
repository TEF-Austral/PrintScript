package lexer

import kotlin.String
import kotlin.collections.mutableListOf
import token.Coordinates
import token.Position

interface Splitter {
    fun split(input: String): List<Pair<String, Coordinates>>
}

class StringSplitter(val specialChars: List<Char>) : Splitter {

    override fun split(input: String): List<Pair<String, Coordinates>> {
        val tokens = mutableListOf<Pair<String, Coordinates>>()
        var line = 1
        var column = 1
        var i = 0

        while (i < input.length) {
            val currentChar = input[i]
            val startLine = line
            val startColumn = column

            when {
                currentChar.isWhitespace() -> {
                    when (currentChar) {
                        ' ' -> column++
                        '\n' -> {
                            line++
                            column = 1
                        }

                        '\t' -> column += 4
                    }
                    i++
                    continue // Skip to the next character
                }

                currentChar == '"' || currentChar == '\'' -> {
                    val stringLiteral = extractStringLiteral(input, i)
                    tokens.add(Pair(stringLiteral, Position(startLine, startColumn)))
                    column += stringLiteral.length
                    i += stringLiteral.length
                }

                isSpecialChar(currentChar) -> {
                    val specialToken = extractSpecialToken(input, i)
                    tokens.add(Pair(specialToken, Position(startLine, startColumn)))
                    column += specialToken.length
                    i += specialToken.length
                }

                else -> {
                    val word = extractWord(input, i)
                    tokens.add(Pair(word, Position(startLine, startColumn)))
                    column += word.length
                    i += word.length
                }
            }
        }

        return tokens
    }

    private fun isSpecialChar(char: Char): Boolean {
        return specialChars.contains(char)
    }

    private fun extractSpecialToken(input: String, startIndex: Int): String {
        val currentChar = input[startIndex]

        if (startIndex + 1 < input.length) {
            val nextChar = input[startIndex + 1]
            val twoCharToken = "$currentChar$nextChar"

            when (twoCharToken) {
                "!=", "<=", ">=", "==", "++", "--", "&&", "||" -> {
                    return twoCharToken
                }
            }
        }
        return currentChar.toString()
    }

    private fun extractWord(input: String, startIndex: Int): String {
        var endIndex = startIndex
        while (endIndex < input.length && !input[endIndex].isWhitespace() && !isSpecialChar(input[endIndex])) {
            endIndex++
        }
        return input.substring(startIndex, endIndex)
    }

    private fun extractStringLiteral(input: String, startIndex: Int): String {
        val quoteChar = input[startIndex]
        var endIndex = startIndex + 1
        while (endIndex < input.length && input[endIndex] != quoteChar) {
            endIndex++
        }
        if (endIndex < input.length) {
            endIndex++ // Include the closing quote
        }
        return input.substring(startIndex, endIndex)
    }
}