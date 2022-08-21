package com.nikhilchauhan.contextual_cards.ui.adapters

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nikhilchauhan.contextual_cards.data.remote.responsemodel.CardsResponse.CardGroup.Card
import com.nikhilchauhan.contextual_cards.databinding.CardHc1Binding
import com.nikhilchauhan.contextual_cards.ui.adapters.Hc1Adapter.Hc1ViewHolder
import com.nikhilchauhan.contextual_cards.ui.callbacks.OnItemClickListener

class Hc1Adapter(
  private val cards: List<Card?>,
  private val titleSpans: List<SpannableStringBuilder>,
  private val descriptionSpans: List<SpannableStringBuilder>,
  private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<Hc1ViewHolder>() {

  inner class Hc1ViewHolder(val binding: CardHc1Binding) : RecyclerView.ViewHolder(binding.root) {
    init {
      binding.root.apply {
        setOnClickListener {
          cards[layoutPosition]?.url?.let { nnUrl ->
            onItemClickListener.onItemClick(layoutPosition, nnUrl)
          }
        }
      }
    }
  }

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
      tvTitle.apply {
        movementMethod = LinkMovementMethod()
        text = currentCard?.name
      }
      tvBody.apply {
        movementMethod = LinkMovementMethod()
        text = titleSpans[position]
      }
      currentCard?.icon?.let { icon ->
        if (icon.imageUrl != null) {
          ivStart.loadImage(icon.imageUrl)
        }
      }
      currentCard?.bgColor?.let {
        cardViewHc1.setBackgroundColor(Color.parseColor(it))
      }
      currentCard?.bgImage?.let {

      }
    }
  }

  override fun getItemCount(): Int = cards.size
}
