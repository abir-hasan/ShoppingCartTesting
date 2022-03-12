package net.omobio.shoppingcarttesting.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import net.omobio.shoppingcarttesting.data.local.ShoppingItem
import net.omobio.shoppingcarttesting.data.remote.responses.ImageResponse
import net.omobio.shoppingcarttesting.data.remote.responses.ImageResult
import net.omobio.shoppingcarttesting.other.APIResponse
import net.omobio.shoppingcarttesting.other.Event
import net.omobio.shoppingcarttesting.repositories.ShoppingRepository
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val repository: ShoppingRepository
) : ViewModel() {

    val shoppingItems = repository.observeAllShoppingItems()

    val totalPrice = repository.observeTotalPrice()

    private val _imagesLiveData = MutableLiveData<Event<APIResponse<ImageResponse>>>()
    val imagesLiveData: LiveData<Event<APIResponse<ImageResponse>>> = _imagesLiveData

    private val _currentImageUrlLiveData = MutableLiveData<String>()
    val currentImageUrlLiveData: LiveData<String> = _currentImageUrlLiveData

    private val _insertShoppingItemStatus = MutableLiveData<Event<APIResponse<ImageResult>>>()
    val insertShoppingItemStatus: LiveData<Event<APIResponse<ImageResult>>> =
        _insertShoppingItemStatus

    fun setCurrentImageUrl(url: String) {
        _currentImageUrlLiveData.postValue(url)
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.deleteShoppingItem(shoppingItem)
    }

    fun insertShoppingItemInDb(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.insertShoppingItem(shoppingItem)
    }

    fun insertShoppingItem(name: String, amount: String, price: String) {

    }

    fun searchForImage(query: String) {

    }

}