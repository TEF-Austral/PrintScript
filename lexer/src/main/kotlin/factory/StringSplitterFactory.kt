package factory

import stringSplitter.Splitter

object StringSplitterFactory : SplitterFactory {
    override fun createDefaultsSplitter(): Splitter {
        val specials =
            listOf(
                '(',
                ')',
                '{',
                '}',
                '=',
                '<',
                '>',
                ';',
                ',',
                ':',
                '+',
                '-',
                '*',
                '/',
                '%',
                '&',
                '|',
                '!',
            )
        return Splitter(specials)
    }

    override fun createCustomSplitter(specialChars: List<Char>): Splitter = Splitter(specialChars)
}
