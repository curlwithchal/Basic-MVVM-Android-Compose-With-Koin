package com.example.belajarandroid.usecase.user

sealed class UserEvent {
    data object OnClickAddUserEvent: UserEvent()
    data object OnClickDeleteUserEvent: UserEvent()
    data object OnClickUpdateUserEvent: UserEvent()
}