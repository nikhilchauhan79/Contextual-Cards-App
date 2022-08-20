package com.nikhilchauhan.contextual_cards.ui.adapters

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
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
  private val titleSpans: List<SpannableStringBuilder>,
  private val descriptionSpans: List<SpannableStringBuilder>,
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
      tvHeading.text = titleSpans[position]
      tvBodyText.text = descriptionSpans[position]
      currentCard?.bgImage?.let { bgImage ->
        if (bgImage.imageUrl != null) {
          ivThreeCard.loadImage(bgImage.imageUrl)
        }
      }
      setButtonsStyle(currentCard)
    }
  }

  private fun CardHc3Binding.setButtonsStyle(currentCard: Card?) {
    currentCard?.cta?.get(0)?.let { cta ->
      btnThreeCard.apply {
        val buttonTextSpan = SpannableString(cta.text)
        cta.text?.length?.let {
          buttonTextSpan.setSpan(
            ForegroundColorSpan(Color.parseColor(cta.textColor)), 0, it,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
          )
        }
        text = buttonTextSpan
        setBackgroundColor(Color.parseColor(cta.bgColor))
      }
    }
  }

  override fun getItemCount(): Int = cards.size
}

fun ImageView.loadImage(
  url: String,
  aspectRatio: Float = 0.91f
) {
  Glide.with(this)
    .load(url)
    .into(this)
}