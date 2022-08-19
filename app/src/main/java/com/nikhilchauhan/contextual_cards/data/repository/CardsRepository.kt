package com.nikhilchauhan.contextual_cards.data.repository

import com.nikhilchauhan.contextual_cards.data.remote.BaseApiResponse
import com.nikhilchauhan.contextual_cards.data.remote.NetworkResult
import com.nikhilchauhan.contextual_cards.data.remote.RemoteDataSource
import com.nikhilchauhan.contextual_cards.data.remote.responsemodel.CardsResponse
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ActivityRetainedScoped
class CardsRepository @Inject constructor(
  private val remoteDataSource: RemoteDataSource
) : BaseApiResponse() {

  suspend fun getCards(): Flow<NetworkResult<CardsResponse>> = flow {
    emit(safeApiCall {
      remoteDataSource.getCards()
    })
  }.flowOn(Dispatchers.IO)
}