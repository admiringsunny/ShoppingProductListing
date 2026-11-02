package com.learn.shoppingproductlisting.ui

import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.learn.shoppingproductlisting.viewModels.ProductListViewModel
import com.squareup.picasso.Picasso

@Composable
fun ProductsListScreen(
    modifier: Modifier,
    viewModel: ProductListViewModel,
    onProductClick: (Int) -> Unit
) {
    val products by viewModel.productsList.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 16.dp)
    ) {
        LaunchedEffect(Unit) { viewModel.fetchAllProducts() }

        if (loading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(50.dp),
                    strokeWidth = 5.dp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Loading products...",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            val context = LocalContext.current
            LazyColumn() {
                items(products) { product ->
                    Log.d("TAG", "ProductsListScreen: $products")
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        onClick = { onProductClick(product.id) }
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AndroidView(
                                factory = {
                                    ImageView(it)
                                        .apply {
                                            scaleType = ImageView.ScaleType.CENTER_CROP
                                        }
                                },
                                update = { imageView ->
                                    Picasso.get()
                                        .load(product.thumbnail)
                                        .into(imageView)
                                },
                                modifier = Modifier.size(80.dp)
                            )

                            Spacer(Modifier.width(4.dp))
                            Column(
                                modifier = Modifier.fillMaxWidth(1f),
                                horizontalAlignment = Alignment.Start
                            ) {
                                if (product.brand != null) {
                                    Text(
                                        text = product.brand,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(Modifier.width(2.dp))
                                Text(
                                    text = product.title,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Spacer(Modifier.width(2.dp))
                                Text(
                                    text = product.description,
                                    maxLines = 3,
                                    fontWeight = FontWeight.Normal,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(Modifier.width(2.dp))

                                Row(horizontalArrangement = Arrangement.Start) {
                                    Text(
                                        text = "$${product.price.toString()}",
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(Modifier.width(6.dp))
                                    Text(text = "(${product.discountPercentage}% OFF)")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}