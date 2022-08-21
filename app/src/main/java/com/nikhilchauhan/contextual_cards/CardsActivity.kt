package com.nikhilchauhan.contextual_cards

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
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
import com.nikhilchauhan.contextual_cards.ui.CardItemTouchHelper
import com.nikhilchauhan.contextual_cards.ui.CardsVM
import com.nikhilchauhan.contextual_cards.ui.adapters.Hc1Adapter
import com.nikhilchauhan.contextual_cards.ui.adapters.Hc3Adapter
import com.nikhilchauhan.contextual_cards.ui.adapters.Hc5Adapter
import com.nikhilchauhan.contextual_cards.ui.adapters.Hc6Adapter
import com.nikhilchauhan.contextual_cards.ui.adapters.Hc9Adapter
import com.nikhilchauhan.contextual_cards.ui.callbacks.OnItemClickListener
import com.nikhilchauhan.contextual_cards.utils.Utils.hide
import com.nikhilchauhan.contextual_cards.utils.Utils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CardsActivity : AppCompatActivity(), OnItemClickListener {
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
    cardsVM.setHc3CardVisiblity()
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
                progressBar.hide()

                Snackbar.make(root, R.string.response_fetched_successfully, Snackbar.LENGTH_LONG)
                  .show()

                hc3Adapter = Hc3Adapter(
                  cardsVM.hc3CardsList.value,
                  cardsVM.hc3TitleSpanList.value,
                  cardsVM.hc3DescriptionSpanList.value, this@CardsActivity,
                  cardsVM.showMenuList.value
                )
                rvHc3.apply {
                  layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                  adapter = hc3Adapter
                }
                CardItemTouchHelper(
                  cardsVM.hc3CardsList.value, hc3Adapter, this@handleResponse
                ).attachToRecyclerView(rvHc3)

                hc6Adapter = Hc6Adapter(
                  cardsVM.hc6CardsList.value,
                  cardsVM.hc6TitleSpanList.value,
                  cardsVM.hc6DescriptionSpanList.value, this@CardsActivity
                )
                rvHc6.apply {
                  layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                  adapter = hc6Adapter
                }

                hc5Adapter = Hc5Adapter(
                  cardsVM.hc5CardsList.value,
                  cardsVM.hc5TitleSpanList.value,
                  cardsVM.hc5DescriptionSpanList.value, this@CardsActivity
                )
                rvHc5.apply {
                  layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                  adapter = hc5Adapter
                }

                hc1Adapter = Hc1Adapter(
                  cardsVM.hc1CardsList.value,
                  cardsVM.hc1TitleSpanList.value,
                  cardsVM.hc1DescriptionSpanList.value, this@CardsActivity
                )

                rvHc1.apply {
                  layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                  adapter = hc1Adapter
                }
              }
              is NetworkResult.Error -> {
                progressBar.hide()
                // progressBar.visibility = View.GONE
                Snackbar.make(root, response.message.toString(), Snackbar.LENGTH_LONG).show()
              }
              is NetworkResult.InProgress -> {
                progressBar.show()
              }
              else -> {
              }
            }
          }
        }

        launch {
          cardsVM.showHc3Card.collect { visibility ->
            if (!visibility) {
              rvHc3.hide()
            }
          }
        }
        launch {
          cardsVM.hc9CardWidth.collect { width ->
            width?.let { nnWidth ->
              Log.d("width", "handleResponse: " + nnWidth)
              hc9Adapter = Hc9Adapter(
                cardsVM.hc9CardsList.value,
                cardsVM.hc9TitleSpanList.value,
                cardsVM.hc9DescriptionSpanList.value,
                this@CardsActivity,
                nnWidth
              )

              rvHc9.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                adapter = hc9Adapter
              }
            }

          }
        }
      }
    }
  }

  override fun onLongPress(
    position: Int,
    view: View
  ) {
  }

  override fun onItemClick(
    position: Int,
    url: String
  ) {
    val openURL = Intent(Intent.ACTION_VIEW)
    openURL.data = Uri.parse(url)
    startActivity(openURL)
  }

  override fun onRemindLaterClick(position: Int) {
    binding?.rvHc3?.visibility = GONE
  }

  override fun onDismissNowClick(position: Int) {
    binding?.rvHc3?.visibility = GONE
    cardsVM.handleDismissNowClick()
  }
}