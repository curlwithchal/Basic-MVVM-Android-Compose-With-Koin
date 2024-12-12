package com.example.belajarandroid.usecase.counter

import com.example.belajarandroid.repo.CounterRepository


class CounterUseCase(private val counterRepository: CounterRepository){

    suspend fun getCounter(): Int{
        return counterRepository.getCounter()
    }

    suspend fun increment(){
        counterRepository.incrementCounter()
    }

    suspend fun decrement(){
        counterRepository.decrementCounter()
    }
}