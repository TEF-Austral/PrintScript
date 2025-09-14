import builder.DefaultNodeBuilder
import coordinates.Position
import formatter.factory.FormatterFactory
import formatter.config.FormatConfig
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import transformer.StringToPrintScriptVersion
import type.CommonTypes
import type.Version

class Version11FormatterTest {
    private val builder = DefaultNodeBuilder()

    private val transformer = StringToPrintScriptVersion()

    private fun transform(version: String): Version = transformer.transform(version)

    private fun tok(
        type: CommonTypes,
        value: String,
    ) = PrintScriptToken(type, value, Position(1, 1))

    @Test
    fun `if statement version 11 default indent size`() {
        // if(true) { println("inside"); }
        val condition = builder.buildLiteralExpressionNode(tok(CommonTypes.BOOLEAN_LITERAL, "true"))
        val printStmt =
            builder.buildPrintStatementNode(
                builder.buildLiteralExpressionNode(tok(CommonTypes.STRING_LITERAL, "inside")),
            )
        val ifStmt = builder.buildIfStatementNode(condition, printStmt, null)
        val program = builder.buildProgramNode(listOf(ifStmt))

        val formatter = FormatterFactory.createWithVersion(transform("1.1"))
        val result = formatter.formatToString(program, FormatConfig())

        val expected =
            """
            if(true) {
                println("inside");
            }
            """.trimIndent().replace("", "")
        assertEquals(expected, result)
    }

    @Test
    fun `if statement with else version 11`() {
        // if(false) { println("yes"); } else { println("no"); }
        val condition =
            builder.buildLiteralExpressionNode(
                tok(CommonTypes.BOOLEAN_LITERAL, "false"),
            )
        val yesStmt =
            builder.buildPrintStatementNode(
                builder.buildLiteralExpressionNode(tok(CommonTypes.STRING_LITERAL, "yes")),
            )
        val noStmt =
            builder.buildPrintStatementNode(
                builder.buildLiteralExpressionNode(tok(CommonTypes.STRING_LITERAL, "no")),
            )
        val ifStmt = builder.buildIfStatementNode(condition, yesStmt, noStmt)
        val program = builder.buildProgramNode(listOf(ifStmt))

        val formatter = FormatterFactory.createWithVersion(transform("1.1"))
        val result = formatter.formatToString(program, FormatConfig())

        val expected =
            """
            if(false) {
                println("yes");
            } else {
                println("no");
            }
            """.trimIndent().replace("", "")
        assertEquals(expected, result)
    }

    @Test
    fun `custom indentSize for if block version 11`() {
        // indentSize = 2
        val condition = builder.buildLiteralExpressionNode(tok(CommonTypes.BOOLEAN_LITERAL, "true"))
        val printStmt =
            builder.buildPrintStatementNode(
                builder.buildLiteralExpressionNode(tok(CommonTypes.STRING_LITERAL, "X")),
            )
        val ifStmt = builder.buildIfStatementNode(condition, printStmt, null)
        val program = builder.buildProgramNode(listOf(ifStmt))

        val config = FormatConfig(indentSize = 2)
        val formatter = FormatterFactory.createWithVersion(transform("1.1"))
        val result = formatter.formatToString(program, config)

        val expected =
            """
            if(true) {
              println("X");
            }
            """.trimIndent().replace("", "")
        assertEquals(expected, result)
    }

    @Test
    fun `const declaration version 11`() {
        val valueExpr =
            builder.buildLiteralExpressionNode(
                tok(CommonTypes.NUMBER_LITERAL, "3.14"),
            )
        val decl =
            builder.buildVariableDeclarationStatementNode(
                tok(CommonTypes.CONST, "const"),
                identifier = tok(CommonTypes.IDENTIFIER, "pi"),
                dataType = tok(CommonTypes.NUMBER, "number"),
                initialValue = valueExpr,
            )
        val program = builder.buildProgramNode(listOf(decl))

        val formatter = FormatterFactory.createWithVersion(transform("1.1"))
        val result = formatter.formatToString(program, FormatConfig())

        assertEquals("const pi: number = 3.14;", result)
    }

    @Test
    fun `const declaration inside if version 11`() {
        val condition =
            builder.buildLiteralExpressionNode(
                tok(CommonTypes.BOOLEAN_LITERAL, "true"),
            )
        val constDecl =
            builder.buildVariableDeclarationStatementNode(
                tok(CommonTypes.CONST, "const"),
                identifier = tok(CommonTypes.IDENTIFIER, "a"),
                dataType = tok(CommonTypes.NUMBER, "number"),
                initialValue =
                    builder.buildLiteralExpressionNode(
                        tok(CommonTypes.NUMBER_LITERAL, "1"),
                    ),
            )
        val ifStmt = builder.buildIfStatementNode(condition, constDecl, null)
        val program = builder.buildProgramNode(listOf(ifStmt))

        val formatter = FormatterFactory.createWithVersion(transform("1.1"))
        val result = formatter.formatToString(program, FormatConfig())

        val expected =
            """
            if(true) {
                const a: number = 1;
            }
            """.trimIndent().replace("", "")
        assertEquals(expected, result)
    }

