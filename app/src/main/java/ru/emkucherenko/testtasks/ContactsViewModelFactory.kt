package ru.emkucherenko.testtasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ContactsViewModelFactory(
    private val repository: ContactsRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        ContactsViewModel(repository) as T
}