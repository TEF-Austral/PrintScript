package lexer

import kotlin.String
import kotlin.collections.mutableListOf
import kotlin.text.iterator

interface Splitter{
    fun split(input: String): List<String>
}

object StringSplitter: Splitter{

    override fun split(input: String): List<String> {
        val strings = mutableListOf<String>()
        var word = ""

        for (char in input) {
            when (char) {
                '(', ')', '{', '}', '=', '<', '>', ';', ',', ' ', '\t', '\n' -> {
                    if (word.isNotEmpty()) {
                        strings.add(word)
                        word = ""
                    }
                    if (char != ' ' && char != '\t' && char != '\n') {
                        strings.add(char.toString())
                    }
                }
                else -> {
                    word += char
                }
            }
        }
        if (word.isNotEmpty()) {
            strings.add(word)
        }
        return strings
    }
}



