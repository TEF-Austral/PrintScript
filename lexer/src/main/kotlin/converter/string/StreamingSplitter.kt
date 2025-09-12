package converter.string

import coordinates.Position
import stringSplitter.extractor.Extraction
import stringSplitter.extractor.TokenExtractor
import java.io.Reader
import kotlin.text.iterator
import result.StreamSplitterResult

class StreamingSplitter(
    private val reader: Reader,
    private val extractors: List<TokenExtractor>,
    private val buffer: String = "",
    private val line: Int = 1,
    private val column: Int = 1,
    private val isStreamFinished: Boolean = false,
) {

    fun next(): StreamSplitterResult? {
        extractFromBuffer()?.let { return it }
        if (isStreamFinished) return null
        return readMoreData().next()
    }

    private fun extractFromBuffer(): StreamSplitterResult? {
        if (buffer.isEmpty()) return null
        for (extractor in extractors) {
            return when (val extraction = extractor.extract(buffer, 0)) {
                is Extraction.Token -> handleToken(extraction)
                is Extraction.Skip -> handleSkip(extraction)
                is Extraction.NoMatch -> continue
            }
        }
        return null
    }

    private fun handleToken(extraction: Extraction.Token): StreamSplitterResult {
        val coordinates = Position(line, column)
        val nextState = consumeFromBuffer(extraction.value)
        return StreamSplitterResult(extraction.value, coordinates, nextState)
    }

    private fun handleSkip(extraction: Extraction.Skip): StreamSplitterResult? {
        val nextState = consumeFromBuffer(extraction.text)
        return nextState.next()
    }

    private fun readMoreData(): StreamingSplitter {
        val charBuffer = CharArray(2048)
        val charsRead = reader.read(charBuffer)
        return if (charsRead != -1) {
            val newBuffer = buffer + String(charBuffer, 0, charsRead)
            StreamingSplitter(reader, extractors, newBuffer, line, column, false)
        } else {
            StreamingSplitter(reader, extractors, buffer, line, column, true)
        }
    }

    private fun consumeFromBuffer(text: String): StreamingSplitter {
        var newLine = line
        var newColumn = column
        for (char in text) {
            if (char == '\n') {
                newLine++
                newColumn = 1
            } else {
                newColumn++
            }
        }
        val newBuffer = buffer.substring(text.length)
        return StreamingSplitter(
            reader,
            extractors,
            newBuffer,
            newLine,
            newColumn,
            isStreamFinished,
        )
    }
}
