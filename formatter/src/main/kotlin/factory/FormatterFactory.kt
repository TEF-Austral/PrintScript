package formatter.factory

import DefaultFormatter
import LineBreaksAfterPrintlnRule
import SpaceAroundAssignmentRule
import SpaceAroundOperatorsRule
import formatter.DefaultFormatter

import formatter.rules.ColonSpacingRule
import formatter.rules.IfBraceRule
import formatter.rules.LineBreakAfterSemicolonRule
import formatter.rules.NormalizeSpacesInTokenRule

class FormatterFactory {

    fun createFormatter(): DefaultFormatter {
        // Crear lista de reglas en orden de prioridad
        val rules = listOf(
            // Reglas no configurables (siempre activas)
            LineBreakAfterSemicolonRule(),      // Siempre salto de línea después de ;
            NormalizeSpacesInTokenRule(),       // Normaliza espacios múltiples dentro de tokens

            // Reglas configurables
            LineBreaksAfterPrintlnRule(),       // 0, 1, o 2 líneas después de println
            ColonSpacingRule(),                 // Espacios antes/después de :
            SpaceAroundAssignmentRule(),        // Espacios alrededor de =
            SpaceAroundOperatorsRule(),         // Espacios alrededor de +, -, *, /
            // IfBraceRule()                        // Llaves de if en misma línea o siguiente
        )

        return DefaultFormatter(rules)
    }
}
