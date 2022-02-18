package net.omobio.shoppingcarttesting.data.local

import androidx.room.*
import kotlinx.coroutines.flow.StateFlow

@Dao
interface ShoppingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    @Delete
    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    @Query("SELECT * FROM shopping_items")
    fun observeAllShoppingItems(): StateFlow<List<ShoppingItem>>

    @Query("SElECT SUM(price * amount) from shopping_items")
    fun observeTotalPrice():StateFlow<Float>
}