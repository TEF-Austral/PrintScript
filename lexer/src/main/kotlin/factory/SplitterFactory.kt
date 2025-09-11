package factory

import stringSplitter.Splitter

interface SplitterFactory {
    fun createDefaultsSplitter(): Splitter

    fun createCustomSplitter(specialChars: List<Char>): Splitter
}
