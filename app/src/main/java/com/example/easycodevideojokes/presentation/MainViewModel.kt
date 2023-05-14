package com.example.easycodevideojokes.presentation

import androidx.annotation.DrawableRes
import com.example.easycodevideojokes.data.Error
import com.example.easycodevideojokes.data.Repository
import com.example.easycodevideojokes.data.ResultCallback

class MainViewModel(private val repository: Repository<JokeUi, Error>) {

    private var jokeUiCallback: JokeUiCallback = JokeUiCallback.Empty()
    private val resultCallback = object : ResultCallback<JokeUi, Error> {
        override fun provideSuccess(data: JokeUi) = data.show(jokeUiCallback)
        override fun provideError(error: Error) = JokeUi.Failed(error.message()).show(jokeUiCallback)
    }

    fun getJoke() {
        repository.fetch()
    }

    fun init(jokeUiCallback: JokeUiCallback) {
        this.jokeUiCallback = jokeUiCallback
        repository.init(object : ResultCallback<JokeUi, Error> {
            override fun provideSuccess(data: JokeUi) = data.show(jokeUiCallback)
            override fun provideError(error: Error) = JokeUi.Failed(error.message()).show(jokeUiCallback)
        })
    }

    fun clear() {
        jokeUiCallback = JokeUiCallback.Empty()
        repository.clear()
    }

    fun chooseFavorite(favorites: Boolean) {
repository.chooseFavorites(favorites)
    }

    fun changeJokeStatus() {
        repository.changeJokeStatus(resultCallback)
    }
}

interface JokeUiCallback {
    fun provideText(text: String)

    fun provideIconResId(@DrawableRes iconResId: Int)

    class Empty : JokeUiCallback {
        override fun provideText(text: String) = Unit
        override fun provideIconResId(iconResId: Int) = Unit
    }
}