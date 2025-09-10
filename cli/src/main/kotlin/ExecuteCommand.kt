import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.path
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

    override fun run() {
        try {
            val cli = CLI()
            val result = cli.handleExecution(srcCodePath.toString())
            echo(result)
        } catch (e: Exception) {
            echo("Error executing file: ${e.message}", err = true)
            exitProcess(1)
        }
    }
}
