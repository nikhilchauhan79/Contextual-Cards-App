package com.nikhilchauhan.contextual_cards.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikhilchauhan.contextual_cards.data.remote.NetworkResult
import com.nikhilchauhan.contextual_cards.data.remote.responsemodel.CardsResponse
import com.nikhilchauhan.contextual_cards.data.repository.CardsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardsVM @Inject constructor(
  private val repository: CardsRepository
) : ViewModel() {
  private val _cardsResponse: MutableStateFlow<NetworkResult<CardsResponse>?> =
    MutableStateFlow(null)

  val cardsResponse = _cardsResponse.asStateFlow()

  fun fetchCardsResponse() {
    viewModelScope.launch {
      repository.getCards().collect { response ->
        _cardsResponse.emit(response)
      }
    }
  }
}