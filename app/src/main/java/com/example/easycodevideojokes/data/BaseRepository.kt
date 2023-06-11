package com.example.easycodevideojokes.data

import com.example.easycodevideojokes.data.cache.CacheDataSource
import com.example.easycodevideojokes.data.cache.JokeResult
import com.example.easycodevideojokes.data.cloud.CloudDataSource
import com.example.easycodevideojokes.presentation.JokeUi

class BaseRepository(
    private val cloudDataSource: CloudDataSource,
    private val cacheDataSource: CacheDataSource,
    private val change: Joke.Mapper<JokeUi> = Change(cacheDataSource)
) : Repository<JokeUi, Error> {

    private var jokeTemporary: Joke? = null

    override fun fetch(): JokeResult {
        val jokeResult = if (getJokeFromCache) {
            cacheDataSource.fetch()
        } else
            cloudDataSource.fetch()
        jokeTemporary = if (jokeResult.isSuccessful()) {
            jokeResult.map(ToDomain())
        } else
            null
        return jokeResult
    }

    override fun changeJokeStatus(): JokeUi = jokeTemporary!!.map(change)

    private var getJokeFromCache = false
    override fun chooseFavorites(favorites: Boolean) {
        getJokeFromCache = favorites
    }
}