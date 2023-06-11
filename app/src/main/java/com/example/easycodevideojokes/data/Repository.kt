package com.example.easycodevideojokes.data

import com.example.easycodevideojokes.data.cache.JokeResult
import com.example.easycodevideojokes.presentation.JokeUi

interface Repository<S, E> {

    fun fetch(): JokeResult
    fun changeJokeStatus(): JokeUi
    fun chooseFavorites(favorites: Boolean)
}