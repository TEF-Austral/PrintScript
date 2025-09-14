package factory

import converter.string.StreamingSplitter
import java.io.Reader
import stringSplitter.Splitter

interface SplitterFactory {
    fun createDefaultsSplitter(): Splitter

    fun createStreamingSplitter(
        reader: Reader,
        size: Int = 65536,
    ): StreamingSplitter

    fun createCustomSplitter(specialChars: List<Char>): Splitter
}
