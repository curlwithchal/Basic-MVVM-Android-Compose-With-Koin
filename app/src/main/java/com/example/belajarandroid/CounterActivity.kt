package com.example.belajarandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.belajarandroid.event.CounterViewEvent
import com.example.belajarandroid.ui.theme.BelajarAndroidTheme
import com.example.belajarandroid.usecase.counter.CounterUIState
import com.example.belajarandroid.usecase.counter.CounterViewModel
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.androidx.compose.koinViewModel

class CounterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BelajarAndroidTheme {
                KoinAndroidContext {
                    CounterScreen()
                }
            }
        }
    }
}

@Composable
fun CounterScreen(
) {
    val viewModel: CounterViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    CounterContent(
        state = state,
        onClickCounterInc = { viewModel.onEvent(CounterViewEvent.OnClickCounterEventInc) },
        onClickCounterDec = { viewModel.onEvent(CounterViewEvent.OnClickCounterEventDec) }
    )
}

@Composable
fun CounterContent(
    state: CounterUIState = CounterUIState(0),
    onClickCounterInc: () -> Unit = {},
    onClickCounterDec: () -> Unit = {}
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("MVVM SAMPLE")
        Spacer(modifier = Modifier.padding(top = 24.dp))
        Button(onClick = onClickCounterInc) {
            Text("Increment")
        }
        Button(onClick = onClickCounterDec) {
            Text("Decrement")
        }
        Spacer(modifier = Modifier.padding(top = 16.dp))
        Text("Counter: ${state.counter}")
    }
}

@Preview(device = "id:small_phone", showBackground = true)
@Composable
fun CounterPreview() {
    BelajarAndroidTheme {
        CounterContent()
    }
}
