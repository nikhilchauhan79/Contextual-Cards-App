package com.nikhilchauhan.contextual_cards

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.nikhilchauhan.contextual_cards.data.remote.NetworkResult
import com.nikhilchauhan.contextual_cards.databinding.ActivityCardsBinding
import com.nikhilchauhan.contextual_cards.ui.CardsVM
import com.nikhilchauhan.contextual_cards.ui.adapters.Hc1Adapter
import com.nikhilchauhan.contextual_cards.ui.adapters.Hc3Adapter
import com.nikhilchauhan.contextual_cards.ui.adapters.Hc5Adapter
import com.nikhilchauhan.contextual_cards.ui.adapters.Hc6Adapter
import com.nikhilchauhan.contextual_cards.ui.adapters.Hc9Adapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CardsActivity : AppCompatActivity() {
  private var _binding: ActivityCardsBinding? = null
  private val cardsVM: CardsVM by viewModels()

  private val binding: ActivityCardsBinding?
    get() = _binding
  private lateinit var hc3Adapter: Hc3Adapter
  private lateinit var hc5Adapter: Hc5Adapter
  private lateinit var hc6Adapter: Hc6Adapter
  private lateinit var hc9Adapter: Hc9Adapter
  private lateinit var hc1Adapter: Hc1Adapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    _binding = ActivityCardsBinding.inflate(layoutInflater)
    setContentView(binding?.root)

    binding?.apply {
      handleResponse()
      swipeContainer.setOnRefreshListener {
        cardsVM.fetchCardsResponse()
        swipeContainer.isRefreshing = false
      }
    }
  }

  private fun ActivityCardsBinding.handleResponse() {
    lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        launch {
          cardsVM.cardsResponse.collect { response ->
            when (response) {
              is NetworkResult.Success -> {
                progressBar.visibility = View.GONE
                hc3Adapter = Hc3Adapter(
                  cardsVM.hc3CardsList.value,
                  cardsVM.hc3TitleSpanList.value,
                  cardsVM.hc3DescriptionSpanList.value
                )
                rvHc3.apply {
                  layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                  adapter = hc3Adapter
                }

                hc6Adapter = Hc6Adapter(
                  cardsVM.hc6CardsList.value,
                  cardsVM.hc6TitleSpanList.value,
                  cardsVM.hc6DescriptionSpanList.value
                )
                rvHc6.apply {
                  layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                  adapter = hc6Adapter
                }

                hc5Adapter = Hc5Adapter(
                  cardsVM.hc5CardsList.value,
                  cardsVM.hc5TitleSpanList.value,
                  cardsVM.hc5DescriptionSpanList.value
                )
                rvHc5.apply {
                  layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                  adapter = hc5Adapter
                }

                hc9Adapter = Hc9Adapter(
                  cardsVM.hc9CardsList.value,
                  cardsVM.hc9TitleSpanList.value,
                  cardsVM.hc9DescriptionSpanList.value
                )

                rvHc9.apply {
                  layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                  adapter = hc9Adapter
                }

                hc1Adapter = Hc1Adapter(
                  cardsVM.hc1CardsList.value,
                  cardsVM.hc1TitleSpanList.value,
                  cardsVM.hc1DescriptionSpanList.value
                )

                rvHc1.apply {
                  layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                  adapter = hc1Adapter
                }
              }
              is NetworkResult.Error -> {
                progressBar.visibility = View.GONE
                Snackbar.make(root, response.message.toString(), Snackbar.LENGTH_LONG).show()
              }
              is NetworkResult.InProgress -> {
                progressBar.visibility = View.VISIBLE
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