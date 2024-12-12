package com.example.belajarandroid.di

import com.example.belajarandroid.repo.UserRepository
import com.example.belajarandroid.repo.UserRepositoryImpl
import com.example.belajarandroid.usecase.user.UserUseCase
import com.example.belajarandroid.usecase.user.UserViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val userModule = module {
    singleOf(::UserRepositoryImpl) bind UserRepository::class
    singleOf(::UserUseCase)
    viewModelOf(::UserViewModel)
}