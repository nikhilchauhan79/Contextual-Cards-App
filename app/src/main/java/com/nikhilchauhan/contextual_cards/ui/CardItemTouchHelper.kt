package com.nikhilchauhan.contextual_cards.ui

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.nikhilchauhan.contextual_cards.data.remote.responsemodel.CardsResponse.CardGroup.Card
import com.nikhilchauhan.contextual_cards.databinding.ActivityCardsBinding
import com.nikhilchauhan.contextual_cards.ui.adapters.Hc3Adapter

class CardItemTouchHelper(
  private val cards: MutableList<Card?>,
  private val hc3Adapter: Hc3Adapter,
  private val binding: ActivityCardsBinding
) : ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
  0, ItemTouchHelper.RIGHT
) {
  override fun onMove(
    recyclerView: RecyclerView,
    viewHolder: RecyclerView.ViewHolder,
    target: RecyclerView.ViewHolder
  ): Boolean {
    // this method is called
    // when the item is moved.
    return false
  }

  override fun onSwiped(
    viewHolder: RecyclerView.ViewHolder,
    direction: Int
  ) {
    // this method is called when we swipe our item to right direction.
    // on below line we are getting the item at a particular position.
    val deletedCard =
      cards[viewHolder.adapterPosition]

    // below line is to get the position
    // of the item at that position.
    val position = viewHolder.adapterPosition

    // this method is called when item is swiped.
    // below line is to remove item from our array list.
    cards.removeAt(viewHolder.adapterPosition)

    // below line is to notify our item is removed from adapter.
    hc3Adapter.notifyItemRemoved(viewHolder.adapterPosition)

    Snackbar.make(binding.rvHc3, "Deleted " + deletedCard?.title, Snackbar.LENGTH_LONG).show()
  }
})