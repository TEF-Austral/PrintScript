import factory.DefaultInterpreterFactory.createWithVersionAndEmitterAndInputProvider
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import parser.stream.ParserAstStream
import type.Version
import java.io.File
import kotlin.test.Test

class VersionCompatibilityTest {

    @Test
    fun testVersionFeatureMatrix() {
        val v10Features =
            mapOf(
                "let declaration" to "let x: number = 5;",
                "println function" to "println(\"hello\");",
                "arithmetic operations" to "let result: number = 5 + 3;",
                "string operations" to "let msg: string = \"hello\" + \" world\";",
            )

        val v11OnlyFeatures =
            mapOf(
                "const declaration" to "const x: number = 5;",
                "boolean operations" to "let flag: boolean = true;",
                "if statement" to "let x: number = 5;\nif (x > 0) { println(\"positive\"); }",
                "readInput function" to "let input: string = readInput(\"prompt\");",
                "readEnv function" to "let env: string = readEnv(\"PATH\");",
            )

        v10Features.forEach { (feature, code) ->
            testFeatureInVersion(feature, code, Version.VERSION_1_0, true)
            testFeatureInVersion(feature, code, Version.VERSION_1_1, true)
        }

        v11OnlyFeatures.forEach { (feature, code) ->
            testFeatureInVersion(feature, code, Version.VERSION_1_0, false)
            testFeatureInVersion(feature, code, Version.VERSION_1_1, true)
        }
    }

    private fun testFeatureInVersion(
        feature: String,
        code: String,
        version: Version,
        shouldWork: Boolean,
    ) {
        val printCollector = PrintCollector()
        val interpreter =
            createWithVersionAndEmitterAndInputProvider(version, printCollector, InputCollector())

        val tempFile = createTempFile(code)

        try {
            val sourceCodeResult = CLI().parseSourceCode(tempFile.absolutePath, version)
            val astStream = ParserAstStream(sourceCodeResult.getParser())
            val result = interpreter.interpret(astStream)

            if (shouldWork) {
                assertTrue(
                    result.interpretedCorrectly,
                    "Expected feature '$feature' to work in version $version but got error: ${result.message}",
                )
            } else {
                assertFalse(
                    result.interpretedCorrectly,
                    "Expected feature '$feature' to fail in version $version but interpretation succeeded",
                )
            }
        } catch (e: Exception) {
            if (shouldWork) {
                throw AssertionError(
                    "Expected feature '$feature' to work in version $version but got exception: ${e.message}",
                )
            }
        } finally {
            tempFile.delete()
        }
    }

    private fun createTempFile(content: String): File {
        val tempFile = File.createTempFile("compatibility_test_", ".txt")
        tempFile.writeText(content)
        return tempFile
    }
}
