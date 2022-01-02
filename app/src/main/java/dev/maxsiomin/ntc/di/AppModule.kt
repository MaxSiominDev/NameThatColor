package dev.maxsiomin.ntc.di

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.maxsiomin.ntc.util.UiActions
import dev.maxsiomin.ntc.util.UiActionsImpl
import javax.inject.Singleton

/**
 * AppModule for DI
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideUiActions(@ApplicationContext context: Context): UiActions = UiActionsImpl(context)

    @Singleton
    @Provides
    fun provideAnalytics(): FirebaseAnalytics = Firebase.analytics
}

