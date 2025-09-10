import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import kotlin.system.exitProcess

class FormatCommand :
    CliktCommand(
        help = "Format the source code file",
        name = "format",
    ) {
    private val srcCodePath by argument(
        name = "SOURCE_FILE",
        help = "Path to the source code file",
    ).path(mustExist = true, canBeDir = false, mustBeReadable = true)

    private val formatterConfigFilePath by option(
        "-c",
        "--config",
        help = "Path to formatter configuration file",
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
                cli.handleFormatting(
                    srcCodePath.toString(),
                    formatterConfigFilePath?.toString(),
                    version,
                )
            echo(result)
        } catch (e: Exception) {
            echo("Error formatting file: ${e.message}", err = true)
            exitProcess(1)
        }
    }
}
