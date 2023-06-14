package com.example.easycodevideojokes.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.easycodevideojokes.JokeApp
import com.example.easycodevideojokes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = (application as JokeApp).viewModel

        binding.showFavoriteCheckBox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.chooseFavorite(isChecked)
        }
        binding.favoriteButton.setOnClickListener {
            viewModel.changeJokeStatus()
        }

        binding.actionButton.setOnClickListener {
            binding.actionButton.isEnabled = false
            binding.progressBar.visibility = View.VISIBLE
            viewModel.getJoke()
        }

        val jokeUiCallback = object : MainViewModel.JokeUiCallback {
            override fun provideText(text: String) {
                binding.actionButton.isEnabled = true
                binding.progressBar.visibility = View.INVISIBLE
                binding.textView.text = text
            }

            override fun provideIconResId(iconResId: Int) {
                binding.favoriteButton.setImageResource(iconResId)
            }
        }
        viewModel.init(jokeUiCallback)
    }
}