import node.ASTNode
import node.Program
import stream.AstStream
import stream.AstStreamResult

class MockAstStream(
    private val statements: List<ASTNode>,
    val index: Int = 0,
) : AstStream {

    constructor(program: Program) : this(program.getStatements())

    override fun peak(): ASTNode? = if (index < statements.size) statements[index] else null

    override fun next(): AstStreamResult {
        val node = statements[index]
        val newStream = MockAstStream(statements, index + 1)
        return AstStreamResult(node, newStream)
    }

    override fun isAtEnd(): Boolean = index >= statements.size
}
