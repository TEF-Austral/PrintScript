import builder.DefaultNodeBuilder
import com.github.ajalt.clikt.core.CliktCommand
import converter.TokenConverter
import factory.StringToTokenConverterFactory
import parser.factory.VOnePointZeroParserFactory
import factory.StringSplitterFactory
import java.io.StringReader
import node.Program
import parser.result.CompleteProgram
import parser.result.FinalResult

class CLI :
    CliktCommand(
        help =
            "A command-line tool for analyzing, " +
                "formatting, validating, and executing source code",
        name = "mycli",
    ) {
    override fun run() {
        if (currentContext.invokedSubcommand == null) {
            echo("Welcome to the Source Code CLI Tool!")
            echo("Available commands:")
            echo("  format   - Format source code")
            echo("  analyze  - Analyze source code for issues")
            echo("  validate - Validate source code (analyze + format)")
            echo("  execute  - Execute source code")
            echo("")
            echo("Usage: mycli <command> <source_file> [options]")
            echo("For detailed help: mycli <command> --help")
        }
    }
    
    fun parseSourceCode(srcCodePath: String): FinalResult {
        val tokenList = lex(getDefaultReader(srcCodePath).read())
        val nodeBuilder = DefaultNodeBuilder()
        val mockTokenStream = MockTokenStream(tokenList)
        val parser = VOnePointZeroParserFactory().createParser(mockTokenStream, nodeBuilder)
        val result = CompleteProgram(parser,parser.parse().getProgram())
        return result

    }

    private fun getDefaultReader(path: String): Reader = FileReader(path)

    private val tokenConverter: TokenConverter =
        StringToTokenConverterFactory
            .createDefaultsTokenConverter()

    private fun lex(input: String): List<Token> {
        val splitter = StringSplitterFactory.createStreamingSplitter(StringReader(input))
        var lexer: Lexer? = DefaultLexer(tokenConverter, splitter)

        val tokens = mutableListOf<Token>()
        while (lexer != null) {
            val result = lexer.next()
            if (result != null) {
                tokens.add(result.token)
                lexer = result.lexer
            } else {
                lexer = null
            }
        }
        return tokens
    }
}
