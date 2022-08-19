package com.nikhilchauhan.contextual_cards

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.nikhilchauhan.contextual_cards.data.remote.NetworkResult
import com.nikhilchauhan.contextual_cards.databinding.ActivityCardsBinding
import com.nikhilchauhan.contextual_cards.ui.CardsVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CardsActivity : AppCompatActivity() {
  private var _binding: ActivityCardsBinding? = null
  private val cardsVM: CardsVM by viewModels()

  private val binding: ActivityCardsBinding?
    get() = _binding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    _binding = ActivityCardsBinding.inflate(layoutInflater)
    setContentView(binding?.root)

    cardsVM.fetchCardsResponse()
    binding?.apply {
      handleResponse()
    }
  }

  private fun ActivityCardsBinding.handleResponse() {
    lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        launch {
          cardsVM.cardsResponse.collect { response ->
            when (response) {
              is NetworkResult.Success -> {
              }
              is NetworkResult.Error -> {
                Snackbar.make(root, response.message.toString(), Snackbar.LENGTH_LONG).show()
              }
              is NetworkResult.InProgress -> {
              }
              else -> {
              }
            }
          }
        }

      }
    }
  }
}