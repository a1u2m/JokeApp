package com.example.easycodevideojokes

class Joke(private val text: String, private val punchline: String) {

    fun toUI() = "$text\n$punchline"

}