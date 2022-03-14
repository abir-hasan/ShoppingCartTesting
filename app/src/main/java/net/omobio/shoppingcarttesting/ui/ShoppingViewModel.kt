package net.omobio.shoppingcarttesting.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.omobio.shoppingcarttesting.data.local.ShoppingItem
import net.omobio.shoppingcarttesting.data.remote.responses.ImageResponse
import net.omobio.shoppingcarttesting.other.Constants
import net.omobio.shoppingcarttesting.other.Event
import net.omobio.shoppingcarttesting.other.Resource
import net.omobio.shoppingcarttesting.repositories.ShoppingRepository
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val repository: ShoppingRepository
) : ViewModel() {

    val shoppingItems = repository.observeAllShoppingItems()

    val totalPrice = repository.observeTotalPrice()

    private val _imagesLiveData = MutableLiveData<Event<Resource<ImageResponse>>>()
    val imagesLiveData: LiveData<Event<Resource<ImageResponse>>> = _imagesLiveData

    private val _currentImageUrlLiveData = MutableLiveData<String>()
    val currentImageUrlLiveData: LiveData<String> = _currentImageUrlLiveData

    private val _insertShoppingItemStatus = MutableLiveData<Event<Resource<ShoppingItem>>>()
    val insertShoppingItemStatus: LiveData<Event<Resource<ShoppingItem>>> =
        _insertShoppingItemStatus

    fun setCurrentImageUrl(url: String) {
        _currentImageUrlLiveData.postValue(url)
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.deleteShoppingItem(shoppingItem)
    }

    fun insertShoppingItemInDb(shoppingItem: ShoppingItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertShoppingItem(shoppingItem)
    }

    fun insertShoppingItem(name: String, amountString: String, priceString: String) {
        if (name.isEmpty() || amountString.isEmpty() || priceString.isEmpty()) {
            _insertShoppingItemStatus.postValue(
                Event(Resource.error(null, "The fields must not be empty!"))
            )
            return
        }
        if (name.length > Constants.MAX_NAME_LENGTH) {
            _insertShoppingItemStatus.postValue(
                Event(
                    Resource.error(
                        null, "The name of the item must not exceed " +
                                "${Constants.MAX_NAME_LENGTH} characters"
                    )
                )
            )
            return
        }
        if (priceString.length > Constants.MAX_PRICE_LENGTH) {
            _insertShoppingItemStatus.postValue(
                Event(
                    Resource.error(
                        null, "The price of the item must not exceed " +
                                "${Constants.MAX_PRICE_LENGTH} characters"
                    )
                )
            )
            return
        }
        val amount = try {
            amountString.toInt()
        } catch (e: Exception) {
            _insertShoppingItemStatus.postValue(
                Event(Resource.error(null, "Please enter a valid amount"))
            )
            return
        }
        val shoppingItem = ShoppingItem(
            name, amount, priceString.toFloat(), _currentImageUrlLiveData.value ?: ""
        )
        insertShoppingItemInDb(shoppingItem)
        setCurrentImageUrl("")
        _insertShoppingItemStatus.postValue(Event(Resource.success(shoppingItem)))
    }

    fun searchForImage(query: String) {

    }

}