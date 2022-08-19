package com.nikhilchauhan.contextual_cards.data.remote

import com.nikhilchauhan.contextual_cards.data.remote.network.ApiService
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
  private val apiService: ApiService
) {
  suspend fun getCards() = apiService.getCards()
}