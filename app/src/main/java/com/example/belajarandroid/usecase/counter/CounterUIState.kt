package com.example.belajarandroid.usecase.counter

data class CounterUIState(
    val counter: Int
) {
    companion object {
        val initValue = CounterUIState(counter = 0)
    }
}