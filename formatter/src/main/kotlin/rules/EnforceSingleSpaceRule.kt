//package formatter.rules
//
//import TokenStream
//import formatter.config.FormatConfig
//import type.CommonTypes
//
///**
// * Regla no configurable que asegura que haya como máximo un espacio entre tokens.
// * Se encarga de normalizar cualquier secuencia de espacios y/o tabulaciones
// * en un único espacio.
// */
//class SingleSpaceSeparatorRule : FormatRule {
//
//    /**
//     * La regla se activa si el siguiente token es de tipo WHITESPACE
//     * y su contenido no es ya un único espacio.
//     *
//     * Esta regla no depende de ninguna configuración, por lo que siempre está
//     * potencialmente activa.
//     */
//    override fun canHandle(stream: TokenStream, config: FormatConfig, state: FormatState): Boolean {
//        val nextToken = stream.peak() ?: return false
//
//        // Se activa para cualquier token de espacio en blanco que no sea ya un espacio simple.
//        // Esto incluye "  ", "\t", " \t ", etc.
//        return nextToken.getType() == CommonTypes.WHITESPACE && nextToken.getValue() != " "
//    }
//
//    /**
//     * Consume el token de espacio en blanco múltiple y lo reemplaza con un único espacio.
//     */
//    override fun apply(
//        stream: TokenStream,
//        config: FormatConfig,
//        state: FormatState
//    ): RuleResult {
//        // Consume el token de whitespace ("   ", "\t", etc.).
//        stream.next()
//
//        // El resultado siempre es un único espacio.
//        val newText = " "
//
//        // El estado se actualiza para indicar que no estamos en una nueva línea.
//        val nextState = state.copy(isNewLine = false)
//
//        return RuleResult(newText = newText, state = nextState)
//    }
//}