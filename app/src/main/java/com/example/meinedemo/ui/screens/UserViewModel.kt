package com.example.meinedemo.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meinedemo.data.UserRepository
import com.example.meinedemo.navigation.DemoApplicationScreen
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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
            DemoApplicationScreen.User("", -1, false)
        )

    fun updateUser(user: User) {
        viewModelScope.launch {
            userRepository.setUserName(user.name)
            userRepository.setUserAge(user.age)
            userRepository.setUserAuthorized(user.authorized)
        }
    }
}
