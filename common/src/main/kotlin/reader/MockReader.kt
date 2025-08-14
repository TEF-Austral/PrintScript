package reader

class MockReader(private val content: String) : Reader {
    override fun read(): String {
        return content
    }
}

