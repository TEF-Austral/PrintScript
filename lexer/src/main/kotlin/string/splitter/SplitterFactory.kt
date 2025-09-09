package string.splitter

object SplitterFactory {
    fun createSplitter(): Splitter {
        val specials = listOf('(', ')', '{', '}', '=', '<', '>', ';', ',', ':', '+', '-', '*', '/', '%', '&', '|', '!')
        return Splitter(specials)
    }
}
