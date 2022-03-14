package net.omobio.shoppingcarttesting.repositories

import androidx.lifecycle.LiveData
import net.omobio.shoppingcarttesting.data.local.ShoppingDao
import net.omobio.shoppingcarttesting.data.local.ShoppingItem
import net.omobio.shoppingcarttesting.data.remote.PixabayAPI
import net.omobio.shoppingcarttesting.data.remote.responses.ImageResponse
import net.omobio.shoppingcarttesting.other.Resource
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

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
        return try {
            val response = pixabayAPI.searchForImage(imageQuery)
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    Resource.success(it)
                } ?: run {
                    Resource.error(null, "Unknown Error Occurred!")
                }
            } else {
                Resource.error(null, "Unknown Error Occurred!")
            }

        } catch (e: Exception) {
            Resource.error(null, "Couldn't Reach the server. Check Internet!")
        }
    }
}