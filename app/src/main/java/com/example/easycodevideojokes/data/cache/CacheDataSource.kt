package com.example.easycodevideojokes.data.cache

import com.example.easycodevideojokes.data.Error
import com.example.easycodevideojokes.data.Joke
import com.example.easycodevideojokes.data.ToBaseUi
import com.example.easycodevideojokes.data.ToCache
import com.example.easycodevideojokes.data.ToFavoriteUi
import com.example.easycodevideojokes.data.cloud.JokeCloud
import com.example.easycodevideojokes.presentation.JokeUi
import com.example.easycodevideojokes.presentation.ManageResources
import io.realm.Realm

interface CacheDataSource : DataSource {

    fun addOrRemove(id: Int, joke: Joke): JokeUi

    class Base(
        private val realm: ProvideRealm,
        manageResources: ManageResources,
        private val error: Error = Error.NoFavoriteJoke(manageResources),
        private val mapper: Joke.Mapper<JokeCache> = ToCache(),
        private val toFavorite: Joke.Mapper<JokeUi> = ToFavoriteUi(),
        private val baseUi: Joke.Mapper<JokeUi> = ToBaseUi()
    ) :
        CacheDataSource {
        override fun addOrRemove(id: Int, joke: Joke): JokeUi {
            realm.provideRealm().let {
                val jokeCached = it.where(JokeCache::class.java).equalTo("id", id).findFirst()
                if (jokeCached == null) {
                    it.executeTransaction { realm ->
                        val jokeDomainCache = joke.map(mapper)
                        realm.insert(jokeDomainCache)
                    }
                    return joke.map(toFavorite)
                } else {
                    it.executeTransaction {
                        jokeCached.deleteFromRealm()

                    }
                    return joke.map(baseUi)
                }
            }
        }

        override fun fetch(jokeCallback: JokeCallback) {
            realm.provideRealm().let {
                val jokes = it.where(JokeCache::class.java).findAll()
                if (jokes.isEmpty()) {
                    jokeCallback.provideError(error)
                } else {
                    val jokeCached = jokes.random()
                    jokeCallback.provideJoke(it.copyFromRealm(jokeCached))
                }
            }
        }
    }

    class Fake(private val manageResources: ManageResources) : CacheDataSource {

        private val error by lazy { Error.NoFavoriteJoke(manageResources) }
        private var map = mutableMapOf<Int, Joke>()
        override fun addOrRemove(id: Int, joke: Joke): JokeUi {
            return if (map.containsKey(id)) {
                map.remove(id)
                joke.map(ToBaseUi())
            } else {
                map[id] = joke
                joke.map(ToFavoriteUi())
            }
        }

        private var count = 0

        override fun fetch(jokeCallback: JokeCallback) {

            if (map.isEmpty())
                jokeCallback.provideError(error)
            else {
                if (++count == map.size)
                    count = 0

                jokeCallback.provideJoke(
                    map.toList()[count].second
                )
            }
        }
    }
}

interface DataSource {
    fun fetch(jokeCallback: JokeCallback)
}

interface JokeCallback : ProvideError {
    fun provideJoke(joke: Joke)

}

interface ProvideError {
    fun provideError(error: Error)
}

interface ProvideRealm {
    fun provideRealm(): Realm
}