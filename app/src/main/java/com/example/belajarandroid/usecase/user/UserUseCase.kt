package com.example.belajarandroid.usecase.user

import com.example.belajarandroid.data.User
import com.example.belajarandroid.repo.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

class UserUseCase(private val userRepository: UserRepository) {

    suspend fun getUsers(): Flow<List<User>> {
        delay(1000L)
        return userRepository.getUsers()
    }

    suspend fun addUser(user: User) {
        userRepository.addUser(user)
    }

    suspend fun deletedUser(id: Int): Boolean {
        return userRepository.deleteUser(id)
    }

    private suspend fun getId(id: Int): User? {
        return userRepository.findById(id)
    }

    suspend fun updateUser(id: Int, user: User): User {
        return userRepository.updateUser(id, user)
    }

}