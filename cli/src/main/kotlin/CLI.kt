import builder.DefaultNodeBuilder
import com.github.ajalt.clikt.core.CliktCommand
import factory.StringToTokenConverterFactory
import parser.factory.VOnePointZeroParserFactory
import factory.StringSplitterFactory
import node.Program

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

    fun parseSourceCode(srcCodePath: String): Program {
        val lexer =
            DefaultLexer(
                StringSplitterFactory.createDefaultsSplitter(),
                StringToTokenConverterFactory.createDefaultsTokenConverter(),
            )
        val tokenList = lexer.tokenize(getDefaultReader(srcCodePath))
        val nodeBuilder = DefaultNodeBuilder()
        val parser = VOnePointZeroParserFactory().createParser(tokenList, nodeBuilder)
        return parser.parse().getProgram()
    }

    private fun getDefaultReader(path: String): Reader = FileReader(path)
}
