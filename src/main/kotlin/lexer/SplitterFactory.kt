package lexer

object SplitterFactory {

    fun createSplitter(): Splitter {
        return StringSplitter(listOf('(', ')', '{', '}', '=', '<', '>', ';', ',',':','+','-','*','/','%','&','|','!'))
    }
}