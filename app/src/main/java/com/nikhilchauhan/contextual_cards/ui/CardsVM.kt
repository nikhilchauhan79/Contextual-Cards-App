package com.nikhilchauhan.contextual_cards.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikhilchauhan.contextual_cards.data.remote.NetworkResult
import com.nikhilchauhan.contextual_cards.data.remote.responsemodel.CardsResponse
import com.nikhilchauhan.contextual_cards.data.remote.responsemodel.CardsResponse.CardGroup
import com.nikhilchauhan.contextual_cards.data.remote.responsemodel.CardsResponse.CardGroup.Card
import com.nikhilchauhan.contextual_cards.data.repository.CardsRepository
import com.nikhilchauhan.contextual_cards.ui.CardGroupTypes.Hc1
import com.nikhilchauhan.contextual_cards.utils.AppConstants
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
  var titleIndicesList =
    MutableStateFlow<MutableMap<CardGroupTypes, MutableList<Int>>>(mutableMapOf())
  var descriptionIndicesList = MutableStateFlow<MutableMap<CardGroupTypes, MutableList<Int>>>(
    mutableMapOf()
  )

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
          nnGroup.cards?.let { cardList ->
            _cardsHashMap.value[Hc1] = cardList
            getIndices(cardList, CardGroupTypes.Hc1)
          }
        }
        "HC3" -> {
          nnGroup.cards?.let { cardList ->
            _cardsHashMap.value[Hc1] = cardList
            getIndices(cardList, CardGroupTypes.Hc3)
          }
        }
        "HC5" -> {
          nnGroup.cards?.let { cardList ->
            _cardsHashMap.value[Hc1] = cardList
            getIndices(cardList, CardGroupTypes.Hc5)
          }
        }
        "HC6" -> {
          nnGroup.cards?.let { cardList ->
            _cardsHashMap.value[Hc1] = cardList
            getIndices(cardList, CardGroupTypes.Hc6)
          }
        }
        "HC9" -> {
          nnGroup.cards?.let { cardList ->
            _cardsHashMap.value[Hc1] = cardList
            getIndices(cardList, CardGroupTypes.Hc9)
          }
        }
      }
    }
  }

  private fun getIndices(
    cardList: List<Card?>,
    groupTypes: CardGroupTypes
  ) {
    cardList.forEach { card ->
      card?.let { nnCard ->
        titleIndicesList.value[groupTypes]?.addAll(
          nnCard.formattedTitle?.text.indexesOf(AppConstants.PLACE_HOLDER)
        )
        descriptionIndicesList.value[groupTypes]?.addAll(
          nnCard.formattedDescription?.text.indexesOf(AppConstants.PLACE_HOLDER)
        )
      }
    }
  }

  fun String?.indexesOf(
    substr: String,
    ignoreCase: Boolean = true
  ): List<Int> {
    return this?.let {
      val indexes = mutableListOf<Int>()
      var startIndex = 0
      while (startIndex in 0 until length) {
        val index = this.indexOf(substr, startIndex, ignoreCase)
        startIndex = if (index != -1) {
          indexes.add(index)
          index + substr.length
        } else {
          index
        }
      }
      return indexes
    } ?: emptyList()
  }
}

enum class CardGroupTypes(val type: String) {
  Hc1("HC1"),
  Hc3("HC3"),
  Hc5("HC5"),
  Hc6("HC6"),
  Hc9("HC9"),
}