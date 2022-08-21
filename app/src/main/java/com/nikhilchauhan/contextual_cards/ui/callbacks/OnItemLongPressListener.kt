package com.nikhilchauhan.contextual_cards.ui.callbacks

import android.view.View

interface OnItemLongPressListener {
  fun onLongPress(
    position: Int,
    view: View
  )
}