package com.devdroid07.storeapp.core.presentation.designsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Ease
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.devdroid07.storeapp.core.presentation.designsystem.animation.animateEnterRight

@ExperimentalMaterial3Api
@Composable
fun <T> SwipeToDeleteContainer(
    item: T,
    onDelete: (T) -> Unit,
    content: @Composable (T) -> Unit,
) {
    var isRemoved by remember { mutableStateOf(false) }
    val swipeState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                isRemoved = true
                true
            } else {
                false
            }
        }
    )

    LaunchedEffect(isRemoved) {
        if (isRemoved) {
            onDelete(item)
        }
    }

    AnimatedVisibility(
        modifier = Modifier,
        visible = !isRemoved,
        exit = shrinkHorizontally(
            animationSpec = tween(
                durationMillis = 1000,
                easing = EaseIn
            ),
            shrinkTowards = Alignment.Start
        ) + fadeOut()
    ) {
        SwipeToDismissBox(
            state = swipeState,
            backgroundContent = {
                DeleteBackground(
                    swipeDismissState = swipeState
                )
            },
            enableDismissFromEndToStart = true,
            enableDismissFromStartToEnd = false,
            content = {
                content(item)
            }
        )
    }
}

@ExperimentalMaterial3Api
@Composable
fun DeleteBackground(
    swipeDismissState: SwipeToDismissBoxState
) {

    val background by animateColorAsState(
        targetValue = when {
            swipeDismissState.progress >= 0.15f && swipeDismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart -> Color.Red
            swipeDismissState.progress  >= 0f -> Color.Gray
            else -> Color.Transparent
        },
        animationSpec = tween(
            500,
            easing = Ease
        ),
        label = "color"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .background(
                color = background,
                shape = RoundedCornerShape(20)
            )
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = Color.White
        )
    }


}
