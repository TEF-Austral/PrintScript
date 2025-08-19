package splitter

import Coordinates
import Position
import splitter.extractor.CommentExtractor
import splitter.extractor.Extraction
import splitter.extractor.NumberExtractor
import splitter.extractor.SpecialTokenExtractor
import splitter.extractor.StringLiteralExtractor
import splitter.extractor.TokenExtractor
import splitter.extractor.WhitespaceExtractor
import splitter.extractor.WordExtractor

class Splitter(specialChars: List<Char>) {
  private val extractors: List<TokenExtractor> = listOf(
    WhitespaceExtractor,
    CommentExtractor,
    StringLiteralExtractor,
    NumberExtractor(),
    SpecialTokenExtractor(specialChars),
    WordExtractor(specialChars)
  )

  fun split(input: String): List<Pair<String, Coordinates>> {
    val result = mutableListOf<Pair<String, Coordinates>>()
    var line = 1
    var column = 1
    var i = 0

    while (i < input.length) {
      for (ext in extractors) {
        when (val extraction = ext.extract(input, i)) {
          is Extraction.NoMatch -> continue
          is Extraction.Skip -> {
            extraction.text.forEach { ch ->
              when (ch) {
                '\n' -> { line++; column = 1 }
                '\t' -> column += 4
                else -> column++
              }
            }
            i += extraction.text.length
          }
          is Extraction.Token -> {
            result += extraction.value to Position(line, column)
            column += extraction.value.length
            i += extraction.value.length
          }
        }
        break
      }
    }
    return result
  }
}
