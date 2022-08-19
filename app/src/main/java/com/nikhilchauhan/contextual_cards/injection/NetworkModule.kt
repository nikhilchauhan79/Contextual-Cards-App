package com.nikhilchauhan.contextual_cards.injection

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nikhilchauhan.contextual_cards.data.remote.network.ApiService
import com.nikhilchauhan.contextual_cards.utils.AppConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

  @Singleton
  @Provides
  fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient =
    OkHttpClient.Builder().addInterceptor(interceptor).build()

  @Singleton
  @Provides
  fun provideGson(): Gson = GsonBuilder().create()

  @Singleton
  @Provides
  fun provideLoggingInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
  }

  @Singleton
  @Provides
  fun provideRetrofit(
    okHttpClient: OkHttpClient,
    gson: Gson
  ): Retrofit = Retrofit.Builder()
    .baseUrl(AppConstants.BASE_URL)
    .addConverterFactory(GsonConverterFactory.create(gson))
    .client(okHttpClient)
    .build()

  @Singleton
  @Provides
  fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
}