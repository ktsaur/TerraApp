package ru.itis.terraapp.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.itis.terraapp.auth.utils.AuthManager
import ru.itis.terraapp.navigation.NavigationManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

class PresentationModule {

    @Provides
    fun provideAuthManager(@ApplicationContext context: Context): AuthManager = AuthManager(context)

    @Provides
    @Singleton
    fun provideNavigationManager(): NavigationManager = NavigationManager()

}