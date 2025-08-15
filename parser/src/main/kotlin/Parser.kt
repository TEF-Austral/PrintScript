//// Parser.kt
//package parser
//
//import Token
//import builder.NodeBuilder
//import node.*
//
//interface Parser {
//    fun parse(): Program
//}
//
//class RecursiveDescentParser(
//    private val tokens: List<Token>,
//    private val nodeBuilder: NodeBuilder,
//    private val expressionParser: ExpressionParser,
//    private val statementParser: StatementParser
//) : Parser {
//
//    private var current = 0
//
//    override fun parse(): Program {
//        val statements = mutableListOf<Statement>()
//
//        while (!isAtEnd()) {
//            val statement = statementParser.parseStatement(this)
//            statements.add(statement)
//        }
//
//        return nodeBuilder.buildProgramNode(statements)
//    }
//
//    fun getCurrentToken(): Token? = if (isAtEnd()) null else tokens[current]
//
//    fun advance(): Token? {
//        if (!isAtEnd()) current++
//        return previous()
//    }
//
//    fun previous(): Token? = if (current == 0) null else tokens[current - 1]
//
//    fun isAtEnd(): Boolean = current >= tokens.size
//
//    fun check(type: String): Boolean = if (isAtEnd()) false else getCurrentToken()?.getType() == type
//
//    fun match(vararg types: String): Boolean {
//        for (type in types) {
//            if (check(type)) {
//                advance()
//                return true
//            }
//        }
//        return false
//    }
//
//    fun consume(type: String, message: String): Token {
//        if (check(type)) return advance()!!
//        throw ParseException("Expected $type but found ${getCurrentToken()?.getType()}. $message")
//    }
//
//    fun getExpressionParser(): ExpressionParser = expressionParser
//    fun getNodeBuilder(): NodeBuilder = nodeBuilder
//}
//
//// ExpressionParser.kt - Strategy Pattern para diferentes tipos de expresiones
//interface ExpressionParser {
//    fun parseExpression(parser: RecursiveDescentParser): Expression
//    fun parsePrimary(parser: RecursiveDescentParser): Expression
//    fun parseBinary(parser: RecursiveDescentParser, left: Expression, minPrec: Int): Expression
//}
//
//class DefaultExpressionParser : ExpressionParser {
//
//    override fun parseExpression(parser: RecursiveDescentParser): Expression {
//        val left = parsePrimary(parser)
//        return parseBinary(parser, left, 0)
//    }
//
//    override fun parsePrimary(parser: RecursiveDescentParser): Expression {
//        val current = parser.getCurrentToken() ?: throw ParseException("Unexpected end of input")
//
//        return when (current.getType()) {
//            "NUMBER", "STRING", "BOOLEAN" -> {
//                parser.advance()
//                parser.getNodeBuilder().buildLiteralExpressionNode(current)
//            }
//
//            "IDENTIFIER" -> {
//                parser.advance()
//                parser.getNodeBuilder().buildIdentifierNode(current)
//            }
//
//            "LEFT_PAREN" -> {
//                parser.advance() // consume '('
//                val expr = parseExpression(parser)
//                parser.consume("RIGHT_PAREN", "Expected ')' after expression")
//                expr
//            }
//
//            else -> throw ParseException("Expected expression but found ${current.getType()}")
//        }
//    }
//
//    override fun parseBinary(parser: RecursiveDescentParser, left: Expression, minPrec: Int): Expression {
//        var result = left
//
//        while (true) {
//            val current = parser.getCurrentToken()
//            if (current == null || !isOperator(current.getType())) break
//
//            val prec = getOperatorPrecedence(current.getType())
//            if (prec < minPrec) break
//
//            val operator = parser.advance()!!
//            var right = parsePrimary(parser)
//
//            // Manejar asociatividad y precedencia correctamente
//            val nextToken = parser.getCurrentToken()
//            if (nextToken != null && isOperator(nextToken.getType())) {
//                val nextPrec = getOperatorPrecedence(nextToken.getType())
//                if (prec < nextPrec || (prec == nextPrec && isRightAssociative(operator.getType()))) {
//                    right = parseBinary(parser, right, prec + 1)
//                }
//            }
//
//            result = parser.getNodeBuilder().buildBinaryExpressionNode(result, operator, right)
//        }
//
//        return result
//    }
//
//    private fun isRightAssociative(type: String): Boolean {
//        // La mayoría de operadores son left-associative
//        // Aquí podrías agregar operadores right-associative como potenciación
//        return false
//    }
//
//    private fun isOperator(type: String): Boolean {
//        return type in listOf(
//            "PLUS",
//            "MINUS",
//            "MULTIPLY",
//            "DIVIDE",
//            "EQUALS",
//            "NOT_EQUALS",
//            "LESS_THAN",
//            "GREATER_THAN"
//        )
//    }
//
//    private fun getOperatorPrecedence(type: String): Int {
//        return when (type) {
//            "EQUALS", "NOT_EQUALS" -> 1
//            "LESS_THAN", "GREATER_THAN" -> 2
//            "PLUS", "MINUS" -> 3
//            "MULTIPLY", "DIVIDE" -> 4
//            else -> 0
//        }
//    }
//}
//
//// StatementParser.kt - Strategy Pattern para diferentes tipos de statements
//interface StatementParser {
//    fun parseStatement(parser: RecursiveDescentParser): Statement
//}
//
//class DefaultStatementParser : StatementParser {
//
//    private val declarationParser = VariableDeclarationParser()
//    private val assignmentParser = AssignmentParser()
//    private val printParser = PrintStatementParser()
//    private val blockParser = BlockStatementParser()
//    private val expressionStatementParser = ExpressionStatementParser()
//
//    override fun parseStatement(parser: RecursiveDescentParser): Statement {
//        return when {
//            parser.check("LET") || parser.check("CONST") -> declarationParser.parse(parser)
//            parser.check("PRINT") -> printParser.parse(parser)
//            parser.check("LEFT_BRACE") -> blockParser.parse(parser)
//            isAssignment(parser) -> assignmentParser.parse(parser)
//            else -> expressionStatementParser.parse(parser)
//        }
//    }
//
//    private fun isAssignment(parser: RecursiveDescentParser): Boolean {
//        // Look ahead para determinar si es una asignación
//        if (!parser.check("IDENTIFIER")) return false
//
//        val savedPosition = parser.current
//        parser.advance()
//        val isAssign = parser.check("ASSIGN")
//        parser.current = savedPosition // restore position
//
//        return isAssign
//    }
//}
//
//// Command Pattern - Cada parser de statement es un comando
//sealed interface StatementParserCommand {
//    fun parse(parser: RecursiveDescentParser): Statement
//}
//
//class VariableDeclarationParser : StatementParserCommand {
//    override fun parse(parser: RecursiveDescentParser): Statement {
//        val declarationType = parser.advance()!! // LET or CONST
//        val identifier = parser.consume("IDENTIFIER", "Expected variable name")
//        parser.consume("COLON", "Expected ':' after variable name")
//        val dataType = parser.consume("TYPE", "Expected data type")
//
//        var initialValue: Expression? = null
//        if (parser.match("ASSIGN")) {
//            initialValue = parser.getExpressionParser().parseExpression(parser)
//        }
//
//        parser.consume("SEMICOLON", "Expected ';' after variable declaration")
//        return parser.getNodeBuilder().buildVariableDeclarationStatementNode(identifier, dataType, initialValue)
//    }
//}
//
//class AssignmentParser : StatementParserCommand {
//    override fun parse(parser: RecursiveDescentParser): Statement {
//        val identifier = parser.consume("IDENTIFIER", "Expected variable name")
//        parser.consume("ASSIGN", "Expected '=' in assignment")
//        val value = parser.getExpressionParser().parseExpression(parser)
//        parser.consume("SEMICOLON", "Expected ';' after assignment")
//
//        return parser.getNodeBuilder().buildAssignmentStatementNode(identifier, value)
//    }
//}
//
//class PrintStatementParser : StatementParserCommand {
//    override fun parse(parser: RecursiveDescentParser): Statement {
//        parser.consume("PRINT", "Expected 'print'")
//        parser.consume("LEFT_PAREN", "Expected '(' after 'print'")
//        val expression = parser.getExpressionParser().parseExpression(parser)
//        parser.consume("RIGHT_PAREN", "Expected ')' after expression")
//        parser.consume("SEMICOLON", "Expected ';' after print statement")
//
//        return parser.getNodeBuilder().buildPrintStatementNode(expression)
//    }
//}
//
//class BlockStatementParser : StatementParserCommand {
//    override fun parse(parser: RecursiveDescentParser): Statement {
//        parser.consume("LEFT_BRACE", "Expected '{'")
//        val statements = mutableListOf<Statement>()
//
//        while (!parser.check("RIGHT_BRACE") && !parser.isAtEnd()) {
//            val statementParser = DefaultStatementParser()
//            statements.add(statementParser.parseStatement(parser))
//        }
//
//        parser.consume("RIGHT_BRACE", "Expected '}'")
//        return parser.getNodeBuilder().buildBlockStatementNode(statements)
//    }
//}
//
//class ExpressionStatementParser : StatementParserCommand {
//    override fun parse(parser: RecursiveDescentParser): Statement {
//        val expression = parser.getExpressionParser().parseExpression(parser)
//        parser.consume("SEMICOLON", "Expected ';' after expression")
//        return parser.getNodeBuilder().buildExpressionStatementNode(expression)
//    }
//}
//
//interface ParserFactory {
//    fun createParser(tokens: List<Token>, nodeBuilder: NodeBuilder): Parser
//}
//
//class RecursiveDescentParserFactory : ParserFactory {
//    override fun createParser(tokens: List<Token>, nodeBuilder: NodeBuilder): Parser {
//        val expressionParser = DefaultExpressionParser()
//        val statementParser = DefaultStatementParser()
//        return RecursiveDescentParser(tokens, nodeBuilder, expressionParser, statementParser)
//    }
//}
//
//class ParseException(message: String) : Exception(message)
//
