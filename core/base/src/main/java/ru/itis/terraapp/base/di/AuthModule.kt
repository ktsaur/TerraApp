package ru.itis.terraapp.base.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.itis.terraapp.base.AuthManager.AuthManager

@Module
@InstallIn(SingletonComponent::class)
class AuthModule {

    @Provides
    fun provideAuthManager(@ApplicationContext context: Context): AuthManager = AuthManager(context)
}