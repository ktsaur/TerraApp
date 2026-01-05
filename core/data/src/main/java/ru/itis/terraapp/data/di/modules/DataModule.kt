package ru.itis.terraapp.data.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.itis.terraapp.data.database.InceptionDatabase
import ru.itis.terraapp.data.database.dao.UserDao
import ru.itis.terraapp.data.database.repository.UserRepositoryImpl
import ru.itis.terraapp.data.di.qualifier.IoDispatchers
import ru.itis.terraapp.data.remote.AttractionsApi
import ru.itis.terraapp.data.remote.OpenWeatherApi
import ru.itis.terraapp.data.remote.interceptors.AppIdInterceptor
import ru.itis.terraapp.data.remote.interceptors.MetricInterceptor
import ru.itis.terraapp.data.repository.AttractionsRepositoryImpl
import ru.itis.terraapp.data.repository.ForecastRepositoryImpl
import ru.itis.terraapp.data.repository.WeatherRepositoryImpl
import ru.itis.terraapp.domain.repositories.AttractionsRepository
import ru.itis.terraapp.domain.repositories.ForecastRepository
import ru.itis.terraapp.domain.repositories.UserRepository
import ru.itis.terraapp.domain.repositories.WeatherRepository
import javax.inject.Singleton
import ru.itis.terraapp.data.BuildConfig.MOCKOON_API_URL
import ru.itis.terraapp.data.BuildConfig.OPEN_WEATHER_API_URL

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    fun provideOkHttpCleint(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AppIdInterceptor())
            .addInterceptor(MetricInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideOpenWeatherApi(
        okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory
    ): OpenWeatherApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(OPEN_WEATHER_API_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
        return retrofit.create(OpenWeatherApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAttractionsApi(
        converterFactory: GsonConverterFactory
    ): AttractionsApi {
        // Для Mockoon используем отдельный OkHttpClient без интерцепторов
        val okHttpClient = OkHttpClient.Builder().build()
        val retrofit = Retrofit.Builder()
            .baseUrl(MOCKOON_API_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
        return retrofit.create(AttractionsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabaseInstance(@ApplicationContext ctx: Context): InceptionDatabase {
        return Room.databaseBuilder(ctx, InceptionDatabase::class.java, DATABASE_NAME)
            .build()
    }

    companion object {
        private const val DATABASE_NAME = "InceptionDB"
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        userDao: UserDao,
        @IoDispatchers ioDispatchers: CoroutineDispatcher
    ) : UserRepository {
        return UserRepositoryImpl(userDao = userDao, ioDispatchers = ioDispatchers)
    }

    @Provides
    @Singleton
    fun provideUserDao(database: InceptionDatabase) : UserDao = database.userDao

    @Provides
    @Singleton
    fun provideWeatherRepository(
        openWeatherApi: OpenWeatherApi
    ): WeatherRepository {
        return WeatherRepositoryImpl(openWeatherApi = openWeatherApi)
    }

    @Provides
    @Singleton
    fun provideForecastRepository(
        openWeatherApi: OpenWeatherApi
    ): ForecastRepository {
        return ForecastRepositoryImpl(openWeatherApi = openWeatherApi)
    }

    @Provides
    @Singleton
    fun provideAttractionsRepository(
        attractionsApi: AttractionsApi
    ): AttractionsRepository {
        return AttractionsRepositoryImpl(attractionsApi = attractionsApi)
    }

    @IoDispatchers
    @Provides
    @Singleton
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
