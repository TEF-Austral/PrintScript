package string.splitter.extractor

/**
 * Un extractor inteligente que siempre captura la coincidencia más larga para un
 * delimitador, incluyendo cualquier espacio en blanco adyacente.
 * No requiere configuración.
 */
class SmartDelimiterExtractor(
    private val specialChars: List<Char>,
) : TokenExtractor {
    private val twoCharOps = setOf("!=", "<=", ">=", "==", "++", "--", "&&", "||")

    override fun extract(
        input: String,
        index: Int,
    ): Extraction {
        val originalIndex = index
        var cursor = index

        // 1. Analiza los espacios en blanco iniciales (opcionales)
        while (cursor < input.length && input[cursor].isWhitespace()) {
            cursor++
        }

        // 2. Busca un delimitador obligatorio después de los espacios
        if (cursor >= input.length || !specialChars.contains(input[cursor])) {
            // Si solo encontramos espacios, no es un delimitador.
            // Se lo dejamos al WhitespaceExtractor.
            return Extraction.NoMatch
        }

        // 3. Captura el delimitador (1 o 2 caracteres)
        val delimiterStart = cursor
        val c = input[delimiterStart]
        val delimiter =
            if (delimiterStart + 1 < input.length) {
                val pair = "$c${input[delimiterStart + 1]}"
                if (pair in twoCharOps) pair else c.toString()
            } else {
                c.toString()
            }
        cursor += delimiter.length

        // 4. Analiza los espacios en blanco finales (opcionales)
        while (cursor < input.length && input[cursor].isWhitespace()) {
            cursor++
        }

        // 5. Devuelve siempre el token más largo encontrado
        return Extraction.Token(input.substring(originalIndex, cursor))
    }
}
