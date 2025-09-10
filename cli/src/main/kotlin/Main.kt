import com.github.ajalt.clikt.core.subcommands

fun main(args: Array<String>) {
    CLI()
        .subcommands(
            FormatCommand(),
            AnalyzeCommand(),
            ValidateCommand(),
            ExecuteCommand(),
        ).main(args)
}
