package com.nikhilchauhan.contextual_cards.ui.adapters

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nikhilchauhan.contextual_cards.data.remote.responsemodel.CardsResponse
import com.nikhilchauhan.contextual_cards.data.remote.responsemodel.CardsResponse.CardGroup.Card
import com.nikhilchauhan.contextual_cards.data.remote.responsemodel.CardsResponse.CardGroup.Card.FormattedDescription
import com.nikhilchauhan.contextual_cards.data.remote.responsemodel.CardsResponse.CardGroup.Card.FormattedTitle
import com.nikhilchauhan.contextual_cards.databinding.CardHc3Binding
import com.nikhilchauhan.contextual_cards.ui.adapters.Hc3Adapter.Hc3ViewHolder

class Hc3Adapter(
  private val cards: List<CardsResponse.CardGroup.Card?>
) : RecyclerView.Adapter<Hc3ViewHolder>() {

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
      val formattedTitle = currentCard?.formattedTitle
      setTitleSpans(formattedTitle)
      setDescriptionSpans(currentCard?.formattedDescription)
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
  }

  private fun CardHc3Binding.setTitleSpans(formattedTitle: FormattedTitle?) {
    formattedTitle?.let { nnTitle ->
      val spannable = SpannableString(nnTitle.text)
      nnTitle.entities?.forEach {
        spannable.setSpan(
          ForegroundColorSpan(Color.parseColor(it?.color)), spannable.getSpanStart(it?.text),
          spannable.getSpanEnd(it?.text), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
      }
      tvBodyText.text = spannable
    }
  }

  private fun CardHc3Binding.setDescriptionSpans(description: FormattedDescription?) {
    description?.let { nnDescription ->
      val spannable = SpannableString(nnDescription.text)
      nnDescription.entities?.forEach {
        spannable.setSpan(
          ForegroundColorSpan(Color.parseColor(it?.color)), spannable.getSpanStart(it?.text),
          spannable.getSpanEnd(it?.text), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
      }
      tvBodyText.text = spannable
    }
  }

  override fun getItemCount(): Int {
    TODO("Not yet implemented")
  }
}

fun ImageView.loadImage(
  url: String,
  aspectRatio: Float
) {
  Glide.with(this)
    .load(url)
    .into(this)
}