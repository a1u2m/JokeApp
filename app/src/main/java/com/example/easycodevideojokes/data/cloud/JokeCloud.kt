package com.example.easycodevideojokes.data.cloud

import com.example.easycodevideojokes.data.Joke
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
) : Joke {
    override suspend fun <T> map(mapper: Joke.Mapper<T>): T = mapper.map(type, mainText, punchline, id)
}