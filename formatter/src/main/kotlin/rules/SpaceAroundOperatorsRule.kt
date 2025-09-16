package formatter.rules

import Token
import TokenStream
import formatter.config.FormatConfig
import type.CommonTypes

/**
 * Aplica una regla de formato para añadir o quitar espacios alrededor de los operadores
 * aritméticos (+, -, *, /), operando sobre un TokenStream.
 */
class SpaceAroundOperatorsRule : FormatRule {

    /**
     * La regla se activa si el *siguiente* token en el stream es un operador aritmético
     * y la configuración correspondiente está definida.
     * Usa `peak()` para inspeccionar sin consumir el token.
     */
    override fun canHandle(stream: TokenStream, config: FormatConfig): Boolean {
        // Obtenemos el siguiente token sin avanzar el stream.
        val nextToken = stream.peak() ?: return false

        return nextToken.getType() == CommonTypes.OPERATORS &&
                config.spaceAroundOperators != null
    }

    /**
     * Consume el token de operador del stream, lo reemplaza con una versión formateada,
     * y devuelve el nuevo texto.
     */
    override fun apply(
        stream: TokenStream,
        config: FormatConfig,
        state: FormatState
    ): RuleResult {
        // Consume el token del stream. canHandle ya verificó que existe y es del tipo correcto.
        // El !! es seguro aquí, ya que canHandle devolvió true.
        val currentToken = stream.next()!!.token

        // La configuración no será null gracias a la comprobación en canHandle.
        val spaceAround = config.spaceAroundOperators!!

        // Obtenemos el símbolo del operador eliminando cualquier espacio existente.
        val operatorSymbol = currentToken.getValue().trim()

        // Usamos SpaceUtil para reconstruir el token con el espaciado correcto.
        val formattedOperator = SpaceUtil.rebuild(
            raw = currentToken.getValue(),
            symbol = operatorSymbol,
            spaceBefore = spaceAround,
            spaceAfter = spaceAround
        )

        // Devolvemos el nuevo texto para el token y el estado sin cambios.
        return RuleResult(newText = formattedOperator, state = state)
    }
}