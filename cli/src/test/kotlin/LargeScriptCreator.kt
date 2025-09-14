import java.io.File

class LargeScriptCreator {

    fun create(
        fileName: String,
        numberOfLines: Int,
    ) {
        File(fileName).bufferedWriter().use { writer ->
            writer.appendLine("let x: number = 0;")
            for (i in 1..numberOfLines) {
                writer.appendLine("x = x + 1;")
            }
            writer.appendLine("println(x);")
        }
    }

    fun createPrintFile(
        fileName: String,
        numberOfLines: Int,
    ) {
        File(fileName).bufferedWriter().use { writer ->
            for (i in 1..numberOfLines) {
                writer.appendLine("println(\"line $i\");")
            }
        }
    }
}
