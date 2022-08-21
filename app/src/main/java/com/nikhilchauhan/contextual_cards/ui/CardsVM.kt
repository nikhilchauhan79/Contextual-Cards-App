package com.nikhilchauhan.contextual_cards.ui

import android.content.SharedPreferences
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.core.content.edit
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
import com.nikhilchauhan.contextual_cards.utils.AppConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardsVM @Inject constructor(
  private val repository: CardsRepository,
  private val preferences: SharedPreferences
) : ViewModel() {
  private val _cardsResponse: MutableStateFlow<NetworkResult<CardsResponse>?> =
    MutableStateFlow(NetworkResult.INIT)

  init {
    fetchCardsResponse()
  }

  val hc3CardsList: MutableStateFlow<MutableList<Card?>> =
    MutableStateFlow(mutableListOf())

  val scrollableMap = MutableStateFlow<MutableMap<CardGroupTypes, Boolean>?>(null)

  val showHc3Card: MutableStateFlow<Boolean> = MutableStateFlow(true)

  fun setHc3CardVisiblity() {
    showHc3Card.value = preferences.getBoolean(AppConstants.SHOW_HC3, true)
  }

  val showMenuList = MutableStateFlow(mutableListOf<Boolean>())

  val hc9CardWidth = MutableStateFlow<Int?>(null)
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

  val hc5CardsList: MutableStateFlow<MutableList<Card?>> =
    MutableStateFlow(mutableListOf())

  val hc6CardsList: MutableStateFlow<MutableList<Card?>> =
    MutableStateFlow(mutableListOf())

  val hc9CardsList: MutableStateFlow<MutableList<Card?>> =
    MutableStateFlow(mutableListOf())

  val hc1CardsList: MutableStateFlow<MutableList<Card?>> =
    MutableStateFlow(mutableListOf())

  val cardsResponse = _cardsResponse.asStateFlow()

  fun fetchCardsResponse() {
    viewModelScope.launch(Dispatchers.IO) {
      _cardsResponse.emit(NetworkResult.InProgress())
      repository.getCards().collect { response ->
        _cardsResponse.emit(response)
        getHc9CardHeight(response.data?.cardGroups)
        filterCardGroups(response.data?.cardGroups)
      }
    }
  }

  private fun getHc9CardHeight(cardGroups: List<CardGroup?>?) {
    cardGroups?.map {
      if (it?.height != null) {
        hc9CardWidth.value = it.height
      }
    }
  }

  private fun filterCardGroups(cardGroups: List<CardGroup?>?) {
    invalidateCardLists()
    cardGroups?.forEach { nnGroup ->
      when (nnGroup?.designType) {
        "HC1" -> {
          nnGroup.cards?.let { cardList ->
            hc1CardsList.value.addAll(cardList)
            createSpans(cardList, Hc1)
            nnGroup.isScrollable?.let { scrollableMap.value?.put(Hc1, it) }
          }
        }
        "HC3" -> {
          nnGroup.cards?.let { cardList ->
            hc3CardsList.value.addAll(cardList)
            showMenuList.value.add(false)
            createSpans(cardList, Hc3)
            nnGroup.isScrollable?.let { scrollableMap.value?.put(Hc3, it) }
          }
        }
        "HC5" -> {
          nnGroup.cards?.let { cardList ->
            hc5CardsList.value.addAll(cardList)
            createSpans(cardList, Hc5)
            nnGroup.isScrollable?.let { scrollableMap.value?.put(Hc5, it) }
          }
        }
        "HC6" -> {
          nnGroup.cards?.let { cardList ->
            hc6CardsList.value.addAll(cardList)
            createSpans(cardList, Hc6)
            nnGroup.isScrollable?.let { scrollableMap.value?.put(Hc6, it) }
          }
        }
        "HC9" -> {
          nnGroup.cards?.let { cardList ->
            hc9CardsList.value.addAll(cardList)
            createSpans(cardList, Hc9)
            nnGroup.isScrollable?.let { scrollableMap.value?.put(Hc9, it) }
          }
        }
      }
    }
  }

  fun handleDismissNowClick() {
    preferences.edit {
      putBoolean(AppConstants.SHOW_HC3, false)
    }
  }

  private fun invalidateCardLists() {
    hc1CardsList.value = mutableListOf()
    hc5CardsList.value = mutableListOf()
    hc9CardsList.value = mutableListOf()
    hc6CardsList.value = mutableListOf()
    hc3CardsList.value = mutableListOf()
    showMenuList.value = mutableListOf()
  }

  private fun createSpans(
    cardList: List<Card?>,
    cardGroup: CardGroupTypes
  ) {
    cardList.forEach { card ->
      card?.run {
        createTitleSpans(formattedTitle, formattedTitle?.entities, cardGroup, url)
        createDescriptionSpans(formattedDescription, formattedDescription?.entities, cardGroup, url)
      }
    }
  }

  private fun createTitleSpans(
    formattedTitle: FormattedTitle?,
    entities: List<Entity?>?,
    cardGroup: CardGroupTypes,
    url: String?
  ) {
    val text = formattedTitle?.text
    val spansList = mutableListOf<SpannableStringBuilder>()
    val newTitleList = mutableListOf<SpannableStringBuilder>()
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
        Log.d("TAG", "createTitleSpans: " + nnText[index])
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
      newTitleList.add(SpannableStringBuilder(formattedTitle.text))
    } else newTitleList.add(newTitle)

    emitInTitleFlow(newTitleList, cardGroup)
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
        Log.d("TAG", "emitInTitleFlow: " + hc6TitleSpanList)
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
    cardGroup: CardGroupTypes,
    url: String?
  ) {
    val text = formattedDescription?.text
    val spansList = mutableListOf<SpannableStringBuilder>()
    val newDescriptionList = mutableListOf<SpannableStringBuilder>()
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
          Log.d("TAG", "createDescriptionSpans: " + nnText[index])
          newDescription.append(nnText[index])
          index++
        }
      }
    }
    if (spansList.isEmpty() && formattedDescription?.text != null) {
      newDescriptionList.add(SpannableStringBuilder(formattedDescription.text))
    } else newDescriptionList.add(newDescription)
    emitInDescriptionFlow(newDescriptionList, cardGroup)
  }
}

enum class CardGroupTypes(val type: String) {
  Hc1("HC1"),
  Hc3("HC3"),
  Hc5("HC5"),
  Hc6("HC6"),
  Hc9("HC9"),
}