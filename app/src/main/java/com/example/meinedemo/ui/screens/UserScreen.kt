package com.example.meinedemo.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.meinedemo.data.UserRepository
import com.example.meinedemo.model.User
import com.example.meinedemo.model.UserViewModelFactory

@Composable
fun UserScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Welcome to the UserScreen",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        val userViewModel = getUserViewModel()
        val user by userViewModel.user.collectAsState()
        val users by userViewModel.allUsers.collectAsState()

        UserInputs(
            user = user,
            onUpdateUser = { updatedUser ->
                userViewModel.updateUser(updatedUser)
            },
            onAddUser = { newUser ->
                userViewModel.addUser(newUser)
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        UserList(
            modifier = Modifier.weight(1f),
            users = users
        )
    }
}

@Composable
private fun UserInputs(
    user: User,
    onUpdateUser: (User) -> Unit,
    onAddUser: (User) -> Unit
) {
    var name by remember(user.name) { mutableStateOf(user.name) }
    var age by remember(user.age) { mutableStateOf(user.age.toString()) }
    var isActive by remember { mutableStateOf(user.authorized) }

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            onValueChange = { newName ->
                name = newName
            },
            label = { Text("Name") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = age,
            onValueChange = { newAge ->
                age = newAge
            },
            label = { Text("Alter") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Switch(
                checked = isActive,
                onCheckedChange = { isChecked ->
                    isActive = isChecked
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("User autorisieren")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(onClick = {
                onUpdateUser(
                    User(
                        user.id,
                        name,
                        age.toIntOrNull() ?: 0,
                        isActive
                    )
                )
            }) {
                Text("Speichern")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { onAddUser(User(0, name, age.toIntOrNull() ?: 0, isActive)) }) {
                Text("Hinzufügen")
            }
        }
    }
}

@Composable
fun getUserViewModel(): UserViewModel {
    val userRepository = UserRepository(LocalContext.current)
    return viewModel(factory = UserViewModelFactory(userRepository))
}

@Composable
fun UserList(
    modifier: Modifier = Modifier,
    users: List<User>
) {
    LazyColumn(modifier = modifier) {
        items(users) { user ->
            UserCard(user)
        }
    }
}

@Composable
fun UserCard(user: User) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${user.name}, ${user.age}, ${if (user.authorized) "ist autorisiert" else "ist nicht autorisiert"}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
