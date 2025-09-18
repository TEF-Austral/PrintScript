import factory.DefaultInterpreterFactory.createWithVersionAndEmitterAndInputProvider
import input.TerminalInputProvider
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import parser.stream.ParserAstStream
import type.Version
import java.io.File

@RunWith(Parameterized::class)
class IntegrationTestSuite {

    @Parameterized.Parameter(0)
    @JvmField
    var testName: String = ""

    @Parameterized.Parameter(1)
    @JvmField
    var version: Version = Version.VERSION_1_0

    @Parameterized.Parameter(2)
    @JvmField
    var sourceCode: String = ""

    @Parameterized.Parameter(3)
    @JvmField
    var shouldSucceed: Boolean = true

    @Parameterized.Parameter(4)
    @JvmField
    var expectedOutput: String? = null

    companion object {
        @Parameterized.Parameters(name = "{0}")
        @JvmStatic
        fun data(): Collection<Array<Any?>> =
            listOf(
                arrayOf(
                    "Basic Let Declaration v1.0",
                    Version.VERSION_1_0,
                    "let x: number = 42;",
                    true,
                    null,
                ),
                arrayOf(
                    "Print Statement v1.0",
                    Version.VERSION_1_0,
                    "println(\"Hello\");",
                    true,
                    "Hello",
                ),
                arrayOf(
                    "Arithmetic Expression v1.0",
                    Version.VERSION_1_0,
                    "let result: number = 10 + 5 * 2;\nprintln(result);",
                    true,
                    "20",
                ),
                arrayOf(
                    "String Concatenation v1.0",
                    Version.VERSION_1_0,
                    "let greeting: string = \"Hello\" + \" \" + \"World\";\nprintln(greeting);",
                    true,
                    "Hello World",
                ),
                arrayOf(
                    "Boolean Logic v1.0",
                    Version.VERSION_1_0,
                    "let flag: boolean = true;\nprintln(flag);",
                    true,
                    "true",
                ),
                arrayOf(
                    "Const Declaration v1.1",
                    Version.VERSION_1_1,
                    "const x: number = 42;",
                    true,
                    null,
                ),
                arrayOf(
                    "If Statement True v1.1",
                    Version.VERSION_1_1,
                    "let x: number = 5;\nif (x > 0) {\n    println(\"positive\");\n}",
                    true,
                    "positive",
                ),
                arrayOf(
                    "If Statement False v1.1",
                    Version.VERSION_1_1,
                    "let x: number = -5;\nif (x > 0) {\n    println(\"positive\");\n}",
                    true,
                    null,
                ),
                arrayOf(
                    "If Else Statement v1.1",
                    Version.VERSION_1_1,
                    "let x: number = 5;\nif (x > 10) {\n    println(\"big\");\n} else {\n    println(\"small\");\n}",
                    true,
                    "small",
                ),
                arrayOf(
                    "Read Input v1.1",
                    Version.VERSION_1_1,
                    "let name: string = readInput(\"Name: \");\nprintln(\"Hello \" + name);",
                    true,
                    null,
                ),
                arrayOf(
                    "Read Environment v1.1",
                    Version.VERSION_1_1,
                    "let path: string = readEnv(\"PATH\");\nprintln(path);",
                    true,
                    null,
                ),
                arrayOf(
                    "Const in v1.0 (Invalid)",
                    Version.VERSION_1_0,
                    "const x: number = 5;",
                    false,
                    null,
                ),
                arrayOf(
                    "If in v1.0 (Invalid)",
                    Version.VERSION_1_0,
                    "let x: number = 5;\nif (x > 0) {\n    println(\"positive\");\n}",
                    false,
                    null,
                ),
                arrayOf(
                    "ReadInput in v1.0 (Invalid)",
                    Version.VERSION_1_0,
                    "let name: string = readInput(\"Name: \");",
                    false,
                    null,
                ),
                arrayOf(
                    "ReadEnv in v1.0 (Invalid)",
                    Version.VERSION_1_0,
                    "let path: string = readEnv(\"PATH\");",
                    false,
                    null,
                ),
                arrayOf(
                    "Const Reassignment (Invalid)",
                    Version.VERSION_1_1,
                    "const x: number = 5;\nx = 10;",
                    false,
                    null,
                ),
                arrayOf(
                    "Undefined Variable (Invalid)",
                    Version.VERSION_1_0,
                    "println(undefinedVar);",
                    false,
                    null,
                ),
                arrayOf(
                    "Type Mismatch (Invalid)",
                    Version.VERSION_1_0,
                    "let x: number = \"string\";",
                    false,
                    null,
                ),
                arrayOf(
                    "Missing Semicolon (Invalid)",
                    Version.VERSION_1_0,
                    "let x: number = 5\nprintln(x);",
                    false,
                    null,
                ),
                arrayOf(
                    "Complex Expression v1.0",
                    Version.VERSION_1_0,
                    "let a: number = 10;\nlet b: number = 20;\nlet result: number = (a + b) * 2 - 5;\nprintln(result);",
                    true,
                    "55",
                ),
                arrayOf(
                    "Multiple Variables v1.1",
                    Version.VERSION_1_1,
                    "const PI: number = 3.14;\nlet radius: number = 5;\nlet area: number = PI * radius * radius;\nif (area > 75) {\n    println(\"Large circle\");\n} else {\n    println(\"Small circle\");\n}",
                    true,
                    "Small circle",
                ),
                arrayOf(
                    "Nested If Statements v1.1",
                    Version.VERSION_1_1,
                    "let x: number = 15;\nif (x > 10) {\n    if (x > 20) {\n        println(\"Very large\");\n    } else {\n        println(\"Moderately large\");\n    }\n}",
                    true,
                    "Moderately large",
                ),
                arrayOf(
                    "String and Number Operations v1.0",
                    Version.VERSION_1_0,
                    "let name: string = \"Alice\";\nlet age: number = 25;\nlet message: string = \"Name: \" + name + \", Age: \" + age;\nprintln(message);",
                    true,
                    "Name: Alice, Age: 25",
                ),
                arrayOf(
                    "Division by Zero (Invalid)",
                    Version.VERSION_1_0,
                    "let result: number = 5 / 0;",
                    false,
                    null,
                ),
                arrayOf(
                    "Boolean Arithmetic (Invalid)",
                    Version.VERSION_1_0,
                    "let result: number = true + false;",
                    false,
                    null,
                ),
                arrayOf(
                    "String Number Mismatch (Invalid)",
                    Version.VERSION_1_0,
                    "let result: number = \"hello\" + 5;",
                    false,
                    null,
                ),
                arrayOf(
                    "Comparison Operations v1.1",
                    Version.VERSION_1_1,
                    "let x: number = 10;\nlet y: number = 5;\nif (x > y) {\n    println(\"x is greater\");\n}\nif (x == 10) {\n    println(\"x equals 10\");\n}\nif (y != 10) {\n    println(\"y does not equal 10\");\n}",
                    true,
                    "x is greater\nx equals 10\ny does not equal 10",
                ),
                arrayOf(
                    "Boolean Conditions v1.1",
                    Version.VERSION_1_1,
                    "let flag: boolean = true;\nif (flag) {\n    println(\"flag is true\");\n}\nif (!flag) {\n    println(\"flag is false\");\n} else {\n    println(\"flag is not false\");\n}",
                    true,
                    "flag is true\nflag is not false",
                ),
                arrayOf(
                    "Complex Invalid Syntax",
                    Version.VERSION_1_1,
                    "const x: number = 5\nlet y: string = readInput(\nif (x > y) println(\"error\");",
                    false,
                    null,
                ),
                arrayOf(
                    "Multiple Errors",
                    Version.VERSION_1_0,
                    "let x: number = \"string\";\nlet y = 5;\nprintln(undefinedVar);",
                    false,
                    null,
                ),
            )
    }

    @Test
    fun testIntegration() {
        val printCollector = PrintCollector()
        val interpreter =
            createWithVersionAndEmitterAndInputProvider(
                version,
                printCollector,
                TerminalInputProvider(),
            )

        val tempFile = createTempFile(sourceCode)

        try {
            val sourceCodeResult = CLI().parseSourceCode(tempFile.absolutePath, version)
            val astStream = ParserAstStream(sourceCodeResult.getParser())
            val result = interpreter.interpret(astStream)

            if (shouldSucceed) {
                assertThat(
                    "Expected successful interpretation but got error: ${result.message}",
                    result.interpretedCorrectly,
                    `is`(true),
                )

                expectedOutput?.let { expected ->
                    val actualOutput = printCollector.messages.filterNotNull().joinToString("\n")
                    assertThat("Output mismatch", actualOutput, `is`(expected))
                }
            } else {
                assertThat(
                    "Expected failure but interpretation succeeded",
                    result.interpretedCorrectly,
                    `is`(false),
                )
            }
        } finally {
            tempFile.delete()
        }
    }

    private fun createTempFile(content: String): File {
        val tempFile = File.createTempFile("test_", ".txt")
        tempFile.writeText(content)
        return tempFile
    }
}
