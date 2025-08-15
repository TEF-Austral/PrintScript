import builder.NodeBuilder
import node.BlockStatement
import node.Program
import node.Statement

sealed interface Parser {

    fun parse(tokens: List<Token>): Program


}

//let x = 42 ;
//let a = 0;
//println(x) ;
// a = 3;
// a + 3;
// 3 + 3 + 3; -> Declaration
// 3 + 3


class DefaultParser(private val nodeBuilder: NodeBuilder) : Parser {
    override fun parse(tokens: List<Token>): Program {
        val statements = mutableListOf<Statement>()
        for (block in delimiterSplit(tokens)) {
            for (token in block) {
                when (token.getType()) {
                    TokenType.IDENTIFIER -> {
                        identifierParser(block)
                    }
                    TokenType.DECLARATION -> {
                        declarationParser(block)
                    }
                    TokenType.PRINT -> {
                        printParser(block)
                    }
                    else -> {
                        expressionParser(block)
                    }
                }
            }
        }
        return nodeBuilder.buildProgramNode(statements)
    }

    private fun identifierParser(block: List<Token>): Statement {
    }
    private fun declarationParser(block: List<Token>): Statement {
        nodeBuilder.buildVariableDeclarationStatementNode(block.first())
    }
    private fun printParser(tokens: List<Token>): Statement {
       for (token in tokens) {
           if (tokens.size == 4){
               val literalNode = nodeBuilder.buildLiteralExpressionNode(token)
               return nodeBuilder.buildPrintStatementNode(literalNode)
           }
           else{
                val expression = expressionParser(tokens[1 until tokens.size - 1])
                return nodeBuilder.buildPrintStatementNode(expression)
           }
        }
    }

    private fun expressionParser(tokens: List<Token>): Program {
        TODO(" A  IMPLEMENTAR")
    }

    // [3 + 3 + 3];
    // [ 3 + 3 ] + 3;
    // print(3 + 3 + 3);
    // Par expression, Impar
    // print("algo") // lenth 4


    private fun delimiterSplit(tokens: List<Token>): List<List<Token>>  {
        val result = mutableListOf<List<Token>>()
        for (token in tokens) {
            val tempList = mutableListOf<Token>()
            when (token.getType()) {
                TokenType.DELIMITERS -> {
                    result.add(tempList)
                }
                else -> {
                    tempList.add(token)
                }
            }
        }
        return result
    }
}



