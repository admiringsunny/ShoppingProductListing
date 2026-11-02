package com.learn.shoppingproductlisting.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learn.shoppingproductlisting.api.RetrofitApi
import com.learn.shoppingproductlisting.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductDetailsViewModel : ViewModel() {
    val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    val _productDetail = MutableStateFlow<Product>(Product())
    val productDetail = _productDetail.asStateFlow()

    fun fetchProductDetails(id: Int) {
        viewModelScope.launch {
            _loading.value = true
            try {
                _productDetail.value = RetrofitApi.api.getAllProductDetails(id)
            } catch (e: Exception) {
                Log.e("ProductViewModel", "API call failed: ${e.message}", e)
                _productDetail.value = Product()
            }
            _loading.value = false
        }
    }
}