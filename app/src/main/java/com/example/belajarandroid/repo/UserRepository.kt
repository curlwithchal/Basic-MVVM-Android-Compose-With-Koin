package com.example.belajarandroid.repo

import android.content.res.Resources.NotFoundException
import com.example.belajarandroid.data.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface UserRepository {
    suspend fun getUsers(): Flow<List<User>>
    suspend fun addUser(user: User)
    suspend fun deleteUser(id: Int): Boolean
    suspend fun findById(id: Int): User?
    suspend fun updateUser(id: Int, user: User): User
}

class UserRepositoryImpl : UserRepository {
    private val _users = arrayListOf<User>()


    override suspend fun getUsers(): Flow<List<User>> = flow {
         emit(_users)
    }

    override suspend fun addUser(user: User) {
        _users.add(user)
    }

    override suspend fun deleteUser(id: Int): Boolean {
        val user = _users.find { it.id == id }
        return _users.remove(user)
    }

    override suspend fun findById(id: Int): User {
        val userId = _users.find { it.id == id }
        if (userId != null) {
            return userId
        } else {
            throw NotFoundException("not found id exception")
        }
    }

    override suspend fun updateUser(id: Int, user: User): User {
        val check = findById(id)
        val index = _users.indexOf(check)
        _users[index] = user
        return check
    }

}