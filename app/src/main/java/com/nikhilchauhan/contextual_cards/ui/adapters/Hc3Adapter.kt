package com.nikhilchauhan.contextual_cards.ui.adapters

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.URLSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nikhilchauhan.contextual_cards.data.remote.responsemodel.CardsResponse.CardGroup.Card
import com.nikhilchauhan.contextual_cards.databinding.CardHc3Binding
import com.nikhilchauhan.contextual_cards.ui.adapters.Hc3Adapter.Hc3ViewHolder
import com.nikhilchauhan.contextual_cards.ui.callbacks.OnItemClickListener
import com.nikhilchauhan.contextual_cards.utils.Utils.hide
import com.nikhilchauhan.contextual_cards.utils.Utils.show

class Hc3Adapter(
  private val cards: List<Card?>,
  private val titleSpans: List<SpannableStringBuilder>,
  private val descriptionSpans: List<SpannableStringBuilder>,
  private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<Hc3ViewHolder>() {

  inner class Hc3ViewHolder(val binding: CardHc3Binding) : RecyclerView.ViewHolder(binding.root) {
    init {
      binding.root.apply {
        setOnLongClickListener {
          onItemClickListener.onLongPress(layoutPosition, it)
          // showContextMenu(layoutPosition)
          true
        }

        setOnClickListener {
          cards[layoutPosition]?.url?.let { nnUrl ->
            onItemClickListener.onItemClick(layoutPosition, nnUrl)
          }
        }
      }
      binding.btnThreeCard.setOnClickListener {
        cards[layoutPosition]?.cta?.get(0)?.url?.let { nnUrl ->
          onItemClickListener.onItemClick(layoutPosition, nnUrl)
        }
      }
    }

    fun showContextMenu(position: Int) {
      binding.llContextMenu.show()
    }

    fun hideContextMenu(position: Int) {
      binding.llContextMenu.hide()
    }
  }

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
      tvHeading.apply {
        movementMethod = LinkMovementMethod()
        text = titleSpans[position]

      }
      tvBodyText.apply {
        movementMethod = LinkMovementMethod()
        text = descriptionSpans[position]
      }
      currentCard?.bgImage?.let { bgImage ->
        if (bgImage.imageUrl != null) {
          ivThreeCard.loadImage(bgImage.imageUrl)
        }
      }
      currentCard?.bgColor?.let {
        cardViewHc3.setBackgroundColor(Color.parseColor(it))
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
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
          )
          buttonTextSpan.setSpan(
            URLSpan(currentCard.url), 0, it,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
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