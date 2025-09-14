package parser.stream

import node.ASTNode
import stream.AstStream
import stream.AstStreamResult
import java.util.NoSuchElementException
import node.EmptyExpression
import parser.ParserInterface

class ParserAstStream(
    private val parser: ParserInterface,
) : AstStream {

    override fun peak(): ASTNode {
        if (isAtEnd()) {
            throw NoSuchElementException("No se puede hacer 'peak' porque el stream está al final.")
        }

        val result = parser.next()

        return if (result.isSuccess) {
            result.node
        } else {
            throw Exception(
                "Fallo en 'peak': No se pudo parsear el siguiente nodo. Causa: ${result.message}",
            )
        }
    }

    override fun next(): AstStreamResult {
        if (isAtEnd()) {
            throw NoSuchElementException(
                "No se puede obtener el siguiente elemento porque el stream está al final.",
            )
        }

        val result = parser.next()

        return if (result.isSuccess) {
            val nextNode = result.node
            val nextParser = result.parser
            AstStreamResult(nextNode, ParserAstStream(nextParser), true)
        } else {
            return AstStreamResult(EmptyExpression(), ParserAstStream(parser), false)
        }
    }

    override fun isAtEnd(): Boolean = parser.isAtEnd()
}
