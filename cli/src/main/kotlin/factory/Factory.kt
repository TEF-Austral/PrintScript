package factory

import factory.result.FactoryResult

interface Factory {
    fun createVersionOne(): FactoryResult

    fun createVersionOnePointOne(): FactoryResult
}
