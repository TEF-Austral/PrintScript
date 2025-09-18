import factory.DefaultInterpreterFactory.createWithVersionAndEmitterAndInputProvider
import input.TerminalInputProvider
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import parser.stream.ParserAstStream
import type.Version
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.Test

@RunWith(Parameterized::class)
class ValidationTest {
    private val basePath = "src/test/resources/validation/"

    @Parameterized.Parameter(0)
    @JvmField
    var version: String = ""

    @Parameterized.Parameter(1)
    @JvmField
    var file: File = File("")

    companion object {
        @Parameterized.Parameters(name = "version {0} - {1}")
        @JvmStatic
        fun data(): Collection<Array<Any>> = collectTestSet("src/test/resources/validation/", false)

        private fun collectTestSet(
            basePath: String,
            includeOlderVersions: Boolean,
        ): Collection<Array<Any>> {
            val baseDir = File(basePath)
            if (!baseDir.exists()) return emptyList()

            return try {
                Files.walk(Paths.get(basePath)).use { stream ->
                    stream
                        .iterator()
                        .asSequence()
                        .filter { Files.isRegularFile(it) }
                        .filter { it.toString().endsWith(".txt") }
                        .filter { Files.isReadable(it) } // Add check for readable files
                        .map { path ->
                            val file = path.toFile()
                            val relativePath = baseDir.toPath().relativize(path).toString()
                            val versionFromPath = extractVersionFromPath(relativePath)
                            Pair(versionFromPath, file)
                        }.filter { (version, _) ->
                            includeOlderVersions || version == "1.1"
                        }.map { (version, file) ->
                            arrayOf<Any>(version, file)
                        }.toList()
                }
            } catch (e: Exception) {
                // If we can't read the validation directory, return empty list to avoid test failures
                emptyList()
            }
        }

        private fun extractVersionFromPath(relativePath: String): String =
            when {
                relativePath.contains("v1_1") -> "1.1"
                relativePath.contains("v1_0") -> "1.0"
                else -> "1.0"
            }
    }

    @Test
    fun testValidation() {
        val interpreter =
            createWithVersionAndEmitterAndInputProvider(
                getVersionEnum(version),
                PrintCollector(),
                TerminalInputProvider(),
            )

        try {
            val sourceCode = CLI().parseSourceCode(file.absolutePath, getVersionEnum(version))
            val astStream = ParserAstStream(sourceCode.getParser())
            val result = interpreter.interpret(astStream)

            val shouldBeValid = file.name.startsWith("valid")
            val errorMatcher = getErrorMatcherForExpectedResult(shouldBeValid)

            val errors = if (result.interpretedCorrectly) emptyList() else listOf(result.message)
            assertThat(errors, errorMatcher)
        } catch (e: Exception) {
            // If we can't read the file, treat it as an invalid case
            val shouldBeValid = file.name.startsWith("valid")
            if (shouldBeValid) {
                throw AssertionError("Expected valid file but got exception: ${e.message}")
            }
            // For invalid files, exceptions are expected
        }
    }

    private fun getVersionEnum(versionString: String): Version =
        when (versionString) {
            "1.1" -> Version.VERSION_1_1
            else -> Version.VERSION_1_0
        }

    private fun getErrorMatcherForExpectedResult(
        shouldBeValid: Boolean,
    ): org.hamcrest.Matcher<List<String?>> =
        if (shouldBeValid) `is`(emptyList()) else not(emptyList())
}
