package com.example.easycodevideojokes.data

import com.example.easycodevideojokes.data.cache.CacheDataSource
import com.example.easycodevideojokes.data.cache.JokeCacheCallback
import com.example.easycodevideojokes.data.cloud.CloudDataSource
import com.example.easycodevideojokes.data.cloud.JokeCloud
import com.example.easycodevideojokes.data.cloud.JokeCloudCallback
import com.example.easycodevideojokes.presentation.JokeUi
import com.example.easycodevideojokes.presentation.ManageResources

class BaseRepository(
    private val cloudDataSource: CloudDataSource,
    private val cacheDataSource: CacheDataSource
) : Repository<JokeUi, Error> {

    private var callback: ResultCallback<JokeUi, Error>? = null
    private var jokeCloudCached: JokeCloud? = null

    override fun fetch() {
        if (getJokeFromCache) {

            cacheDataSource.fetch(object : JokeCacheCallback {
                override fun provideJoke(joke: JokeCloud) {
                    callback?.provideSuccess(joke.toFavoriteUi())
                }

                override fun provideError(error: Error) {
                    callback?.provideError(error)
                }
            })
        } else
            cloudDataSource.fetch(object : JokeCloudCallback {
                override fun provideJokeCloud(jokeCloud: JokeCloud) {
                    jokeCloudCached = jokeCloud
                    callback?.provideSuccess(jokeCloud.toUi())
                }

                override fun provideError(error: Error) {
                    jokeCloudCached = null
                    callback?.provideError(error)
                }
            })
    }

    override fun clear() {
        callback = null
    }

    override fun changeJokeStatus(resultCallback: ResultCallback<JokeUi, Error>) {
        jokeCloudCached?.let {
            resultCallback.provideSuccess(it.change(cacheDataSource))
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