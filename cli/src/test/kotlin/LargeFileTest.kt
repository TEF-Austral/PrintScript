import builder.DefaultNodeBuilder
import factory.DefaultInterpreterFactory
import factory.DefaultLexerFactory

import factory.StringSplitterFactory
import factory.StringToTokenConverterFactory
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.FileReader
import java.io.PrintStream
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import parser.factory.DefaultParserFactory
import parser.stream.ParserAstStream
import stream.token.LexerTokenStream

class LargeFileTest {

//    private val filePath = "src/test/resources/VerySmallFile.txt"
    private val filePath = "src/test/resources/VeryLargeFile.txt"

    private val fileLines = 32 * 1024 // 2976  // 11912

    fun createFile() {
        LargeScriptCreator().create(filePath, fileLines)
    }

    @Test
    fun `Very Large File Should Pass Interpreter Test Style`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))
        val reader = BufferedReader(FileReader(filePath))
        createFile()
        val lexer =
            DefaultLexerFactory(
                StringSplitterFactory,
                StringToTokenConverterFactory,
            ).createVersionOnePointOne(reader)
        val tokenStream = LexerTokenStream(lexer)
        val parser = DefaultParserFactory().createDefault(DefaultNodeBuilder(), tokenStream)
        val astStream = ParserAstStream(parser)
        val interpreter = DefaultInterpreterFactory().createDefaultInterpreter()
        val result = interpreter.interpret(astStream)

        assertTrue(result.interpretedCorrectly, "The program should have executed successfully.")
        assertEquals("Program executed successfully", result.message)

        val printed = outputStream.toString().trim()
        assertEquals("$fileLines", printed)
    }

//    @Test
//    fun `Very Large File Should Pass Parser and Lexer And Log ASTs`() {
//        val outputStream = ByteArrayOutputStream()
//        createFile()
//        System.setOut(PrintStream(outputStream))
//        val reader = BufferedReader(FileReader(filePath))
//        val lexer =
//            DefaultLexerFactory(
//                StringSplitterFactory,
//                StringToTokenConverterFactory,
//            ).createVersionOnePointOne(reader)
//        val tokenStream = LexerTokenStream(lexer)
//        val parser = DefaultParserFactory().createDefault(DefaultNodeBuilder(), tokenStream)
//        val astStream = ParserAstStream(parser)
//
//        val loggerFile = File("src/test/resources/ParserLogger.txt")
//        loggerFile.writeText("")
//
//        var currentStream = astStream
//        var statementCount = 0
//        while (!currentStream.isAtEnd()) {
//            try {
//                val astResult = currentStream.next()
//                FileWriter(loggerFile, true).use { it.write(astResult.node.toString() + "\n") }
//                currentStream = astResult.nextStream as ParserAstStream
//                statementCount++
//            } catch (e: Exception) {
//                println("Parser failed on statement ${statementCount + 1}: ${e.message}")
//                break
//            }
//        }
//        statementCount += 2
//        // This will print how many statements were actually parsed.
//        println("Total statements parsed: $statementCount")
//
//        val printed = outputStream.toString().trim()
//        assertEquals("$fileLines", printed)
//    }

//    @Test
//    fun `Very Large File Should Log All Tokens From TokenStream`() {
//        val outputStream = ByteArrayOutputStream()
//        createFile()
//        System.setOut(PrintStream(outputStream))
//        val reader = BufferedReader(FileReader(filePath))
//        val lexer =
//            DefaultLexerFactory(
//                StringSplitterFactory,
//                StringToTokenConverterFactory,
//            ).createVersionOnePointOne(reader)
//        val tokenStream = LexerTokenStream(lexer)
//
//        val loggerFile = File("src/test/resources/logger.txt")
//        loggerFile.writeText("")
//
//        var currentStream = tokenStream
//        var tokenCount = 0
//        var lastToken: Token? = null
//        while (!currentStream.isAtEnd()) {
//            val token = currentStream.peak()
//            if (token != null) {
//                FileWriter(loggerFile, true).use { it.write(token.toString() + "\n") }
//                lastToken = token
//                tokenCount++
//            }
//            currentStream = currentStream.next()?.nextStream as? LexerTokenStream ?: break
//        }
//
//        println("Total tokens: $tokenCount")
//        println("Last token: $lastToken")
//
//        val printed = outputStream.toString().trim()
//        assertEquals("$fileLines", printed)
//    }
//
//    @Test
//    fun `Very Large File Should Log Nodes from Mock TokenStream`() {
//        val outputStream = ByteArrayOutputStream()
//        createFile()
//        System.setOut(PrintStream(outputStream))
//        val reader = BufferedReader(FileReader(filePath))
//        val lexer =
//            DefaultLexerFactory(
//                StringSplitterFactory,
//                StringToTokenConverterFactory,
//            ).createVersionOnePointOne(reader)
//
//        val tokenList = mutableListOf<Token>()
//        val tokenStream = LexerTokenStream(lexer)
//        var currentStream = tokenStream
//        while (!currentStream.isAtEnd()) {
//            val token = currentStream.peak()
//            if (token != null) {
//                tokenList.add(token)
//            }
//            currentStream = currentStream.next()?.nextStream as? LexerTokenStream ?: break
//        }
//
//        val mockTokenStream = MockTokenStream(tokenList)
//
//        val loggerFile = File("src/test/resources/MockTokenStreamLogger.txt")
//        loggerFile.writeText("")
//
//        val parser = DefaultParserFactory().createDefault(DefaultNodeBuilder(), mockTokenStream)
//        var astStream = ParserAstStream(parser)
//        var statementCount = 0
//        while (!astStream.isAtEnd()) {
//            try {
//                val astResult = astStream.next()
//                FileWriter(loggerFile, true).use { it.write(astResult.node.toString() + "\n") }
//                astStream = astResult.nextStream as ParserAstStream
//                statementCount++
//            } catch (e: Exception) {
//                println("Parser failed on statement ${statementCount + 1}: ${e.message}")
//                break
//            }
//        }
//        println("Total statements parsed: $statementCount")
//        val printed = outputStream.toString().trim()
//        assertEquals("$fileLines", printed)
//    }
}
