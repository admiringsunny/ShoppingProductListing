package com.learn.shoppingproductlisting.ui

import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.learn.shoppingproductlisting.viewModels.ProductDetailsViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

@Composable
fun ProductDetailsScreen(
    modifier: Modifier,
    id: Int,
    viewModel: ProductDetailsViewModel
) {
    var quantity by remember { mutableIntStateOf(1) }
    val productDetail by viewModel.productDetail.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    var isImageLoading by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 16.dp)
    ) {
        LaunchedEffect(Unit) {
            viewModel.fetchProductDetails(id)
        }
        Spacer(modifier = Modifier.height(8.dp))

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
                    text = "Loading Product Details...",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            val context = LocalContext.current
            LazyColumn {
                item {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = productDetail.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.Center
                    )
                    if (productDetail.images.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        AndroidView(
                            factory = {
                                ImageView(it).apply { scaleType = ImageView.ScaleType.FIT_CENTER }
                            },
                            update = { imageView ->
                                isImageLoading = true
                                Picasso.get()
                                    .load(productDetail.images[0])
                                    .into(imageView, object : Callback {
                                        override fun onSuccess() {
                                            isImageLoading = false
                                        }

                                        override fun onError(e: Exception?) {
                                            isImageLoading = false
                                        }
                                    })
                            },
                            modifier = Modifier
                                .height(200.dp)
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally),
                        )
                        if (isImageLoading) {
                            Column(
                                modifier = Modifier
                                    .height(200.dp)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(50.dp),
                                    strokeWidth = 5.dp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Loading Product Image...",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = productDetail.brand,
                        style = MaterialTheme.typography.bodyMedium,
                        fontStyle = FontStyle.Italic,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Start
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = productDetail.description,
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.bodySmall,
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "$${productDetail.price} (${productDetail.discountPercentage}% OFF)",
                        fontSize = 16.sp,
                        textAlign = TextAlign.End,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodySmall,
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Quantity
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .height(40.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    if (quantity > 1) quantity--
                                }, modifier = Modifier.fillMaxHeight()
                            ) {
                                Text(text = "-")
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$quantity",
                                    textAlign = TextAlign.Center
                                )
                            }
                            Button(
                                onClick = {
                                    quantity++
                                }, modifier = Modifier.fillMaxHeight()
                            ) {
                                Text(text = "+")
                            }
                        }


                        // Add to cart Button
                        Spacer(Modifier.width(20.dp))
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                Toast.makeText(
                                    context,
                                    "Added to cart: id = ${productDetail.id}, Qty = $quantity",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        ) {
                            Text("Add to cart")
                        }
                    }
                }
            }
        }
    }
}