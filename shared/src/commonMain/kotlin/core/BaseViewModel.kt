package core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


abstract class BaseViewModel<S,E>(initialState:S) : ViewModel() {
    private val _state : MutableStateFlow<S> = MutableStateFlow(initialState)
    val state : StateFlow<S> = _state.asStateFlow()

    private val _events = Channel<E>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

//    protected val currentState: S = _state.value
    protected val currentState: S get() = _state.value    // custom getter: evaluated on EVERY read

    protected fun updateState(reducer: S.() -> S) = _state.update { it.reducer() }


    protected fun sendEvent(event: E) {
        viewModelScope.launch { _events.send(event) }
    }

}