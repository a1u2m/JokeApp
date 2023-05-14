package com.example.easycodevideojokes

import com.google.gson.annotations.SerializedName

data class JokeCloud(
    @SerializedName("type")
    val type: String,
    @SerializedName("setup")
    val mainText: String,
    @SerializedName("punchline")
    val punchline: String,
    @SerializedName("id")
    val id: Int
) {

    fun toJoke(): Joke {
        return Joke(mainText, punchline)
    }

}