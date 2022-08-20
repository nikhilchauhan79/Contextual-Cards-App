package com.nikhilchauhan.contextual_cards.ui.adapters

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nikhilchauhan.contextual_cards.data.remote.responsemodel.CardsResponse.CardGroup.Card
import com.nikhilchauhan.contextual_cards.databinding.CardHc1Binding
import com.nikhilchauhan.contextual_cards.ui.adapters.Hc1Adapter.Hc1ViewHolder

class Hc1Adapter(
  private val cards: List<Card?>,
  private val titleSpans: List<SpannableStringBuilder>,
  private val descriptionSpans: List<SpannableStringBuilder>,
) : RecyclerView.Adapter<Hc1ViewHolder>() {

  inner class Hc1ViewHolder(val binding: CardHc1Binding) : RecyclerView.ViewHolder(binding.root)

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): Hc1ViewHolder {
    val binding = CardHc1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
    return Hc1ViewHolder(binding)
  }

  override fun onBindViewHolder(
    holder: Hc1ViewHolder,
    position: Int
  ) {
    with(holder.binding) {
      val currentCard = cards[position]
      tvTitle.text = currentCard?.name
      tvBody.text = titleSpans[position]
      currentCard?.icon?.let { icon ->
        if (icon.imageUrl != null) {
          ivStart.loadImage(icon.imageUrl)
        }
      }
      currentCard?.bgColor?.let {
        cardView.setBackgroundColor(Color.parseColor(it))
      }
    }
  }

  override fun getItemCount(): Int = cards.size
}
