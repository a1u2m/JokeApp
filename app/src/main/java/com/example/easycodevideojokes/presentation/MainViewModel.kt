package com.example.easycodevideojokes.presentation

import androidx.annotation.DrawableRes
import com.example.easycodevideojokes.data.Error
import com.example.easycodevideojokes.data.Joke
import com.example.easycodevideojokes.data.Repository
import com.example.easycodevideojokes.data.ToBaseUi
import com.example.easycodevideojokes.data.ToFavoriteUi

class MainViewModel(
    private val repository: Repository<JokeUi, Error>,
    private val toFavorite: Joke.Mapper<JokeUi> = ToFavoriteUi(),
    private val toBaseUi: Joke.Mapper<JokeUi> = ToBaseUi()
) {
    private var jokeUiCallback: JokeUiCallback = JokeUiCallback.Empty()

    fun getJoke() = Thread {
        val result = repository.fetch()
        if (result.isSuccessful()) result.map(if (result.toFavorite()) toFavorite else toBaseUi)
            .show(jokeUiCallback)
        else JokeUi.Failed(result.errorMessage()).show(jokeUiCallback)
    }.start()


    fun init(jokeUiCallback: JokeUiCallback) {
        this.jokeUiCallback = jokeUiCallback
    }

    fun clear() {
        jokeUiCallback = JokeUiCallback.Empty()
    }

    fun chooseFavorite(favorites: Boolean) {
        repository.chooseFavorites(favorites)
    }

    fun changeJokeStatus() = Thread {
        val jokeUI = repository.changeJokeStatus()
        jokeUI.show(jokeUiCallback)
    }.start()
}

interface JokeUiCallback {
    fun provideText(text: String)

    fun provideIconResId(@DrawableRes iconResId: Int)

    class Empty : JokeUiCallback {
        override fun provideText(text: String) = Unit
        override fun provideIconResId(iconResId: Int) = Unit
    }
}