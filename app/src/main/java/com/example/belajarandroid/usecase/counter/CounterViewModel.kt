package com.example.belajarandroid.usecase.counter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.belajarandroid.event.CounterViewEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class CounterViewModel(private val useCase: CounterUseCase): ViewModel(){

    private val _state = MutableStateFlow(CounterUIState.initValue)
    val state: StateFlow<CounterUIState> = _state.asStateFlow()

    private fun currentState(): CounterUIState = _state.value

    private fun updateState(newState: CounterUIState){
        _state.value = newState
    }

    fun onEvent(event: CounterViewEvent){
        viewModelScope.launch {
            when(event){
                is CounterViewEvent.OnClickCounterEventInc -> onClickCounterEventIncrement()
                is CounterViewEvent.OnClickCounterEventDec -> onClickCounterEventDecrement()
            }
        }
    }

    private suspend fun onClickCounterEventDecrement(){
        useCase.decrement()
        val newCounterValue = useCase.getCounter()
        updateState(currentState().copy(counter = newCounterValue))
    }

    private suspend fun onClickCounterEventIncrement(){
        useCase.increment()
        val newCounterValue = useCase.getCounter()
        updateState(currentState().copy(counter = newCounterValue))
    }
}