package net.omobio.shoppingcarttesting.repositories

import androidx.lifecycle.LiveData
import net.omobio.shoppingcarttesting.data.local.ShoppingItem
import net.omobio.shoppingcarttesting.data.remote.responses.ImageResponse
import net.omobio.shoppingcarttesting.other.Resource

interface ShoppingRepository {

    suspend fun insertShoppingItem(item: ShoppingItem)

    suspend fun deleteShoppingItem(item: ShoppingItem)

    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>

    fun observeTotalPrice(): LiveData<Float>

    suspend fun searchForImage(imageQuery: String): Resource<ImageResponse>
}