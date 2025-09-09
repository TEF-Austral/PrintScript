class Analyzer11Tests {
//    private fun runAnalyzer(
//        stmts: List<Statement>,
//        config: AnalyzerConfig = AnalyzerConfig(),
//        version: String = "1.1.0",
//    ): List<Diagnostic> {
//        val tempConfigFile =
//            File.createTempFile("analyzer", ".json").apply {
//                writeText(
//                    """
//                    {
//                      "identifierStyle":"${config.identifierStyle}",
//                      "restrictPrintlnArgs":${config.restrictPrintlnArgs},
//                      "restrictReadInputArgs":${config.restrictReadInputArgs}
//                    }
//                    """.trimIndent(),
//                )
//                deleteOnExit()
//            }
//        return AnalyzerFactory
//            .create(version, tempConfigFile.absolutePath)
//            .analyze(Program(stmts))
//    }
//
//    private fun tok(
//        text: String,
//        type: CommonTypes = CommonTypes.IDENTIFIER,
//        position: Position = Position(0, 0),
//    ) = PrintScriptToken(type, text, position)
//
//    private fun lit(
//        value: Int,
//        position: Position = Position(0, 0),
//    ) = LiteralExpression(tok(value.toString(), CommonTypes.NUMBER_LITERAL, position), position)
//
//    @Test
//    fun `default 1_1_0 enforces println and readInput rules`() {
//        val stmts =
//            listOf(
//                PrintStatement(
//                    BinaryExpression(lit(1), tok("+", CommonTypes.OPERATORS), lit(2), Position(0, 0)),
//                    Position(0, 0),
//                ),
//                // invalid argument "1+2" → diagnostic
//                ReadEnvExpression("1+2", Position(1, 0)),
//            )
//        val diags = runAnalyzer(stmts)
//        assertEquals(2, diags.size)
//        assertEquals("println must take only a literal or identifier", diags[0].message)
//        assertEquals("readInput must take only a literal or identifier", diags[1].message)
//    }
//
//    @Test
//    fun `1_1_x respects println override but enforces readInput`() {
//        val cfg = AnalyzerConfig(restrictPrintlnArgs = false, restrictReadInputArgs = false)
//        val stmts =
//            listOf(
//                // println rule disabled → no diag
//                PrintStatement(
//                    BinaryExpression(lit(1), tok("+", CommonTypes.OPERATORS), lit(2), Position(0, 0)),
//                    Position(0, 0),
//                ),
//                // still invalid readInput arg → diagnostic
//                ReadInputStatement("1+2", Position(1, 0)),
//            )
//        val diags = runAnalyzer(stmts, cfg)
//        assertEquals(1, diags.size)
//        assertEquals("readInput must take only a literal or identifier", diags[0].message)
//    }
//
//    @Test
//    fun `1_1_x allows valid println and readInput args`() {
//        val stmts =
//            listOf(
//                // literal is valid for println
//                PrintStatement(lit(42), Position(0, 0)),
//                // identifier is valid for readInput
//                ReadInputStatement("myVar", Position(1, 0)),
//            )
//        val diags = runAnalyzer(stmts)
//        assertTrue(diags.isEmpty())
//    }
//
//    @Test
//    fun `version 1_1_0 enforces same rules as 1_0_0`() {
//        val stmts =
//            listOf(
//                // invalid complex expr → println diagnostic
//                PrintStatement(
//                    BinaryExpression(lit(1), tok("+", CommonTypes.OPERATORS), lit(2), Position(0, 0)),
//                    Position(0, 0),
//                ),
//                // invalid string → readInput diagnostic
//                ReadInputStatement("1+2", Position(1, 0)),
//            )
//        val diags = runAnalyzer(stmts, version = "1.1.5")
//        assertEquals(2, diags.size)
//        assertEquals("println must take only a literal or identifier", diags[0].message)
//        assertEquals("readInput must take only a literal or identifier", diags[1].message)
//    }
}
