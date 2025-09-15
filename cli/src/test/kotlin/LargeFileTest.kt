
import java.util.logging.Logger

class LargeFileTest {

    companion object {
        private val logger = Logger.getLogger(LargeFileTest::class.java.name)
    }

    private val filePath = "src/test/resources/PrintLogger.txt"
    private val fileLines = 32 * 1024 * 1024

    fun createFile() {
        LargeScriptCreator().create(filePath, fileLines)
    }

    fun createPrintFile() {
        LargeScriptCreator().createPrintFile(filePath, fileLines)
    }
}
//    @Test
//    fun `Very Large File Should Pass Interpreter Test Style`() {
//        createFile()
//
//        val printedLines = mutableListOf<String>()
//        val printStream = object : PrintStream(System.out) {
//            override fun println(x: String?) { printedLines.add(x ?: "") }
//            override fun print(x: String?) { printedLines.add(x ?: "") }
//        }
//        System.setOut(printStream)
//
//        val reader = BufferedReader(FileReader(filePath))
//        val lexer = DefaultLexerFactory(
//            StringSplitterFactory,
//            StringToTokenConverterFactory,
//        ).createVersionOnePointOne(reader)
//        val tokenStream = LexerTokenStream(lexer)
//        val parser = DefaultParserFactory().createDefault(DefaultNodeBuilder(), tokenStream)
//        val astStream = ParserAstStream(parser)
//        val interpreter = DefaultInterpreterFactory().createDefaultInterpreter()
//        val result = interpreter.interpret(astStream)
//
//        assertTrue(result.interpretedCorrectly, "The program should have executed successfully.")
//        assertEquals("Program executed successfully", result.message)
//
//        val printed = printedLines.joinToString("\n").trim()
//        assertEquals("$fileLines", printed)
//    }
//
//    @Test
//    fun `Very Large File And Storage Should Not Pass`() {
//        createPrintFile()
//
//        val reader = BufferedReader(FileReader(filePath))
//        val lexer = DefaultLexerFactory(
//            StringSplitterFactory,
//            StringToTokenConverterFactory,
//        ).createVersionOnePointOne(reader)
//        val tokenStream = LexerTokenStream(lexer)
//        val parser = DefaultParserFactory().createDefault(DefaultNodeBuilder(), tokenStream)
//        val astStream = ParserAstStream(parser)
//        val emitter = PrintCollector()
//        val interpreter = DefaultInterpreterFactory().createWithVersionAndEmitter(
//            Version.VERSION_1_1,
//            emitter,
//        )
//        val result = interpreter.interpret(astStream)
//
//        assertTrue(result.interpretedCorrectly, "The program should have executed successfully.")
//        assertEquals("Program executed successfully", result.message)
//        emitter.messages.forEach { message -> logger.info(message) }
//    }
