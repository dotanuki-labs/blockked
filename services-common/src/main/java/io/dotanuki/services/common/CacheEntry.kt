package io.dotanuki.services.common

sealed class CacheEntry {

    object BTCPrice : CacheEntry()

    override fun toString() = when (this) {
        is BTCPrice -> "average-bitcoin-price"
        else -> super.toString()
    }

}