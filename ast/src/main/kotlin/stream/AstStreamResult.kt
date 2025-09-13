package stream

import node.ASTNode

data class AstStreamResult(
    val node: ASTNode,
    val nextStream: AstStream,
)
