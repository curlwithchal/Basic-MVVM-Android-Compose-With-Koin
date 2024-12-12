package com.example.belajarandroid.di

import com.example.belajarandroid.repo.CounterRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.core.module.dsl.bind
import com.example.belajarandroid.repo.CounterRepositoryImpl
import com.example.belajarandroid.usecase.counter.CounterUseCase
import com.example.belajarandroid.usecase.counter.CounterViewModel
import org.koin.core.module.dsl.viewModelOf


val moduleCounter = module {
    singleOf(::CounterRepositoryImpl) { bind<CounterRepository>() }
    singleOf(::CounterUseCase)
    viewModelOf(::CounterViewModel)
}