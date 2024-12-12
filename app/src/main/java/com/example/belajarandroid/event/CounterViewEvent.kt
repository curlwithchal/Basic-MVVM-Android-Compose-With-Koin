package com.example.belajarandroid.event


sealed class CounterViewEvent{
    data object OnClickCounterEventInc: CounterViewEvent()
    data object OnClickCounterEventDec: CounterViewEvent()
}