import emitter.Emitter
import result.InterpreterResult

class PrintCollector : Emitter {
    val messages: MutableList<String?> = ArrayList<String?>()

    override fun emit(value: InterpreterResult) {
        messages.add(value.message)
    }

    override fun stringEmit(value: String) {
        messages.add(value)
    }
}
