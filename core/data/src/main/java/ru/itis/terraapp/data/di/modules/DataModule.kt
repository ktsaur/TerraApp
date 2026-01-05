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
import retrofit2.converter.gson.GsonConverterFactory
import ru.itis.terraapp.data.database.InceptionDatabase
import ru.itis.terraapp.data.database.dao.UserDao
import ru.itis.terraapp.data.database.repository.UserRepositoryImpl
import ru.itis.terraapp.data.di.qualifier.IoDispatchers
import ru.itis.terraapp.domain.repositories.UserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    /*@Provides
    fun provideOkHttpCleint(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AppIdInterceptor())
            .addInterceptor(MetricInterceptor())
            .build()
    }*/

    /*@Provides
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
    }*/

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

    @IoDispatchers
    @Provides
    @Singleton
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
