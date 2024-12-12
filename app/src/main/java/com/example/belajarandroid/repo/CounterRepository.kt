package com.example.belajarandroid.repo


interface CounterRepository {
    suspend fun getCounter(): Int
    suspend fun incrementCounter()
    suspend fun decrementCounter()
}


class CounterRepositoryImpl : CounterRepository {

    private var counter: Int = 0

    override suspend fun getCounter(): Int {
        return counter
    }

    override suspend fun incrementCounter() {
        counter++
    }

    override suspend fun decrementCounter() {
        when {
            counter > 0 -> counter--
        }
    }

}