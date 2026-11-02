package com.learn.shoppingproductlisting.nav

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.learn.shoppingproductlisting.ui.ProductDetailsScreen
import com.learn.shoppingproductlisting.ui.ProductsListScreen

object List : NavKey
data class Details(val id: Int) : NavKey

@Composable
fun NavigationScreen(modifier: Modifier) {
    val backStack = remember { mutableStateListOf<NavKey>(List) }

    NavDisplay(
        backStack = backStack,
        onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<List> {
                ProductsListScreen(
                    modifier, viewModel(),
                    onProductClick = { id ->
                        Log.d("NAV", "Clicked Product ID: $id")
                        backStack.add(Details(id))
                    })
            }
            entry<Details> {
                ProductDetailsScreen(modifier, it.id, viewModel())
            }
        }
    )
}