package ru.itis.terraapp.auth.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.itis.terraapp.auth.utils.AuthManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AuthModule {

    @Provides
    fun provideAuthManager(@ApplicationContext context: Context): AuthManager = AuthManager(context)
}