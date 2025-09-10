import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import kotlin.system.exitProcess
import type.Version
import transformer.StringToPrintScriptVersion

class AnalyzeCommand :
    CliktCommand(
        help = "Analyze the source code file for issues",
        name = "analyze",
    ) {
    private val transformer = StringToPrintScriptVersion()

    private fun transform(version: String): Version =
        transformer.transform(version) // TODO SE PUEDE CAMBIAR MAS TARDE

    private val srcCodePath by argument(
        name = "SOURCE_FILE",
        help = "Path to the source code file",
    ).path(mustExist = true, canBeDir = false, mustBeReadable = true)

    private val analyzerConfigFilePath by option(
        "-c",
        "--config",
        help = "Path to analyzer configuration file",
    ).path(canBeDir = false)

    private val version by option(
        "-v",
        "--version",
        help = "Version to use (default: 1.0)",
    ).default("1.0")

    override fun run() {
        try {
            val cli = CLI()
            val result =
                cli.handleAnalyzing(
                    srcCodePath.toString(),
                    analyzerConfigFilePath?.toString(),
                    transform(version),
                )
            echo(result)
        } catch (e: Exception) {
            echo("Error analyzing file: ${e.message}", err = true)
            exitProcess(1)
        }
    }
}
