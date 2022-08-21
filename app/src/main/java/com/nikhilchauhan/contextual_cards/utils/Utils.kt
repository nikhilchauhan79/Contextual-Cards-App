package com.nikhilchauhan.contextual_cards.utils

import android.view.View

object Utils {
  fun View.show(){
    visibility = View.VISIBLE
  }


  fun View.hide(){
    visibility = View.GONE
  }
}