@file:OptIn(ExperimentalMaterial3Api::class,
            ExperimentalMaterial3Api::class
)

package com.devdroid07.storeapp.store.presentation.myCart

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.devdroid07.storeapp.R
import com.devdroid07.storeapp.core.presentation.designsystem.CartIcon
import com.devdroid07.storeapp.core.presentation.designsystem.CloseIcon
import com.devdroid07.storeapp.core.presentation.designsystem.Logo
import com.devdroid07.storeapp.core.presentation.designsystem.RemoveIcon
import com.devdroid07.storeapp.core.presentation.designsystem.components.ErrorContent
import com.devdroid07.storeapp.core.presentation.designsystem.components.StoreToolbar
import com.devdroid07.storeapp.core.presentation.designsystem.components.SwipeToDeleteContainer
import com.devdroid07.storeapp.core.presentation.designsystem.components.handleResultView
import com.devdroid07.storeapp.core.presentation.ui.ObserveAsEvents
import com.devdroid07.storeapp.core.presentation.ui.UiText
import com.devdroid07.storeapp.store.presentation.myCart.components.EmptyMyCartScreen
import com.devdroid07.storeapp.store.presentation.myCart.components.FooterMyCart
import com.devdroid07.storeapp.store.presentation.myCart.components.ItemCard
import com.devdroid07.storeapp.store.presentation.myCart.components.MyCartShimmerEffect
import kotlinx.coroutines.launch

@Composable
fun MyCartScreenRoot(
    state: MyCartState,
    context: Context,
    viewModel: MyCartViewModel,
    onBack: () -> Unit,
    onAction: (MyCartAction) -> Unit
) {

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            is MyCartEvent.Error -> {
                scope.launch {
                    snackbarHostState.showSnackbar(message = event.error.asString(context))
                }
            }
            is MyCartEvent.Success -> {
                scope.launch {
                    snackbarHostState.showSnackbar(message = event.message.asString(context))
                }
            }
        }

    }

    MyCartScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onAction = { action ->
            when (action) {
                MyCartAction.OnBackClick -> onBack()
                else -> Unit
            }
            onAction(action)
        }
    )

}

@Composable
private fun MyCartScreen(
    state: MyCartState,
    snackbarHostState: SnackbarHostState,
    onAction: (MyCartAction) -> Unit
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            StoreToolbar(
                title = stringResource(R.string.my_cart_title_screen),
                isMenu = false,
                isProfile = false,
                onBack = {
                    onAction(MyCartAction.OnBackClick)
                },
                openDrawer = { /*TODO*/ }
            )
        }
    ) { paddingValue ->

        val result = handleResultView(
            isLoading = state.isLoading,
            contentLoading = {
                MyCartShimmerEffect(paddingValues = paddingValue)
            },
            isEmpty = state.myCart.isEmpty(),
            contentEmpty = {
                EmptyMyCartScreen()
            },
            error = state.error,
            retry = {
                onAction(MyCartAction.OnRetryClick)
            }
        )

        if (result) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValue)
            ) {

                LazyColumn(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.myCart) { cart ->
                        SwipeToDeleteContainer(
                            item = cart,
                            onDelete = {
                                onAction(MyCartAction.OnRemoveProduct(it.idProduct.toInt()))
                            }
                        ) {
                            ItemCard(
                                cart = cart
                            )
                        }
                    }
                }

                FooterMyCart(
                    state = state,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.3f),
                )

            }
        }

    }
}