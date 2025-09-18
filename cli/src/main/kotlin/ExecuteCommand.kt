import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import transformer.StringToPrintScriptVersion
import type.Version
import kotlin.system.exitProcess

class ExecuteCommand :
    CliktCommand(
        help = "Execute the source code file",
        name = "execute",
    ) {

    private val srcCodePath by argument(
        name = "SOURCE_FILE",
        help = "Path to the source code file",
    ).path(mustExist = true, canBeDir = false, mustBeReadable = true)

    private val version by option(
        "-v",
        "--version",
        help = "Version to use (default: 1.0)",
    ).default("1.0")

    private fun transform(version: String): Version =
        StringToPrintScriptVersion().transform(version)

    override fun run() {
        try {
            val cli = CLI()
            val result = cli.handleExecution(srcCodePath.toString(), transform(version))
            echo(result)
        } catch (e: Exception) {
            echo("Error executing file: ${e.message}", err = true)
            exitProcess(1)
        }
    }
}
