package com.nikhilchauhan.contextual_cards.data.remote

import com.nikhilchauhan.contextual_cards.data.remote.responsemodel.CardsResponse

sealed class NetworkResult<T>(
  val data: T? = null,
  val message: String? = null
) {
  object INIT : NetworkResult<CardsResponse>() {

  }

  class Success<T>(data: T) : NetworkResult<T>(data)
  class Error<T>(
    message: String,
    data: T? = null
  ) : NetworkResult<T>(data, message)

  class InProgress<T> : NetworkResult<T>()
}