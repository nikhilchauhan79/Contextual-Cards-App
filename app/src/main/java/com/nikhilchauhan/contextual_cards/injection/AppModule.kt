package com.nikhilchauhan.contextual_cards.injection

import android.content.Context
import android.content.SharedPreferences
import com.nikhilchauhan.contextual_cards.utils.AppConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

  @Singleton
  @Provides
  fun providePreferences(@ApplicationContext context: Context): SharedPreferences =
    context.getSharedPreferences(AppConstants.APP_SHARED_PREF, Context.MODE_PRIVATE)
}