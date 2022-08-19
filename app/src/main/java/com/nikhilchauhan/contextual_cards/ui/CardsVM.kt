package com.nikhilchauhan.contextual_cards.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikhilchauhan.contextual_cards.data.remote.NetworkResult
import com.nikhilchauhan.contextual_cards.data.remote.responsemodel.CardsResponse
import com.nikhilchauhan.contextual_cards.data.remote.responsemodel.CardsResponse.CardGroup
import com.nikhilchauhan.contextual_cards.data.repository.CardsRepository
import com.nikhilchauhan.contextual_cards.ui.CardGroupTypes.Hc1
import com.nikhilchauhan.contextual_cards.ui.CardGroupTypes.Hc3
import com.nikhilchauhan.contextual_cards.ui.CardGroupTypes.Hc5
import com.nikhilchauhan.contextual_cards.ui.CardGroupTypes.Hc6
import com.nikhilchauhan.contextual_cards.ui.CardGroupTypes.Hc9
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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

  private val _cardsHashMap: MutableStateFlow<MutableMap<CardGroupTypes, List<CardsResponse.CardGroup.Card?>>> =
    MutableStateFlow(mutableMapOf())

  val cardsHashMap = _cardsHashMap.asStateFlow()

  val cardsResponse = _cardsResponse.asStateFlow()

  fun fetchCardsResponse() {
    viewModelScope.launch(Dispatchers.IO) {
      repository.getCards().collect { response ->
        _cardsResponse.emit(response)
        filterCardGroups(response.data?.cardGroups)
      }
    }
  }

  private fun filterCardGroups(cardGroups: List<CardGroup?>?) {
    cardGroups?.forEach { nnGroup ->
      when (nnGroup?.designType) {
        "HC1" -> {
          nnGroup.cards?.let {
            _cardsHashMap.value.put(Hc1, it)
          }
        }
        "HC3" -> {
          nnGroup.cards?.let {
            _cardsHashMap.value.put(Hc3, it)
          }
        }
        "HC5" -> {
          nnGroup.cards?.let {
            _cardsHashMap.value.put(Hc5, it)
          }
        }
        "HC6" -> {
          nnGroup.cards?.let {
            _cardsHashMap.value.put(Hc6, it)
          }
        }
        "HC9" -> {
          nnGroup.cards?.let {
            _cardsHashMap.value.put(Hc9, it)
          }
        }
      }
    }
  }
}

enum class CardGroupTypes(val type: String) {
  Hc1("HC1"),
  Hc3("HC3"),
  Hc5("HC5"),
  Hc6("HC6"),
  Hc9("HC9"),
}