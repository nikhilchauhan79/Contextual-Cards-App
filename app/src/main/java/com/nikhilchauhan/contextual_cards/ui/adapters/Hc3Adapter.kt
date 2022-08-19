package com.nikhilchauhan.contextual_cards.ui.adapters

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nikhilchauhan.contextual_cards.data.remote.responsemodel.CardsResponse.CardGroup.Card
import com.nikhilchauhan.contextual_cards.databinding.CardHc3Binding
import com.nikhilchauhan.contextual_cards.ui.adapters.Hc3Adapter.Hc3ViewHolder

class Hc3Adapter(
  private val cards: List<Card?>,
  private val titleIndices: List<Int>,
  private val descriptionIndices: List<Int>,
) : RecyclerView.Adapter<Hc3ViewHolder>() {
  var titleIndex = 0
  var descIndex = 0

  inner class Hc3ViewHolder(val binding: CardHc3Binding) : RecyclerView.ViewHolder(binding.root)

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): Hc3ViewHolder {
    val binding = CardHc3Binding.inflate(LayoutInflater.from(parent.context), parent, false)
    return Hc3ViewHolder(binding)
  }

  override fun onBindViewHolder(
    holder: Hc3ViewHolder,
    position: Int
  ) {
    with(holder.binding) {
      val currentCard = cards[position]
      tvHeading.text = currentCard?.name
      setButtonsStyle(currentCard)
    }
  }

  private fun CardHc3Binding.setButtonsStyle(currentCard: Card?) {
    currentCard?.cta?.get(0)?.let {
      btnThreeCard.apply {
        text = it.text
        setBackgroundColor(Color.parseColor(it.bgColor))
      }
    }

    currentCard?.formattedTitle?.let { nnTitle ->
      nnTitle.entities?.forEach { nnEntity ->
        val newTitle = StringBuilder(nnTitle.text.toString()).replaceRange(
          titleIndices[titleIndex], titleIndices[titleIndex] + 2, nnEntity?.text ?: ""
        )
        val titleSpannable = SpannableStringBuilder(newTitle)
        titleSpannable.setSpan(
          ForegroundColorSpan(Color.parseColor(nnEntity?.color)), titleIndices[titleIndex],
          titleIndices[titleIndex] + nnEntity?.text?.length!!, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        titleIndex++
      }

    }
    currentCard?.formattedDescription?.let { nnDesc ->
      nnDesc.entities?.forEach { nnEntity ->
        val newTitle = StringBuilder(nnEntity?.text.toString()).replaceRange(
          descriptionIndices[descIndex], descriptionIndices[descIndex] + 2, nnEntity?.text ?: ""
        )
        val titleSpannable = SpannableStringBuilder(newTitle)
        titleSpannable.setSpan(
          ForegroundColorSpan(Color.parseColor(nnEntity?.color)), descriptionIndices[descIndex],
          descriptionIndices[descIndex] + nnEntity?.text?.length!!,
          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        descIndex++
      }
    }
  }

  override fun getItemCount(): Int = cards.size
}

fun ImageView.loadImage(
  url: String,
  aspectRatio: Float
) {
  Glide.with(this)
    .load(url)
    .into(this)
}