package com.nikhilchauhan.contextual_cards.ui

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikhilchauhan.contextual_cards.data.remote.NetworkResult
import com.nikhilchauhan.contextual_cards.data.remote.responsemodel.CardsResponse
import com.nikhilchauhan.contextual_cards.data.remote.responsemodel.CardsResponse.CardGroup
import com.nikhilchauhan.contextual_cards.data.remote.responsemodel.CardsResponse.CardGroup.Card
import com.nikhilchauhan.contextual_cards.data.remote.responsemodel.CardsResponse.CardGroup.Card.FormattedDescription
import com.nikhilchauhan.contextual_cards.data.remote.responsemodel.CardsResponse.CardGroup.Card.FormattedTitle
import com.nikhilchauhan.contextual_cards.data.remote.responsemodel.CardsResponse.CardGroup.Card.FormattedTitle.Entity
import com.nikhilchauhan.contextual_cards.data.repository.CardsRepository
import com.nikhilchauhan.contextual_cards.ui.CardGroupTypes.Hc1
import com.nikhilchauhan.contextual_cards.ui.CardGroupTypes.Hc3
import com.nikhilchauhan.contextual_cards.ui.CardGroupTypes.Hc5
import com.nikhilchauhan.contextual_cards.ui.CardGroupTypes.Hc6
import com.nikhilchauhan.contextual_cards.ui.CardGroupTypes.Hc9
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardsVM @Inject constructor(
  private val repository: CardsRepository
) : ViewModel() {

  init {
    fetchCardsResponse()
  }

  private val _cardsResponse: MutableStateFlow<NetworkResult<CardsResponse>?> =
    MutableStateFlow(null)

  private val _cardsList: MutableStateFlow<MutableList<CardsResponse.CardGroup.Card?>> =
    MutableStateFlow(mutableListOf())

  val _hc3CardsList: MutableStateFlow<MutableList<CardsResponse.CardGroup.Card?>> =
    MutableStateFlow(mutableListOf())

  val hc3TitleSpanList: MutableStateFlow<MutableList<SpannableStringBuilder>> =
    MutableStateFlow(mutableListOf())

  val hc3DescriptionSpanList: MutableStateFlow<MutableList<SpannableStringBuilder>> =
    MutableStateFlow(mutableListOf())

  val hc1DescriptionSpanList: MutableStateFlow<MutableList<SpannableStringBuilder>> =
    MutableStateFlow(mutableListOf())

  val hc1TitleSpanList: MutableStateFlow<MutableList<SpannableStringBuilder>> =
    MutableStateFlow(mutableListOf())

  val hc9DescriptionSpanList: MutableStateFlow<MutableList<SpannableStringBuilder>> =
    MutableStateFlow(mutableListOf())

  val hc9TitleSpanList: MutableStateFlow<MutableList<SpannableStringBuilder>> =
    MutableStateFlow(mutableListOf())

  val hc6DescriptionSpanList: MutableStateFlow<MutableList<SpannableStringBuilder>> =
    MutableStateFlow(mutableListOf())

  val hc6TitleSpanList: MutableStateFlow<MutableList<SpannableStringBuilder>> =
    MutableStateFlow(mutableListOf())

  val hc5DescriptionSpanList: MutableStateFlow<MutableList<SpannableStringBuilder>> =
    MutableStateFlow(mutableListOf())

  val hc5TitleSpanList: MutableStateFlow<MutableList<SpannableStringBuilder>> =
    MutableStateFlow(mutableListOf())

  val _hc5CardsList: MutableStateFlow<MutableList<CardsResponse.CardGroup.Card?>> =
    MutableStateFlow(mutableListOf())

  val _hc6CardsList: MutableStateFlow<MutableList<CardsResponse.CardGroup.Card?>> =
    MutableStateFlow(mutableListOf())

  val _hc9CardsList: MutableStateFlow<MutableList<CardsResponse.CardGroup.Card?>> =
    MutableStateFlow(mutableListOf())

  val _hc1CardsList: MutableStateFlow<MutableList<CardsResponse.CardGroup.Card?>> =
    MutableStateFlow(mutableListOf())

  val cardsHashMap = _cardsList.asStateFlow()

  val cardsResponse = _cardsResponse.asStateFlow()

  private fun fetchCardsResponse() {
    viewModelScope.launch {
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
            _hc1CardsList.value.addAll(cardList)
            createSpans(cardList, CardGroupTypes.Hc1)
          }
        }
        "HC3" -> {
          nnGroup.cards?.let { cardList ->
            _hc3CardsList.value.addAll(cardList)
            createSpans(cardList, CardGroupTypes.Hc3)
          }
        }
        "HC5" -> {
          nnGroup.cards?.let { cardList ->
            _hc5CardsList.value.addAll(cardList)
            createSpans(cardList, CardGroupTypes.Hc5)
          }
        }
        "HC6" -> {
          nnGroup.cards?.let { cardList ->
            _hc6CardsList.value.addAll(cardList)
            createSpans(cardList, CardGroupTypes.Hc6)
          }
        }
        "HC9" -> {
          nnGroup.cards?.let { cardList ->
            _hc9CardsList.value.addAll(cardList)
            createSpans(cardList, CardGroupTypes.Hc9)
          }
        }
      }
    }
  }

  private fun createSpans(
    cardList: List<Card?>,
    cardGroup: CardGroupTypes
  ) {
    cardList.forEach { card ->
      card?.run {
        createTitleSpans(formattedTitle, formattedTitle?.entities, cardGroup)
        createDescriptionSpans(formattedDescription, formattedDescription?.entities, cardGroup)
      }
    }
  }

  private fun createTitleSpans(
    formattedTitle: FormattedTitle?,
    entities: List<Entity?>?,
    cardGroup: CardGroupTypes
  ) {
    val text = formattedTitle?.text
    val spansList = mutableListOf<SpannableStringBuilder>()
    entities?.map { entity ->
      entity?.let { nnEntity ->
        val spanStr = SpannableStringBuilder(nnEntity.text)
        nnEntity.text?.let { nnText ->
          spanStr.setSpan(
            ForegroundColorSpan(Color.parseColor(nnEntity.color)), 0, nnText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
          )
        }
        spansList.add(spanStr)
      }
    }
    val newTitle = SpannableStringBuilder()

    text?.let { nnText ->
      Log.d("TAG", "createTitleSpans:Text " + nnText)
      var counter = 0
      var index = 0
      while (index in nnText.indices) {
        Log.d("TAG", "createTitleSpans: " + index)
        if ('{' == nnText[index] && index + 1 in nnText.indices && '}' == nnText[index + 1]) {
          newTitle.append(spansList[counter])
          index += 2
          counter++
        } else {
          newTitle.append(nnText[index])
          index++
        }
      }
    }
    if (spansList.isEmpty() && formattedTitle?.text != null) {
      spansList.add(SpannableStringBuilder(formattedTitle.text))
    }
    emitInTitleFlow(spansList, cardGroup)
  }

  private fun emitInTitleFlow(
    spansList: MutableList<SpannableStringBuilder>,
    cardGroup: CardGroupTypes
  ) {
    when (cardGroup) {
      Hc1 -> {
        hc1TitleSpanList.value.addAll(spansList)
      }
      Hc3 -> {
        hc3TitleSpanList.value.addAll(spansList)
      }
      Hc5 -> {
        hc5TitleSpanList.value.addAll(spansList)
      }
      Hc6 -> {
        hc6TitleSpanList.value.addAll(spansList)
      }
      Hc9 -> {
        hc9TitleSpanList.value.addAll(spansList)
      }
    }
  }

  private fun emitInDescriptionFlow(
    spansList: MutableList<SpannableStringBuilder>,
    cardGroup: CardGroupTypes
  ) {
    when (cardGroup) {
      Hc1 -> {
        hc1DescriptionSpanList.value.addAll(spansList)
      }
      Hc3 -> {
        hc3DescriptionSpanList.value.addAll(spansList)
      }
      Hc5 -> {
        hc5DescriptionSpanList.value.addAll(spansList)
      }
      Hc6 -> {
        hc6DescriptionSpanList.value.addAll(spansList)
      }
      Hc9 -> {
        hc9DescriptionSpanList.value.addAll(spansList)
      }
    }
  }

  private fun createDescriptionSpans(
    formattedDescription: FormattedDescription?,
    entities: List<FormattedDescription.Entity?>?,
    cardGroup: CardGroupTypes
  ) {
    val text = formattedDescription?.text
    val spansList = mutableListOf<SpannableStringBuilder>()
    entities?.map { entity ->
      entity?.let { nnEntity ->
        val spanStr = SpannableStringBuilder(nnEntity.text)
        nnEntity.text?.let { nnText ->
          spanStr.setSpan(
            ForegroundColorSpan(Color.parseColor(nnEntity.color)), 0, nnText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
          )
        }
        spansList.add(spanStr)
      }
    }
    val newDescription = SpannableStringBuilder()

    text?.let { nnText ->
      var counter = 0
      var index = 0
      while (index in nnText.indices) {
        if ('{' == nnText[index] && index + 1 in nnText.indices && '}' == nnText[index + 1]) {
          newDescription.append(spansList[counter])
          index += 2
          counter++
        } else {
          newDescription.append(nnText[index])
          index++
        }
      }
    }
    if (spansList.isEmpty() && formattedDescription?.text != null) {
      spansList.add(SpannableStringBuilder(formattedDescription.text))
    }
    emitInDescriptionFlow(spansList, cardGroup)
  }
}

enum class CardGroupTypes(val type: String) {
  Hc1("HC1"),
  Hc3("HC3"),
  Hc5("HC5"),
  Hc6("HC6"),
  Hc9("HC9"),
}