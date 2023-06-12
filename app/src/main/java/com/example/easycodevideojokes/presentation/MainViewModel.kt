package com.example.easycodevideojokes.presentation

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easycodevideojokes.data.Error
import com.example.easycodevideojokes.data.Joke
import com.example.easycodevideojokes.data.Repository
import com.example.easycodevideojokes.data.ToBaseUi
import com.example.easycodevideojokes.data.ToFavoriteUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val repository: Repository<JokeUi, Error>,
    private val toFavorite: Joke.Mapper<JokeUi> = ToFavoriteUi(),
    private val toBaseUi: Joke.Mapper<JokeUi> = ToBaseUi()
) : ViewModel() {
    private var jokeUiCallback: JokeUiCallback = JokeUiCallback.Empty()

    fun getJoke() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.fetch()
            val ui =
                if (result.isSuccessful()) result.map(if (result.toFavorite()) toFavorite else toBaseUi)
                else JokeUi.Failed(result.errorMessage())
            withContext(Dispatchers.Main) {
                ui.show(jokeUiCallback)
            }
        }
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

    fun changeJokeStatus() {
        viewModelScope.launch(Dispatchers.IO) {
            val jokeUI = repository.changeJokeStatus()
            withContext(Dispatchers.Main) {
                jokeUI.show(jokeUiCallback)
            }
        }
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