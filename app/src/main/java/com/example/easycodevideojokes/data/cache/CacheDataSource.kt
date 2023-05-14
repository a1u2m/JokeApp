package com.example.easycodevideojokes.data.cache

import com.example.easycodevideojokes.data.Error
import com.example.easycodevideojokes.data.cloud.JokeCloud
import com.example.easycodevideojokes.presentation.JokeUi
import com.example.easycodevideojokes.presentation.ManageResources
import kotlin.random.Random

interface CacheDataSource {

    fun addOrRemove(id: Int, joke: JokeCloud): JokeUi
    fun fetch(jokeCacheCallback: JokeCacheCallback)


    class Fake(private val manageResources: ManageResources) : CacheDataSource {

        private val error by lazy { Error.NoFavoriteJoke(manageResources) }
        private var map = mutableMapOf<Int, JokeCloud>()
        override fun addOrRemove(id: Int, joke: JokeCloud): JokeUi {
            return if (map.containsKey(id)) {
                map.remove(id)
                joke.toUi()
            } else {
                map[id] = joke
                joke.toFavoriteUi()
            }
        }

        private var count = 0

        override fun fetch(jokeCacheCallback: JokeCacheCallback) {

            if (map.isEmpty())
                jokeCacheCallback.provideError(error)
            else {
                if (++count == map.size)
                    count = 0

                jokeCacheCallback.provideJoke(
                    map.toList()[count].second
                )
            }
        }
    }
}

interface JokeCacheCallback : ProvideError {
    fun provideJoke(joke: JokeCloud)

}

interface ProvideError {
    fun provideError(error: Error)
}