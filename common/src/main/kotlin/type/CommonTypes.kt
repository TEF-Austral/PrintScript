package type

enum class CommonTypes {
    DELIMITERS, // ( ) { } , . ; : ?
    OPERATORS, // + - * / %
    ASSIGNMENT, // =
    COMPARISON, // == != < > <= >=
    LOGICAL_OPERATORS, // && ||
    CONDITIONALS, // if else switch case
    LOOPS, // for while do
    DATA_TYPES, // NUMBER STRING
    BOOLEAN_LITERAL,
    STRING_LITERAL, // "Hello" 'World'
    NUMBER_LITERAL, // 123 45.67
    IDENTIFIER, // variable names, function names
    RETURN, // return
    FUNCTION, // function
    LET,
    CONST,
    PRINT, // print
    NUMBER {
        override fun toString(): String = super.toString().lowercase().toTitleCase()
    },
    STRING {
        override fun toString(): String = super.toString().lowercase().toTitleCase()
    },
    BOOLEAN {
        override fun toString(): String = super.toString().lowercase().toTitleCase()
    },
    EMPTY,
}

private fun String.toTitleCase(): String =
    this.split(' ').joinToString(" ") { word ->
        word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
