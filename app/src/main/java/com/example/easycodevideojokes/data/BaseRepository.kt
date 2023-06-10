package com.example.easycodevideojokes.data

import com.example.easycodevideojokes.data.cache.CacheDataSource
import com.example.easycodevideojokes.data.cache.JokeCallback
import com.example.easycodevideojokes.data.cloud.CloudDataSource
import com.example.easycodevideojokes.presentation.JokeUi

class BaseRepository(
    private val cloudDataSource: CloudDataSource,
    private val cacheDataSource: CacheDataSource,
    private val change: Joke.Mapper<JokeUi> = Change(cacheDataSource),
    toFavorite: Joke.Mapper<JokeUi> = ToFavoriteUi(),
    toBaseUi: Joke.Mapper<JokeUi> = ToBaseUi()
) : Repository<JokeUi, Error> {

    private var callback: ResultCallback<JokeUi, Error>? = null
    private var jokeTemporary: Joke? = null
    private val jokeCacheCallback = BaseJokeCallback(toFavorite)
    private val cloudCallback = BaseJokeCallback(toBaseUi)

    override fun fetch() = if (getJokeFromCache)
        cacheDataSource.fetch(jokeCacheCallback)
    else
        cloudDataSource.fetch(cloudCallback)


    private inner class BaseJokeCallback(
        private val mapper: Joke.Mapper<JokeUi>
    ) : JokeCallback {
        override fun provideJoke(joke: Joke) {
            jokeTemporary = joke
            callback?.provideSuccess(joke.map(mapper))
        }

        override fun provideError(error: Error) {
            jokeTemporary = null
            callback?.provideError(error)
        }

    }

    override fun clear() {
        callback = null
    }

    override fun changeJokeStatus(resultCallback: ResultCallback<JokeUi, Error>) {
        jokeTemporary?.let {
            resultCallback.provideSuccess(it.map(change))
        }
    }

    private var getJokeFromCache = false
    override fun chooseFavorites(favorites: Boolean) {
        getJokeFromCache = favorites
    }

    override fun init(resultCallback: ResultCallback<JokeUi, Error>) {
        callback = resultCallback
    }
}