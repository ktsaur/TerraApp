package ru.itis.terraapp.feature.mainscreen.impl.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.itis.terraapp.feature.mainscreen.api.MainScreenFeature
import ru.itis.terraapp.feature.mainscreen.impl.MainScreenFeatureImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainScreenModule {
    
    @Provides
    @Singleton
    fun provideMainScreenFeature(): MainScreenFeature {
        return MainScreenFeatureImpl()
    }
}

