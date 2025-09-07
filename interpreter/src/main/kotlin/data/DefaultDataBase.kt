package data

import variable.Variable

class DefaultDataBase(
    private val variables: MutableMap<String, Variable> = mutableMapOf(),
    private val constants: MutableMap<String, Variable> = mutableMapOf(),
) : DataBase {
    override fun getVariables(): MutableMap<String, Variable> = variables

    override fun getConstants(): MutableMap<String, Variable> = constants

    override fun addVariable(
        key: String,
        value: Variable,
    ) {
        variables[key] = value
    }

    override fun addConstant(
        key: String,
        value: Variable,
    ) {
        if (value.getValue() == null) {
            return
        }
        constants[key] = value
    }

    override fun changeVariableValue(
        key: String,
        value: Variable,
    ) {
        variables[key] = value
    }

    override fun getVariableValue(key: String): Any? = variables[key]

    override fun getConstantValue(key: String): Any? = constants[key]
}
