package com.nikhilchauhan.contextual_cards.ui.callbacks

import android.view.View

interface OnItemClickListener {
  fun onLongPress(
    position: Int,
    view: View
  )

  fun onItemClick(
    position: Int,
    url: String
  )
}