package net.omobio.shoppingcarttesting.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.omobio.shoppingcarttesting.data.local.ShoppingItemDatabase
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Named("test_db")
    fun providesInMemoryDb(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(context, ShoppingItemDatabase::class.java)
            .allowMainThreadQueries()
            .build()
}