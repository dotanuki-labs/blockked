package io.dotanuki.services.cache

import android.annotation.SuppressLint
import android.content.Context
import io.dotanuki.services.common.CacheEntry
import io.dotanuki.services.common.CacheService
import io.dotanuki.services.common.MarketPriceResponse
import kotlinx.serialization.json.JSON

@SuppressLint("ApplySharedPref")
class PersistantCache(context: Context) : CacheService {

    private val prefs by lazy {
        context.getSharedPreferences("blockchaininfo.cache", Context.MODE_PRIVATE)
    }

    override fun save(key: CacheEntry, value: MarketPriceResponse) {
        prefs.edit()
            .putString(key.toString(), JSON.stringify(value))
            .commit()
    }

    override fun retrieveOrNull(key: CacheEntry): MarketPriceResponse? {
        val target = prefs.getString(key.toString(), null)

        return try {
            JSON.parse(target)
        } catch (error: Throwable) {
            null
        }
    }

    override fun remove(key: CacheEntry) {
        prefs.edit()
            .remove(key.toString())
            .commit()
    }

    override fun purge() {
        prefs.edit().clear().commit()
    }

}