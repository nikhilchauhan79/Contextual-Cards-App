package com.nikhilchauhan.contextual_cards.ui.adapters

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.adapters.ViewBindingAdapter.setOnLongClickListener
import androidx.recyclerview.widget.RecyclerView
import com.nikhilchauhan.contextual_cards.data.remote.responsemodel.CardsResponse.CardGroup.Card
import com.nikhilchauhan.contextual_cards.databinding.CardHc6Binding
import com.nikhilchauhan.contextual_cards.ui.adapters.Hc6Adapter.Hc6ViewHolder
import com.nikhilchauhan.contextual_cards.ui.callbacks.OnItemClickListener

class Hc6Adapter(
  private val cards: List<Card?>,
  private val titleSpans: List<SpannableStringBuilder>,
  private val descriptionSpans: List<SpannableStringBuilder>,
  private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<Hc6ViewHolder>() {

  inner class Hc6ViewHolder(val binding: CardHc6Binding) : RecyclerView.ViewHolder(binding.root){
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
      tvTitle.apply {
        movementMethod = LinkMovementMethod()
        text = titleSpans[position]
      }
      tvDescription.apply {
        movementMethod = LinkMovementMethod()
        text = descriptionSpans[position]
      }
      currentCard?.icon?.let { icon ->
        if (icon.imageUrl != null) {
          ivIconStart.loadImage(icon.imageUrl)
        }
      }

      currentCard?.bgColor?.let {
        cardViewHc6.setBackgroundColor(Color.parseColor(it))
      }
    }
  }

  override fun getItemCount(): Int = cards.size
}
