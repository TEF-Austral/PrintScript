package converter.factory

import converter.TokenConverter

sealed interface TokenConverterFactory {

    fun createDefaultsTokenConverter(): TokenConverter

}