package com.nikhilchauhan.contextual_cards.data.remote.network

import com.nikhilchauhan.contextual_cards.data.remote.responsemodel.CardsResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
  @GET("04a04703-5557-4c84-a127-8c55335bb3b4")
  suspend fun getCards(): Response<CardsResponse>
}