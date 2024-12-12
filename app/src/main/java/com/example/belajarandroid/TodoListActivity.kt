package com.example.belajarandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.belajarandroid.data.User
import com.example.belajarandroid.di.userModule
import com.example.belajarandroid.ui.theme.BelajarAndroidTheme
import com.example.belajarandroid.usecase.user.UserEvent
import com.example.belajarandroid.usecase.user.UserUiState
import com.example.belajarandroid.usecase.user.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.androidx.compose.koinViewModel
import kotlin.random.Random

class TodoListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BelajarAndroidTheme {
                KoinAndroidContext {
                    Scaffold { innerPadding ->
                        Column(modifier = Modifier.padding(innerPadding)) {
                            TodoScreen()
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun TodoScreen() {

    val viewModel: UserViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    var isError by remember { mutableStateOf(false) }
    var tempId by remember { mutableIntStateOf(0) }
    var progressCreate by remember { mutableStateOf(false) }
    var progressUpdate by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    TodoContent(
        userName = viewModel.username,
        age = viewModel.age,
        onUsernameChange = { username -> viewModel.updateUserName(username) },
        onAgeChange = { age -> viewModel.updateAge(age) },
        userUIState = state,
        isError = isError,
        progressCreate = progressCreate,
        progressUpdate = progressUpdate,
        onClickView = { id, username, age ->
            viewModel.updateUserName(username)
            viewModel.updateAge(age)
            if (id != null) tempId = id
        },
        onCreate = {

            val user = User(
                id = Random.nextInt(1, 50),
                name = viewModel.username,
                age = viewModel.age
            )

            if (viewModel.username.isNotEmpty() && viewModel.age.isNotEmpty()) {
                isError = false
                scope.launch {
                    progressCreate = true
                    delay(1000L)
                    viewModel.onEvent(UserEvent.OnClickAddUserEvent, user = user)
                    progressCreate = false
                }
            } else {
                isError = true
            }

            viewModel.updateUserName("")
            viewModel.updateAge("")
        },
        errorTextUsername = {
            if (isError) Text("username is not blank")
        },
        errorTextAge = {
            if (isError) Text("age is not blank")
        },
        onUpdate = {
            val user = User(
                id = tempId,
                name = viewModel.username,
                age = viewModel.age
            )
            if (viewModel.username.isNotEmpty() && viewModel.age.isNotEmpty()) {
                isError = false
                scope.launch {
                    progressUpdate = true
                    delay(1000L)
                    viewModel.onEvent(
                        event = UserEvent.OnClickUpdateUserEvent,
                        userId = tempId,
                        user
                    )
                    progressUpdate = false
                }
            } else {
                isError = true
            }
            viewModel.updateUserName("")
            viewModel.updateAge("")
        },
        onDeleted = {
            viewModel.onEvent(event = UserEvent.OnClickDeleteUserEvent, userId = it)
        }
    )

}

@Composable
fun TodoContent(
    userName: String,
    age: String,
    onUsernameChange: (String) -> Unit,
    onAgeChange: (String) -> Unit,
    userUIState: UserUiState,
    onCreate: () -> Unit,
    onUpdate: () -> Unit,
    onDeleted: (Int) -> Unit,
    isError: Boolean = false,
    onClickView: (id: Int?, username: String, age: String) -> Unit,
    errorTextUsername: @Composable () -> Unit,
    errorTextAge: @Composable () -> Unit,
    progressCreate: Boolean,
    progressUpdate: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)

    ) {
        TextField(
            value = userName,
            onValueChange = onUsernameChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Username") },
            supportingText = errorTextUsername,
            isError = isError

        )
        TextField(
            value = age,
            onValueChange = onAgeChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Age") },
            supportingText = errorTextAge,
            isError = isError
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = onCreate, enabled = !progressCreate) {
                if (progressCreate) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Create")
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onUpdate, enabled = !progressUpdate) {
                if (progressUpdate) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Update")
                }
            }
        }
        UserList(userUIState, onDelete = onDeleted, onClickView = onClickView)
    }
}

@Composable
fun EmptyScreen() {
    Text("Please Add Data")
}

@Composable
fun UserList(
    uiState: UserUiState,
    onDelete: (Int) -> Unit,
    onClickView: (id: Int?, username: String, age: String) -> Unit
) {

    when (uiState) {
        is UserUiState.Loading ->
            Box(modifier = Modifier.fillMaxWidth()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

        is UserUiState.Success ->
            Column {
                uiState.users.forEach {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            modifier = Modifier.clickable { onClickView(it.id, it.name, it.age) },
                            text = "Id : ${it.id} Name: ${it.name} Age: ${
                                it.age
                            }"
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "ic_remove",
                            modifier = Modifier.clickable {
                                onDelete(it.id)
                                println(it.id)
                            },
                            tint = Color.Red
                        )
                    }
                }
            }

        is UserUiState.Empty -> EmptyScreen()
    }
}

@Preview(showBackground = true, device = "id:small_phone")
@Composable
fun TodoListPreview() {
    val context = LocalContext.current

    org.koin.compose.KoinApplication(application = {
        androidContext(context)
        modules(userModule)
    }) {
        BelajarAndroidTheme {
            TodoScreen()
        }
    }
}