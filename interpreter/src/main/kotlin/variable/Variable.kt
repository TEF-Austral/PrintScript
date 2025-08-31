package variable

import type.CommonTypes

class Variable(
    private val type: CommonTypes,
    private val value: Any?,
) {
    fun getType(): CommonTypes = type

    fun getValue(): Any? = value

    fun changeValue(value: Any?): Variable = Variable(this.type, value)

    fun changeType(type: CommonTypes): Variable = Variable(type, this.value)

    fun isValueNull(): Boolean = value == null
}
