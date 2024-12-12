package com.example.belajarandroid.usecase.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.belajarandroid.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class UserViewModel(private val userUseCase: UserUseCase) : ViewModel() {

    private val _state = MutableStateFlow<UserUiState>(UserUiState.Loading)
    val state: StateFlow<UserUiState> = _state.asStateFlow()

    var username by mutableStateOf("")
        private set

    var age by mutableStateOf("")
        private set

    fun updateUserName(setUsername: String) {
        username = setUsername
    }

    fun updateAge(setAge: String) {
        age = setAge
    }

    private suspend fun loadUser() {
        userUseCase.getUsers()
            .onStart { UserUiState.Loading }
            .collect { users ->
                _state.value = if (users.isEmpty())
                    UserUiState.Empty
                else
                    UserUiState.Success(users.toMutableList())
            }
    }

    fun onEvent(event: UserEvent, userId: Int? = null, user: User? = null) {
        when (event) {
            is UserEvent.OnClickAddUserEvent -> if (user != null) onAddUserEvent(user)
            is UserEvent.OnClickUpdateUserEvent -> if (userId != null && user != null) onUpdateUserEvent(
                userId,
                user
            )

            is UserEvent.OnClickDeleteUserEvent -> if (userId != null) onDeleteUserEvent(userId)
        }
    }

    private fun onAddUserEvent(user: User) {
        viewModelScope.launch {
            userUseCase.addUser(user)
            loadUser()
        }
    }

    private fun onUpdateUserEvent(id: Int, user: User) {
        viewModelScope.launch {
            userUseCase.updateUser(id, user)
            loadUser()
        }
    }

    private fun onDeleteUserEvent(id: Int) {
        viewModelScope.launch {
            userUseCase.deletedUser(id)
            loadUser()
        }
    }
}

class UnsupportedUserEventException : Exception("Unsupported User Event")