    @Test
    fun `nested if statements version 11 indent levels`() {
        val innerPrint =
            builder.buildPrintStatementNode(
                builder.buildLiteralExpressionNode(tok(CommonTypes.STRING_LITERAL, "inner")),
            )
        val innerIf =
            builder.buildIfStatementNode(
                builder.buildLiteralExpressionNode(tok(CommonTypes.BOOLEAN_LITERAL, "false")),
                innerPrint,
                null,
            )
        val outerIf =
            builder.buildIfStatementNode(
                builder.buildLiteralExpressionNode(tok(CommonTypes.BOOLEAN_LITERAL, "true")),
                innerIf,
                null,
            )
        val program = builder.buildProgramNode(listOf(outerIf))

        val formatter = FormatterFactory.createWithVersion(transform("1.1"))
        val result = formatter.formatToString(program, FormatConfig())

        val expected =
            """
            if(true) {
                if(false) {
                    println("inner");
                }
            }
            """.trimIndent()
        assertEquals(expected, result)
    }

    @Test
    fun `blank line before println in if block when configured`() {
        val printStmt =
            builder.buildPrintStatementNode(
                builder.buildLiteralExpressionNode(tok(CommonTypes.STRING_LITERAL, "inside")),
            )
        val ifStmt =
            builder.buildIfStatementNode(
                builder.buildLiteralExpressionNode(tok(CommonTypes.BOOLEAN_LITERAL, "true")),
                printStmt,
                null,
            )
        val program = builder.buildProgramNode(listOf(ifStmt))

        val config = FormatConfig(blankLinesBeforePrintln = 1)
        val formatter = FormatterFactory.createWithVersion(transform("1.1"))
        val result = formatter.formatToString(program, config)

        val expected =
            """
            if(true) {
            
                println("inside");
            }
            """.trimIndent()
        assertEquals(expected, result)
    }

    @Test
    fun `let declaration inside else with custom spacing and indent`() {
        val printOk =
            builder.buildPrintStatementNode(
                builder.buildLiteralExpressionNode(tok(CommonTypes.STRING_LITERAL, "ok")),
            )
        val declX =
            builder.buildVariableDeclarationStatementNode(
                tok(CommonTypes.LET, "let"),
                identifier = tok(CommonTypes.IDENTIFIER, "x"),
                dataType = tok(CommonTypes.NUMBER, "number"),
                initialValue =
                    builder.buildLiteralExpressionNode(
                        tok(CommonTypes.NUMBER_LITERAL, "1"),
                    ),
            )
        val ifStmt =
            builder.buildIfStatementNode(
                builder.buildLiteralExpressionNode(tok(CommonTypes.BOOLEAN_LITERAL, "true")),
                printOk,
                declX,
            )
        val program = builder.buildProgramNode(listOf(ifStmt))

        val config =
            FormatConfig(
                spaceBeforeColon = true,
                spaceAfterColon = false,
                spaceAroundAssignment = false,
                indentSize = 2,
            )
        val formatter = FormatterFactory.createWithVersion(transform("1.1"))
        val result = formatter.formatToString(program, config)

        val expected =
            """
            if(true) {
              println("ok");
            } else {
              let x :number=1;
            }
            """.trimIndent()
        assertEquals(expected, result)
    }

    @Test
    fun `blank lines capped at max`() {
        val literal =
            builder.buildLiteralExpressionNode(
                tok(CommonTypes.STRING_LITERAL, "test"),
            )
        val printStmt = builder.buildPrintStatementNode(literal)
        val program = builder.buildProgramNode(listOf(printStmt))
        val config = FormatConfig(blankLinesBeforePrintln = 5)
        val formatter = FormatterFactory.createWithVersion(transform("1.1"))

        val result = formatter.formatToString(program, config)
        assertEquals("\n\nprintln(\"test\");", result)
    }

    @Test
    fun `negative blank lines treated as zero`() {
        val literal =
            builder.buildLiteralExpressionNode(
                tok(CommonTypes.STRING_LITERAL, "test"),
            )
        val printStmt = builder.buildPrintStatementNode(literal)
        val program = builder.buildProgramNode(listOf(printStmt))
        val config = FormatConfig(blankLinesBeforePrintln = -2)
        val formatter = FormatterFactory.createWithVersion(transform("1.1"))

        val result = formatter.formatToString(program, config)
        assertEquals("println(\"test\");", result)
    }

    @Test
    fun `const declaration without initial value`() {
        val decl =
            builder.buildVariableDeclarationStatementNode(
                tok(CommonTypes.CONST, "const"),
                identifier = tok(CommonTypes.IDENTIFIER, "pi"),
                dataType = tok(CommonTypes.NUMBER, "number"),
                initialValue = null,
            )
        val program = builder.buildProgramNode(listOf(decl))
        val formatter = FormatterFactory.createWithVersion(transform("1.1"))

        val result = formatter.formatToString(program, FormatConfig())
        assertEquals("const pi: number;", result)
    }
}
