import builder.NodeBuilder
import node.Program

sealed interface Parser {

    fun parse(tokens: List<Token>): Program


}

class DefaultParser(private val nodeBuilder: NodeBuilder) : Parser {

    override fun parse(tokens: List<Token>): Program {
        for (token in tokens) {
            // BlockStatement
            when (token.getType()) {
                TokenType.DECLARATION -> {
                    // Handle variable declaration
                }
                TokenType.ASSIGNMENT -> {
                    // Handle assignment
                }
                TokenType.FUNCTION -> {
                    // Handle function declaration
                }
                TokenType.PRINT -> {
                    // Handle print statement
                }
                TokenType.IDENTIFIER -> {
                    // Handle identifier usage
                }
                TokenType.NUMBER_LITERAL, TokenType.STRING_LITERAL -> {
                    // Handle literals
                }
                else -> {
                    // Handle other tokens or throw an error
                }
            }
        }

    }


}


