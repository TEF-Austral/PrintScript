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
    // Larger buffer to reduce I/O operations
    private val bufferSize = 65536 // 64KB read buffer

    fun next(): StreamSplitterResult? {
        // Try to extract from current buffer
        val extraction = tryExtraction()
        if (extraction != null) {
            return extraction
        }

        // If stream is finished and buffer is not empty, we have incomplete data
        if (isStreamFinished) {
            return if (buffer.isNotEmpty()) {
                // Force process remaining as error or skip
                handleRemainingBuffer()
            } else {
                null
            }
        }

        // Need more data - read complete logical units
        val newSplitter = readCompleteLogicalUnits()
        return newSplitter.next()
    }

    private fun tryExtraction(): StreamSplitterResult? {
        if (buffer.isEmpty()) return null

        // Only attempt extraction if we have a complete logical unit or stream is done
        val shouldAttemptExtraction = isStreamFinished || hasCompleteLogicalUnit()

        if (!shouldAttemptExtraction) {
            // Don't have complete logical unit yet, need to read more
            return null
        }

        // Try each extractor
        for (extractor in extractors) {
            val extraction = extractor.extract(buffer, 0)
            when (extraction) {
                is Extraction.Token -> {
                    return createTokenResult(extraction.value)
                }
                is Extraction.Skip -> {
                    // Create new splitter with skip consumed and continue
                    val newSplitter = consumeFromBuffer(extraction.text)
                    return newSplitter.next()
                }
                is Extraction.NoMatch -> continue
            }
        }

        // No extractor matched - skip one character
        return skipSingleCharacter()
    }

    private fun hasCompleteLogicalUnit(): Boolean {
        if (buffer.isEmpty()) return false

        val semicolonIndex = buffer.indexOf(';')
        val newlineIndex = buffer.indexOf('\n')

        // We have a complete logical unit if we find either a semicolon or newline
        return semicolonIndex >= 0 || newlineIndex >= 0
    }

    private fun createTokenResult(tokenValue: String): StreamSplitterResult {
        val coordinates = Position(line, column)
        val nextSplitter = consumeFromBuffer(tokenValue)
        return StreamSplitterResult(tokenValue, coordinates, nextSplitter)
    }

    private fun skipSingleCharacter(): StreamSplitterResult? {
        if (buffer.isEmpty()) return null
        val charToSkip = buffer.substring(0, 1)
        val newSplitter = consumeFromBuffer(charToSkip)
        return newSplitter.next()
    }

    private fun handleRemainingBuffer(): StreamSplitterResult? {
        if (buffer.isEmpty()) return null

        // Try extractors one more time on remaining buffer
        for (extractor in extractors) {
            val extraction = extractor.extract(buffer, 0)
            when (extraction) {
                is Extraction.Token -> {
                    return createTokenResult(extraction.value)
                }
                is Extraction.Skip -> {
                    val newSplitter = consumeFromBuffer(extraction.text)
                    return newSplitter.next()
                }
                is Extraction.NoMatch -> continue
            }
        }

        // If still no match, skip the remaining buffer character by character
        return skipSingleCharacter()
    }

    private fun readCompleteLogicalUnits(): StreamingSplitter {
        if (isStreamFinished) {
            return this
        }

        var currentBuffer = buffer
        var finished = false

        // Keep reading until we have at least one complete logical unit or stream ends
        while (!finished) {
            val charBuffer = CharArray(bufferSize)
            val charsRead =
                try {
                    reader.read(charBuffer)
                } catch (e: Exception) {
                    -1 // Treat exceptions as end of stream
                }

            if (charsRead > 0) {
                currentBuffer += String(charBuffer, 0, charsRead)

                // Check if we now have at least one complete logical unit
                val semicolonIndex = currentBuffer.indexOf(';')
                val newlineIndex = currentBuffer.indexOf('\n')

                if (semicolonIndex >= 0 || newlineIndex >= 0) {
                    // We have at least one complete logical unit, we can proceed
                    break
                }
            } else {
                finished = true
            }
        }

        return StreamingSplitter(
            reader,
            extractors,
            currentBuffer,
            line,
            column,
            finished,
        )
    }

    private fun consumeFromBuffer(text: String): StreamingSplitter {
        require(text.length <= buffer.length) {
            "Cannot consume more text than available in buffer: requested ${text.length}, available ${buffer.length}"
        }

        var newLine = line
        var newColumn = column

        // Update position based on consumed text
        for (char in text) {
            when (char) {
                '\n' -> {
                    newLine++
                    newColumn = 1
                }
                '\r' -> {
                    // Handle carriage return (don't update column)
                }
                else -> {
                    newColumn++
                }
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
