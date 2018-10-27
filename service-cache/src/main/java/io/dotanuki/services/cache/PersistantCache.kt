package io.dotanuki.services.cache

import android.content.Context
import com.orhanobut.hawk.Hawk
import io.dotanuki.blockked.domain.CacheEntry
import io.dotanuki.blockked.domain.services.CacheService

class PersistantCache(context: Context) : CacheService {

    init {
        Hawk.init(context).build()
    }

    override fun <T> save(key: CacheEntry, value: T) {
        Hawk.put(key.toString(), value)
    }

    override fun <T> retrieveOrNull(key: CacheEntry): T? =
        Hawk.get(key.toString())

    override fun remove(key: CacheEntry) {
        Hawk.delete(key.toString())
    }

    override fun purge() {
        Hawk.deleteAll()
    }


}