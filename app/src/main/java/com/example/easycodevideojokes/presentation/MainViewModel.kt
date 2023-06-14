package com.example.easycodevideojokes.presentation

import androidx.annotation.DrawableRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easycodevideojokes.data.Error
import com.example.easycodevideojokes.data.Joke
import com.example.easycodevideojokes.data.Repository
import com.example.easycodevideojokes.data.ToBaseUi
import com.example.easycodevideojokes.data.ToFavoriteUi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val repository: Repository<JokeUi, Error>,
    private val toFavorite: Joke.Mapper<JokeUi> = ToFavoriteUi(),
    private val toBaseUi: Joke.Mapper<JokeUi> = ToBaseUi(),
    dispatcherList: DispatcherList = DispatcherList.Base()
) : BaseViewModel(dispatcherList = dispatcherList) {

    val jokeUiLiveData = MutableLiveData<JokeUi>()
    private val blockUi: suspend (JokeUi) -> Unit = {
        jokeUiLiveData.value = it
    }

    fun getJoke() {
        handle({
            val result = repository.fetch()
            if (result.isSuccessful()) result.map(if (result.toFavorite()) toFavorite else toBaseUi)
            else JokeUi.Failed(result.errorMessage())
        }, blockUi)
    }

    fun chooseFavorite(favorites: Boolean) {
        repository.chooseFavorites(favorites)
    }

    fun changeJokeStatus() {
        handle({
            repository.changeJokeStatus()
        }, blockUi)
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
            override fun io() = Dispatchers.IO

            override fun ui() = Dispatchers.Main

        }
    }
}

abstract class BaseViewModel(private val dispatcherList: MainViewModel.DispatcherList) :
    ViewModel() {
    fun <T> handle(
        blockIo: suspend () -> T,
        blockUi: suspend (T) -> Unit
    ) =
        viewModelScope.launch(dispatcherList.io()) {
            val result = blockIo.invoke()
            withContext(dispatcherList.ui()) {
                blockUi.invoke(result)
            }
        }
}