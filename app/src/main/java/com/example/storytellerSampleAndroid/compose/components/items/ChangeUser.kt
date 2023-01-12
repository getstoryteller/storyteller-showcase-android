package com.example.storytellerSampleAndroid.compose.components.items

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.storytellerSampleAndroid.SampleApp
import com.example.storytellerSampleAndroid.compose.JetpackComposeViewModel
import com.example.storytellerSampleAndroid.compose.toast
import com.storyteller.Storyteller
import java.util.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ChangeUserContainer(
  onRefresh: () -> Unit,
  coroutineScope: CoroutineScope,
  listState: LazyListState,
  viewModel: JetpackComposeViewModel
) {
  Row(
    modifier = Modifier
      .fillMaxSize()
      .padding(vertical = 8.dp),
    horizontalArrangement = Arrangement.SpaceAround
  ) {
    ChangeUserButton(onSuccess = {
      onRefresh()
      coroutineScope.launch {
        listState.animateScrollToItem(0)
      }
      viewModel.startRefreshing()
    })
  }
}

@Composable
fun ChangeUserButton(
  modifier: Modifier = Modifier, onSuccess: () -> Unit = {}, onFailure: () -> Unit = {}
) {
  val context = LocalContext.current
  Button(modifier = modifier, onClick = {
    val freshUserId = UUID.randomUUID().toString()
    SampleApp.initializeStoryteller(userId = freshUserId, onSuccess = {
      Log.i("Storyteller Sample", "initialize success ${Storyteller.currentUser}")
      context.toast("user id changed to: $freshUserId")
      onSuccess.invoke()
    }, onFailure = {
      Log.e("Storyteller Sample", "initialize failed $it}")
      context.toast("failed to change user id")
      onFailure.invoke()
    })
  }) {
    Text("Change user")
  }
}
