package ru.itis.terraapp.data.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ru.itis.terraapp.data.database.entity.UserEntity
import ru.itis.terraapp.domain.model.User

inline fun <reified T : ViewModel> Fragment.lazyViewModel(noinline create: (SavedStateHandle) -> T) =
    viewModels<T> {
        AssistedFactory(this, create)
    }

fun User.toEntity() = UserEntity(userId ?: 0, email = email, password = password, username = username)

fun UserEntity.toUser() = User(id, email = email, password = password, username = username)