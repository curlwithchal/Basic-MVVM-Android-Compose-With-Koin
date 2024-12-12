package com.example.belajarandroid.usecase.user

import com.example.belajarandroid.data.User


sealed interface UserUiState {
    data object Loading : UserUiState
    data class Success(
        val users: List<User>
    ) : UserUiState

    data object Empty : UserUiState
}



