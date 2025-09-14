package factory

import stringSplitter.extractor.CommentExtractor
import stringSplitter.extractor.NumberExtractor
import stringSplitter.extractor.SpecialTokenExtractor
import stringSplitter.extractor.StringLiteralExtractor
import stringSplitter.extractor.TokenExtractor
import stringSplitter.extractor.WhitespaceExtractor
import stringSplitter.extractor.WordExtractor

object TokenExtractorFactory {
    fun createTokenExtractor(specialChars: List<Char>): List<TokenExtractor> =
        listOf(
            WhitespaceExtractor,
            CommentExtractor,
            StringLiteralExtractor,
            NumberExtractor(),
            SpecialTokenExtractor(specialChars),
            WordExtractor(specialChars),
        )
}
