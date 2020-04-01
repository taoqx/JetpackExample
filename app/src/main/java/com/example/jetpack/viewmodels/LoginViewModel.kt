package com.example.jetpack.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    enum class LoginState {
        UNAUTHENTICATED,
        AUTHENTICATED,
        CANCEL
    }

    val state: MutableLiveData<LoginState> by lazy {
        MutableLiveData<LoginState>()
    }
    var username: String

    init {
        state.value = LoginState.UNAUTHENTICATED
        username = ""
    }

    fun login(name: String) {
        if (name.isNotEmpty()) {
            state.value = LoginState.AUTHENTICATED
            username = name
        }
    }

    fun popBack() {
        if (state.value == LoginState.UNAUTHENTICATED) {
            state.value = LoginState.CANCEL
        }
    }
}