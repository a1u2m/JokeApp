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

    class Base(text: String, punchline: String) :
        Abstract(text, punchline, R.drawable.ic_favorite_empty_48)

    class Favorite(text: String, punchline: String) :
        Abstract(text, punchline, R.drawable.ic_favorite_filled_48)

    class Failed(text: String) : Abstract(text, "", 0)

}
