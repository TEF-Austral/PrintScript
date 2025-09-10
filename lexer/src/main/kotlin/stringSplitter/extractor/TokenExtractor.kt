package stringSplitter.extractor

interface TokenExtractor {
    fun extract(
        input: String,
        index: Int,
    ): Extraction
}
