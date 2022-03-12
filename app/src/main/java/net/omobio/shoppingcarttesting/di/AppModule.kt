package net.omobio.shoppingcarttesting.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.omobio.shoppingcarttesting.data.local.ShoppingDao
import net.omobio.shoppingcarttesting.data.local.ShoppingItemDatabase
import net.omobio.shoppingcarttesting.data.remote.PixabayAPI
import net.omobio.shoppingcarttesting.other.Constants.BASE_URL
import net.omobio.shoppingcarttesting.other.Constants.DATABASE_NAME
import net.omobio.shoppingcarttesting.repositories.DefaultShoppingRepository
import net.omobio.shoppingcarttesting.repositories.ShoppingRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideShoppingItemDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, ShoppingItemDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideShoppingDao(database: ShoppingItemDatabase) = database.shoppingDao()

    @Singleton
    @Provides
    fun providePixabayAPI(): PixabayAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PixabayAPI::class.java)
    }

    // Doing this cause Hilt won't know how to create ShoppingRepository in ViweModel
    @Singleton
    @Provides
    fun provideDefaultShoppingItemRepository(
        dao: ShoppingDao,
        pixabayAPI: PixabayAPI
    ) = DefaultShoppingRepository(dao, pixabayAPI) as ShoppingRepository

}