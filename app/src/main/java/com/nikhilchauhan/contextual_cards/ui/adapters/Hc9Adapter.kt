package com.nikhilchauhan.contextual_cards.ui.adapters

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nikhilchauhan.contextual_cards.data.remote.responsemodel.CardsResponse.CardGroup.Card
import com.nikhilchauhan.contextual_cards.databinding.CardHc9Binding
import com.nikhilchauhan.contextual_cards.ui.adapters.Hc9Adapter.Hc9ViewHolder
import com.nikhilchauhan.contextual_cards.ui.callbacks.OnItemClickListener

class Hc9Adapter(
  private val cards: List<Card?>,
  private val titleSpans: List<SpannableStringBuilder>,
  private val descriptionSpans: List<SpannableStringBuilder>,
  private val onItemClickListener: OnItemClickListener,
  private val customWidth: Int
) : RecyclerView.Adapter<Hc9ViewHolder>() {

  inner class Hc9ViewHolder(val binding: CardHc9Binding) : RecyclerView.ViewHolder(binding.root) {
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
  ): Hc9ViewHolder {
    val binding = CardHc9Binding.inflate(LayoutInflater.from(parent.context), parent, false)
    return Hc9ViewHolder(binding)
  }

  override fun onBindViewHolder(
    holder: Hc9ViewHolder,
    position: Int
  ) {
    with(holder.binding) {
      Log.d("TAG", "onBindViewHolder:" + customWidth)
      cardViewHc9.layoutParams.width = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, customWidth.toFloat(), holder.itemView.resources.displayMetrics
      ).toInt()
      val currentCard = cards[position]
      currentCard?.bgImage?.let { bgImage ->
        if (bgImage.imageUrl != null) {
          ivHc9.loadImage(bgImage.imageUrl)
        }
      }
      currentCard?.bgColor?.let {
        cardViewHc9.setBackgroundColor(Color.parseColor(it))
      }
    }
  }

  override fun getItemCount(): Int = cards.size
}
