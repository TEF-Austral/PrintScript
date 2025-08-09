package lexer

sealed interface Lexer {

}

class PrintScriptLexer : Lexer {

    fun tokenize(input: String,splitter: Splitter): List<String> {
        val strings = splitter.split(input)
        for (str in strings) {
            

        }

    }




    // String -> List<String> -> Token -> List<Token>

    // Classes que recibe un string y crea token.

    // let edad = "3";


    // let nombre = "Juan";
    // let edad = 25 \n let nombre = "Juan";


    // Token("let", "keyword"),
    // Token("edad", "identifier"),
    // Token("=", "operator"),
    // Token("25", "number"),
    // Token(";", "SemiColon")

}