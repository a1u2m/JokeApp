package com.example.easycodevideojokes.data

import com.example.easycodevideojokes.data.cache.JokeResult
import com.example.easycodevideojokes.presentation.JokeUi

interface Repository<S, E> {

    suspend fun fetch(): JokeResult
    suspend fun changeJokeStatus(): JokeUi
    fun chooseFavorites(favorites: Boolean)
}