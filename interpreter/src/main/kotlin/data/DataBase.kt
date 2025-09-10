package data

import variable.Variable

interface DataBase {
    fun getVariables(): MutableMap<String, Variable>

    fun getConstants(): MutableMap<String, Variable>

    fun addVariable(
        key: String,
        value: Variable,
    )

    fun addConstant(
        key: String,
        value: Variable,
    )

    fun changeVariableValue(
        key: String,
        value: Variable,
    )

    fun getVariableValue(key: String): Any?

    fun getConstantValue(key: String): Any?

    fun isConstant(key: String): Boolean

    fun getValue(key: String): Any?
}
