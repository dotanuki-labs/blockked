package io.dotanuki.blockked.domain

sealed class CacheEntry {

    object AverageBTCPrice : CacheEntry()

    override fun toString() = when (this) {
        is AverageBTCPrice -> "average-bitcoin-price"
        else -> super.toString()
    }

}