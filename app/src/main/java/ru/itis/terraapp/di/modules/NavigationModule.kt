package ru.itis.terraapp.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.itis.terraapp.navigation.NavigationManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

class NavigationModule {

    @Provides
    @Singleton
    fun provideNavigationManager(): NavigationManager = NavigationManager()

}