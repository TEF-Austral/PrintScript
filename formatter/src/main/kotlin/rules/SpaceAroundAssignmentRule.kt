package formatter.rules

import TokenStream
import formatter.config.FormatConfig
import type.CommonTypes

/**
 * Aplica una regla de formato para añadir o quitar espacios alrededor del operador
 * de asignación (=), operando sobre un TokenStream.
 */
class SpaceAroundAssignmentRule : FormatRule {

/**
     * La regla se activa si el *siguiente* token en el stream es de tipo asignación
     * y la configuración correspondiente (`spaceAroundAssignment`) está definida (no es null).
     *
     * Usa `peak()` para inspeccionar el token sin consumirlo.
     */
    override fun canHandle(stream: TokenStream, config: FormatConfig): Boolean {
        // Obtenemos el siguiente token sin avanzar el stream.
        val nextToken = stream.peak() ?: return false

        return nextToken.getType() == CommonTypes.ASSIGNMENT &&
                config.spaceAroundAssignment != null
    }

    /**
     * Consume el token de asignación del stream, lo reemplaza con una versión formateada
     * que respeta la configuración de espaciado, y devuelve el nuevo texto.
     */
    override fun apply(
        stream: TokenStream,
        config: FormatConfig,
        state: FormatState
    ): RuleResult {
        // Consume el token del stream. canHandle ya verificó que existe y es del tipo correcto.
        val currentToken = stream.next()!!.token

        // La configuración no será null gracias a la comprobación en canHandle.
        val spaceAround = config.spaceAroundAssignment!!

        // SpaceUtil es perfecto para esto. Le decimos que ponga o quite
        // el espacio en AMBOS lados del símbolo '=' según un solo ajuste.
        val formattedAssignment = SpaceUtil.rebuild(
            raw = currentToken.getValue(),
            symbol = "=", // El símbolo es siempre "=" para esta regla
            spaceBefore = spaceAround,
            spaceAfter = spaceAround
        )

        // Devolvemos el nuevo texto para el token y el estado sin cambios.
        return RuleResult(newText = formattedAssignment, state = state)
    }
}