package com.learn.shoppingproductlisting.api

import com.learn.shoppingproductlisting.model.Product
import com.learn.shoppingproductlisting.model.ProductResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductsApi {
    @GET("products")
    suspend fun getAllProducts(): ProductResponse
    @GET("products/{id}")
    suspend fun getAllProductDetails(@Path("id") id: Int): Product
}

object RetrofitApi {
    val api: ProductsApi = Retrofit.Builder()
        .baseUrl("https://dummyjson.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ProductsApi::class.java)
}