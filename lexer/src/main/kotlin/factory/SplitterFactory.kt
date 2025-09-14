package factory

import converter.string.StreamingSplitter
import java.io.Reader
import stringSplitter.Splitter

interface SplitterFactory {
    fun createDefaultsSplitter(): Splitter

    fun createStreamingSplitter(reader: Reader): StreamingSplitter

    fun createCustomSplitter(specialChars: List<Char>): Splitter
}
