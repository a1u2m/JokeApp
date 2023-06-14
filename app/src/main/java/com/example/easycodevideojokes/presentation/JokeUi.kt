package com.example.easycodevideojokes.presentation

import androidx.annotation.DrawableRes
import com.example.easycodevideojokes.R

interface JokeUi {

    fun show(jokeUiCallback: JokeUiCallback)
    abstract class Abstract(

        private val text: String,
        private val punchline: String,
        @DrawableRes private val iconResId: Int
    ) : JokeUi {

        override fun show(jokeUiCallback: JokeUiCallback) {
            jokeUiCallback.provideText("$text\n$punchline")
            jokeUiCallback.provideIconResId(iconResId)
        }
    }

    data class Base(private val text: String, private val punchline: String) :
        Abstract(text, punchline, R.drawable.ic_favorite_empty_48)

    data class Favorite(private val text: String, private val punchline: String) :
        Abstract(text, punchline, R.drawable.ic_favorite_filled_48)

    data class Failed(private val text: String) : Abstract(text, "", 0)

}
