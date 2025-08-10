package lexer

class SplitterFactory {

    fun createSplitter(): Splitter {
        return StringSplitter(listOf('(', ')', '{', '}', '=', '<', '>', ';', ','))
    }
}