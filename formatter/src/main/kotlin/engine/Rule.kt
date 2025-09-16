package formatter.engine

import Token

class Rule(
    val name: String,
    val applies: (Token, FormatterContext) -> Boolean,
    val apply: (Token, FormatterContext) -> Unit,
)
