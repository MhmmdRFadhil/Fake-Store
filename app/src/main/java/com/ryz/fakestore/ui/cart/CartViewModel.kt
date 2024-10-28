package com.ryz.fakestore.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ryz.fakestore.data.local.model.CartProductEntity
import com.ryz.fakestore.data.repository.CartRepository
import com.ryz.fakestore.utils.GenericUiState
import com.ryz.fakestore.utils.updateFromResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val repository: CartRepository) : ViewModel() {

    private val _allProduct = MutableStateFlow<GenericUiState<List<CartProductEntity>>>(
        GenericUiState.Loading(false)
    )
    val allProduct = _allProduct.asStateFlow()

    private val _insertItem = MutableStateFlow<GenericUiState<Any>>(GenericUiState.Loading(false))
    val insertItem = _insertItem.asStateFlow()

    private val _updateItem = MutableStateFlow<GenericUiState<Any>>(GenericUiState.Loading(false))
    val updateItem = _updateItem.asStateFlow()

    private val _deleteItem = MutableStateFlow<GenericUiState<Any>>(GenericUiState.Loading(false))
    val deleteItem = _deleteItem.asStateFlow()

    private val _deleteAll = MutableStateFlow<GenericUiState<Any>>(GenericUiState.Loading(false))
    val deleteAll = _deleteAll.asStateFlow()

    fun getCartProducts() = viewModelScope.launch {
        repository.getCartProducts().collect { resource ->
            _allProduct.updateFromResource(resource)
        }
    }

    fun insertItem(item: CartProductEntity) = viewModelScope.launch {
        val resource = repository.addOrUpdateCart(item)
        _insertItem.updateFromResource(resource)
    }

    fun updateItem(item: CartProductEntity) = viewModelScope.launch {
        val resource = repository.update(item)
        _updateItem.updateFromResource(resource)
    }

    fun deleteItem(id: Int) = viewModelScope.launch {
        val resource = repository.delete(id)
        _deleteItem.updateFromResource(resource)
    }

    fun deleteAll() = viewModelScope.launch {
        val resource = repository.deleteAll()
        _deleteAll.updateFromResource(resource)
    }
}