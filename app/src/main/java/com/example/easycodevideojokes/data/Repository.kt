package com.example.easycodevideojokes.data

import com.example.easycodevideojokes.presentation.JokeUi

interface Repository<S, E> {

    fun fetch()
    fun clear()
    fun init(resultCallback: ResultCallback<S, E>)
    fun changeJokeStatus(resultCallback: ResultCallback<JokeUi, Error>)
    fun chooseFavorites(favorites: Boolean)

}

interface ResultCallback<S, E> {

    fun provideSuccess(data: S)

    fun provideError(error: E)

}