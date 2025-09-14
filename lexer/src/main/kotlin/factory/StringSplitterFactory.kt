package factory

import converter.string.StreamingSplitter
import java.io.Reader
import stringSplitter.Splitter

object StringSplitterFactory : SplitterFactory {

    override fun createDefaultsSplitter(): Splitter {
        val specials = SpecialCharsFactory.createSpecialChars()
        return Splitter(specials)
    }

    override fun createStreamingSplitter(reader: Reader): StreamingSplitter {
        val specialChars = SpecialCharsFactory.createSpecialChars()
        val tokenExtractor = TokenExtractorFactory.createTokenExtractor(specialChars)
        return StreamingSplitter(reader, tokenExtractor)
    }

    override fun createCustomSplitter(specialChars: List<Char>): Splitter = Splitter(specialChars)
}
