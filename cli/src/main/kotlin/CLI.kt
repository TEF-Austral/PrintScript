import builder.DefaultNodeBuilder
import com.github.ajalt.clikt.core.CliktCommand
import config.AnalyzerConfig
import config.AnalyzerConfigLoader.loadAnalyzerConfig
import factory.DefaultLexerFactory
import factory.StringToTokenConverterFactory
import factory.StringSplitterFactory
import formatter.config.FormatConfig
import formatter.config.FormatConfigLoader.loadFormatConfig
import parser.factory.DefaultParserFactory
import parser.result.FinalResult
import stream.token.LexerTokenStream
import type.Version
import java.io.FileInputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

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
            echo("Usage: ./gradlew run -Pargs=<command> <source_file> [options]")
        }
    }

    fun parseSourceCode(
        srcCodePath: String,
        version: Version,
    ): FinalResult {
        val tokenStream = tokeniseSourceCode(srcCodePath, version)
        val parser =
            DefaultParserFactory.createWithVersion(
                version,
                DefaultNodeBuilder(),
                tokenStream,
            )
        return parser.parse()
    }

    private fun pathToInputStream(srcCodePath: String): InputStream = FileInputStream(srcCodePath)

    fun parseFormatConfigFromFile(configPath: String): FormatConfig = loadFormatConfig(configPath)

    fun parseAnalyzerConfigFromFile(configPath: String): AnalyzerConfig =
        loadAnalyzerConfig(configPath)

    fun tokeniseSourceCode(
        srcCodePath: String,
        version: Version,
    ): TokenStream {
        val reader = InputStreamReader(pathToInputStream(srcCodePath), StandardCharsets.UTF_8)
        val lexerFactory = DefaultLexerFactory(StringSplitterFactory, StringToTokenConverterFactory)
        val lexer = lexerFactory.createLexerWithVersion(version, reader)
        return LexerTokenStream(lexer)
    }
}
