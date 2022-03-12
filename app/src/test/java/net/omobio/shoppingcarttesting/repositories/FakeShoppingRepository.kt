package net.omobio.shoppingcarttesting.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import net.omobio.shoppingcarttesting.data.local.ShoppingItem
import net.omobio.shoppingcarttesting.data.remote.responses.ImageResponse
import net.omobio.shoppingcarttesting.other.APIResponse

/**
 * Fake Repository class to simulate it's actual counter part
 * Implementing Interface to have the same actionable methods
 * Created this to avoid calling APIs from our testing classes or methods
 */
class FakeShoppingRepository : ShoppingRepository {

    private val shoppingItems = mutableListOf<ShoppingItem>()

    private val observableShoppingItems = MutableLiveData<List<ShoppingItem>>(shoppingItems)

    private val observableTotalPrice = MutableLiveData<Float>()

    private var shouldReturnNetworkError = false

    fun shouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    private fun refreshLiveData() {
        observableShoppingItems.postValue(shoppingItems)
        observableTotalPrice.postValue(getTotalPrice())
    }

    private fun getTotalPrice(): Float {
        return shoppingItems.sumOf { it.price.toDouble() }.toFloat()
    }

    override suspend fun insertShoppingItem(item: ShoppingItem) {
        shoppingItems.add(item)
        refreshLiveData()
    }

    override suspend fun deleteShoppingItem(item: ShoppingItem) {
        shoppingItems.remove(item)
        refreshLiveData()
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return observableShoppingItems
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return observableTotalPrice
    }

    override suspend fun searchForImage(imageQuery: String): APIResponse<ImageResponse> {
        return if (shouldReturnNetworkError) {
            APIResponse.error(null, "Error Occurred!")
        } else {
            APIResponse.success(ImageResponse(listOf(), 0, 0))
        }
    }

}