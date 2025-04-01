package com.example.meinedemo.ui.user

import com.example.meinedemo.data.UserRepository
import com.example.meinedemo.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModelTest {

    @Test
    fun testAddUser() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        val userRepositoryMock = mock<UserRepository>()
        val user = User(name = "Erika Muster", age = 28, authorized = true)

        val viewModel = UserViewModel(userRepositoryMock)

        viewModel.addUser(user)

        verify(userRepositoryMock).addUser(user)

        Dispatchers.resetMain()
    }

}
