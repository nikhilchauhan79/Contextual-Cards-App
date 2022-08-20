package com.nikhilchauhan.contextual_cards.ui.adapters

import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nikhilchauhan.contextual_cards.data.remote.responsemodel.CardsResponse.CardGroup.Card
import com.nikhilchauhan.contextual_cards.databinding.CardHc9Binding
import com.nikhilchauhan.contextual_cards.ui.adapters.Hc9Adapter.Hc9ViewHolder

class Hc9Adapter(
  private val cards: List<Card?>,
  private val titleSpans: List<SpannableStringBuilder>,
  private val descriptionSpans: List<SpannableStringBuilder>,
) : RecyclerView.Adapter<Hc9ViewHolder>() {

  inner class Hc9ViewHolder(val binding: CardHc9Binding) : RecyclerView.ViewHolder(binding.root)

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): Hc9ViewHolder {
    val binding = CardHc9Binding.inflate(LayoutInflater.from(parent.context), parent, false)
    return Hc9ViewHolder(binding)
  }

  override fun onBindViewHolder(
    holder: Hc9ViewHolder,
    position: Int
  ) {
    with(holder.binding) {
      val currentCard = cards[position]
      currentCard?.bgImage?.let { bgImage ->
        if (bgImage.imageUrl != null) {
          ivHc9.loadImage(bgImage.imageUrl)
        }
      }
    }
  }

  override fun getItemCount(): Int = cards.size
}
