package com.example.meinedemo.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meinedemo.data.UserRepository
import com.example.meinedemo.model.User
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    val user: StateFlow<User> = userRepository
        .user
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            User(id = 0, name = "", age = 1, authorized = false)
        )

    val allUsers: StateFlow<List<User>> = flow {
        userRepository.getAllUsers().collect { emit(it) }
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        emptyList()
    )

    fun updateUser(user: User) {
        viewModelScope.launch {
            userRepository.setUserName(user.name)
            userRepository.setUserAge(user.age)
            userRepository.setUserAuthorized(user.authorized)
        }
    }

    fun addUser(user: User) {
        viewModelScope.launch {
            userRepository.addUser(user)
        }
    }
}

