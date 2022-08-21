package com.nikhilchauhan.contextual_cards.ui.adapters

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nikhilchauhan.contextual_cards.data.remote.responsemodel.CardsResponse.CardGroup.Card
import com.nikhilchauhan.contextual_cards.databinding.CardHc5Binding
import com.nikhilchauhan.contextual_cards.ui.adapters.Hc5Adapter.Hc5ViewHolder

class Hc5Adapter(
  private val cards: List<Card?>,
  private val titleSpans: List<SpannableStringBuilder>,
  private val descriptionSpans: List<SpannableStringBuilder>,
) : RecyclerView.Adapter<Hc5ViewHolder>() {

  inner class Hc5ViewHolder(val binding: CardHc5Binding) : RecyclerView.ViewHolder(binding.root)

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): Hc5ViewHolder {
    val binding = CardHc5Binding.inflate(LayoutInflater.from(parent.context), parent, false)
    return Hc5ViewHolder(binding)
  }

  override fun onBindViewHolder(
    holder: Hc5ViewHolder,
    position: Int
  ) {
    with(holder.binding) {
      val currentCard = cards[position]
      // tvTitle.text = currentCard?.name
      // tvDescription.text = descriptionSpans[position]
      currentCard?.bgImage?.let { bgImage ->
        if (bgImage.imageUrl != null) {
          ivIconStart.loadImage(bgImage.imageUrl)
        }
      }
      currentCard?.bgColor?.let {
        cardViewHc5.setBackgroundColor(Color.parseColor(it))
      }
    }
  }

  override fun getItemCount(): Int = cards.size
}
