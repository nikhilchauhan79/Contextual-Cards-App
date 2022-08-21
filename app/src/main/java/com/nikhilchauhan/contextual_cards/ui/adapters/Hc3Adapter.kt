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
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.nikhilchauhan.contextual_cards.data.remote.responsemodel.CardsResponse.CardGroup.Card
import com.nikhilchauhan.contextual_cards.databinding.CardContextHc3Binding
import com.nikhilchauhan.contextual_cards.databinding.CardHc3Binding
import com.nikhilchauhan.contextual_cards.ui.callbacks.OnItemClickListener
import com.nikhilchauhan.contextual_cards.utils.Utils.hide
import com.nikhilchauhan.contextual_cards.utils.Utils.show

class Hc3Adapter(
  private val cards: MutableList<Card?>,
  private val titleSpans: List<SpannableStringBuilder>,
  private val descriptionSpans: List<SpannableStringBuilder>,
  private val onItemClickListener: OnItemClickListener,
  private val showMenuList: MutableList<Boolean>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private val SHOW_MENU = 1
  private val HIDE_MENU = 2

  inner class Hc3ViewHolder(val binding: CardHc3Binding) : RecyclerView.ViewHolder(binding.root) {
    init {
      binding.root.apply {
        setOnLongClickListener {
          onItemClickListener.onLongPress(layoutPosition, it)
          showMenu(layoutPosition)
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

  inner class Hc3MenuViewHolder(val binding: CardContextHc3Binding) : RecyclerView.ViewHolder(
    binding.root
  ) {
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
      binding.apply {
        btnThreeCard.setOnClickListener {
          cards[layoutPosition]?.cta?.get(0)?.url?.let { nnUrl ->
            onItemClickListener.onItemClick(layoutPosition, nnUrl)
          }
        }
        cvRemindLater.setOnClickListener {
          cards.removeAt(layoutPosition)
          notifyItemRemoved(layoutPosition)
          onItemClickListener.onRemindLaterClick(layoutPosition)
        }
        cvDismissNow.setOnClickListener {
          cards.removeAt(layoutPosition)
          notifyItemRemoved(layoutPosition)
          onItemClickListener.onDismissNowClick(layoutPosition)
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
  ): RecyclerView.ViewHolder {
    val binding = if (viewType == SHOW_MENU) {
      CardContextHc3Binding.inflate(LayoutInflater.from(parent.context), parent, false)
    } else {
      CardHc3Binding.inflate(LayoutInflater.from(parent.context), parent, false)
    }
    return if (binding is CardHc3Binding) {
      Hc3ViewHolder(binding)
    } else {
      Hc3MenuViewHolder(binding as CardContextHc3Binding)
    }
  }

  override fun getItemViewType(position: Int): Int {
    return if (showMenuList[position]) SHOW_MENU else HIDE_MENU
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

  private fun CardContextHc3Binding.setButtonsStyle(currentCard: Card?) {
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
  override fun onBindViewHolder(
    holder: ViewHolder,
    position: Int
  ) {
    if (holder is Hc3ViewHolder) {
      with(holder.binding) {
        showMenuList.add(position, false)
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
          constraintHc3.setBackgroundColor(Color.parseColor(it))
        }
        setButtonsStyle(currentCard)
      }
    } else if (holder is Hc3MenuViewHolder) {
      with(holder.binding) {
        showMenuList.add(position, false)
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
          constraintHc3.setBackgroundColor(Color.parseColor(it))
        }
        setButtonsStyle(currentCard)
      }
    }
  }

  fun showMenu(position: Int) {
    for (i in 0 until showMenuList.size) {
      showMenuList[i] = false
    }
    showMenuList[position] = true
    notifyDataSetChanged()
  }

  fun isMenuShown(): Boolean {
    for (i in 0 until showMenuList.size) {
      if (showMenuList[i]) {
        return true
      }
    }
    return false
  }

  fun closeMenu() {
    for (i in 0 until showMenuList.size) {
      showMenuList[i] = false
    }
    notifyDataSetChanged()
  }
}

fun ImageView.loadImage(
  url: String,
  aspectRatio: Float = 0.91f
) {
  Glide.with(this)
    .load(url)
    .into(this)
}