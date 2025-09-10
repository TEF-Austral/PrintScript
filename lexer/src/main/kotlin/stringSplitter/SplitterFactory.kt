package stringSplitter

object SplitterFactory {
    fun createSplitter(): Splitter {
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
}
