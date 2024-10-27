package com.ryz.fakestore.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ryz.fakestore.data.model.response.ProductResponse
import com.ryz.fakestore.data.repository.ProductRepository
import com.ryz.fakestore.utils.GenericUiState
import com.ryz.fakestore.utils.updateFromResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: ProductRepository) : ViewModel() {
    private val _category =
        MutableStateFlow<GenericUiState<List<String>>>(GenericUiState.Loading(false))
    val category = _category.asStateFlow()

    private val _product =
        MutableStateFlow<GenericUiState<List<ProductResponse>>>(GenericUiState.Loading(false))
    val product = _product.asStateFlow()

    private val _productDetail =
        MutableStateFlow<GenericUiState<ProductResponse>>(GenericUiState.Loading(false))
    val productDetail = _productDetail.asStateFlow()

    fun getAllCategory() = viewModelScope.launch {
        repository.getAllCategory().collect { resource ->
            _category.updateFromResource(resource)
        }
    }

    fun getProductByCategory(category: String) = viewModelScope.launch {
        repository.getProductByCategory(category).collect { resource ->
            _product.updateFromResource(resource)
        }
    }

    fun getProduct() = viewModelScope.launch {
        repository.getProduct().collect { resource ->
            _product.updateFromResource(resource)
        }
    }

    fun getProductDetail(productId: Int) = viewModelScope.launch {
        repository.getProductDetail(productId).collect { resource ->
            _productDetail.updateFromResource(resource)
        }
    }
}