package net.omobio.shoppingcarttesting.repositories

import androidx.lifecycle.LiveData
import net.omobio.shoppingcarttesting.data.local.ShoppingDao
import net.omobio.shoppingcarttesting.data.local.ShoppingItem
import net.omobio.shoppingcarttesting.data.remote.PixabayAPI
import net.omobio.shoppingcarttesting.data.remote.responses.ImageResponse
import net.omobio.shoppingcarttesting.other.APIResponse
import javax.inject.Inject

class DefaultShoppingRepository @Inject constructor(
    private val shoppingDao: ShoppingDao,
    private val pixabayAPI: PixabayAPI
) : ShoppingRepository {

    override suspend fun insertShoppingItem(item: ShoppingItem) {
        shoppingDao.insertShoppingItem(item)
    }

    override suspend fun deleteShoppingItem(item: ShoppingItem) {
        shoppingDao.deleteShoppingItem(item)
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return shoppingDao.observeAllShoppingItems()
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return shoppingDao.observeTotalPrice()
    }

    override suspend fun searchForImage(imageQuery: String): APIResponse<ImageResponse> {
        return try {
            val response = pixabayAPI.searchForImage(imageQuery)
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    APIResponse.success(it)
                } ?: run {
                    APIResponse.error(null, "Unknown Error Occurred!")
                }
            } else {
                APIResponse.error(null, "Unknown Error Occurred!")
            }

        } catch (e: Exception) {
            APIResponse.error(null, "Couldn't Reach the server. Check Internet!")
        }
    }
}