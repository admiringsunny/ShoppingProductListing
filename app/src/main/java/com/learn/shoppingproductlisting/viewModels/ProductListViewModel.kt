package com.learn.shoppingproductlisting.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learn.shoppingproductlisting.api.RetrofitApi
import com.learn.shoppingproductlisting.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductListViewModel : ViewModel() {
    val _productsList = MutableStateFlow<List<Product>>(emptyList())
    val productsList = _productsList.asStateFlow()
    val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    fun fetchAllProducts() {
        viewModelScope.launch {
            _loading.value = true

            try {
                Log.d("ProductViewModel", "API call started")

                val response = RetrofitApi.api.getAllProducts().products

                Log.d("ProductViewModel", "API call successful")
                Log.d("ProductViewModel", "Products received: ${response.size}")

                _productsList.value = response

            } catch (e: Exception) {
                Log.e("ProductViewModel", "API call failed: ${e.message}", e)

                _productsList.value = emptyList()

            } finally {
                _loading.value = false
            }
        }
    }
}