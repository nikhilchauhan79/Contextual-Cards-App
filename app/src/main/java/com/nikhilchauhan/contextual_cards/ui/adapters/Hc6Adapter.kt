package com.nikhilchauhan.contextual_cards.ui.adapters

import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nikhilchauhan.contextual_cards.data.remote.responsemodel.CardsResponse.CardGroup.Card
import com.nikhilchauhan.contextual_cards.databinding.CardHc6Binding
import com.nikhilchauhan.contextual_cards.ui.adapters.Hc6Adapter.Hc6ViewHolder

class Hc6Adapter(
  private val cards: List<Card?>,
  private val titleSpans: List<SpannableStringBuilder>,
  private val descriptionSpans: List<SpannableStringBuilder>,
) : RecyclerView.Adapter<Hc6ViewHolder>() {

  inner class Hc6ViewHolder(val binding: CardHc6Binding) : RecyclerView.ViewHolder(binding.root)

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): Hc6ViewHolder {
    val binding = CardHc6Binding.inflate(LayoutInflater.from(parent.context), parent, false)
    return Hc6ViewHolder(binding)
  }

  override fun onBindViewHolder(
    holder: Hc6ViewHolder,
    position: Int
  ) {
    with(holder.binding) {
      val currentCard = cards[position]
      tvTitle.text = titleSpans[position]
      tvDescription.text = descriptionSpans[position]
      currentCard?.icon?.let { icon ->
        if (icon.imageUrl != null) {
          ivIconStart.loadImage(icon.imageUrl)
        }
      }
    }
  }

  override fun getItemCount(): Int = cards.size
}
