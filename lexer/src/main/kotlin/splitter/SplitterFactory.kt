package splitter

object SplitterFactory {

    fun createSplitter(): Splitter {
        return StringSplitter(listOf('(', ')', '{', '}', '=', '<', '>', ';', ',',':','+','-','*','/','%','&','|','!'))
    }
}