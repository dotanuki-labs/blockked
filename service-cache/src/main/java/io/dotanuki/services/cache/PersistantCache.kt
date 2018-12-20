package io.dotanuki.services.cache

import android.annotation.SuppressLint
import android.content.Context
import io.dotanuki.blockked.domain.SupportedStatistic
import io.dotanuki.services.common.BitcoinStatsResponse
import io.dotanuki.services.common.CacheService
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.JSON
import kotlinx.serialization.parse
import kotlinx.serialization.stringify

@SuppressLint("ApplySharedPref")
class PersistantCache(context: Context) : CacheService {

    private val prefs by lazy {
        context.getSharedPreferences("blockchaininfo.cache", Context.MODE_PRIVATE)
    }

    @ImplicitReflectionSerializer
    override fun save(key: SupportedStatistic, value: BitcoinStatsResponse) {
        prefs.edit()
            .putString(key.toString(), JSON.nonstrict.stringify(value))
            .commit()
    }

    @ImplicitReflectionSerializer
    override fun retrieveOrNull(key: SupportedStatistic): BitcoinStatsResponse? {
        val target = prefs.getString(key.toString(), null)

        return try {
            JSON.nonstrict.parse(target?.let { it } ?: "{}")
        } catch (error: Throwable) {
            null
        }
    }

    override fun remove(key: SupportedStatistic) {
        prefs.edit()
            .remove(key.toString())
            .commit()
    }

    override fun purge() {
        prefs.edit().clear().commit()
    }

}