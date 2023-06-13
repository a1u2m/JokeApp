package com.example.easycodevideojokes.presentation

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easycodevideojokes.data.Error
import com.example.easycodevideojokes.data.Joke
import com.example.easycodevideojokes.data.Repository
import com.example.easycodevideojokes.data.ToBaseUi
import com.example.easycodevideojokes.data.ToFavoriteUi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val repository: Repository<JokeUi, Error>,
    private val toFavorite: Joke.Mapper<JokeUi> = ToFavoriteUi(),
    private val toBaseUi: Joke.Mapper<JokeUi> = ToBaseUi(),
    private val handleUi: HandleUi = HandleUi.Base(DispatcherList.Base())
) : ViewModel() {
    private var jokeUiCallback: JokeUiCallback = JokeUiCallback.Empty()

    fun getJoke() =
        handleUi.handle(viewModelScope, jokeUiCallback) {
            val result = repository.fetch()
            if (result.isSuccessful()) result.map(if (result.toFavorite()) toFavorite else toBaseUi)
            else JokeUi.Failed(result.errorMessage())
        }


    override fun onCleared() {
        super.onCleared()
        jokeUiCallback = JokeUiCallback.Empty()
    }

    fun init(jokeUiCallback: JokeUiCallback) {
        this.jokeUiCallback = jokeUiCallback
    }

    fun chooseFavorite(favorites: Boolean) {
        repository.chooseFavorites(favorites)
    }

    fun changeJokeStatus() =
        handleUi.handle(viewModelScope, jokeUiCallback) {
            repository.changeJokeStatus()
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

interface DispatcherList {
    fun io(): CoroutineDispatcher
    fun ui(): CoroutineDispatcher

    class Base : DispatcherList {
        override fun io(): CoroutineDispatcher = Dispatchers.IO

        override fun ui(): CoroutineDispatcher = Dispatchers.Main

    }
}

interface HandleUi {
    fun handle(
        coroutineScope: CoroutineScope,
        jokeUiCallback: JokeUiCallback, block: suspend () -> JokeUi
    )

    class Base(private val dispatcherList: DispatcherList) : HandleUi {
        override fun handle(
            coroutineScope: CoroutineScope,
            jokeUiCallback: JokeUiCallback,
            block: suspend () -> JokeUi
        ) {
            coroutineScope.launch(dispatcherList.io()) {
                val jokeUi = block.invoke()
                withContext(dispatcherList.ui()) {
                    jokeUi.show(jokeUiCallback)
                }
            }
        }

    }
}