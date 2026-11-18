package ru.emkucherenko.testtasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ContactsViewModel(private val repository: ContactsRepository) : ViewModel() {

    private val _state = MutableStateFlow<ContactsUiState>(ContactsUiState.Idle)
    val state: StateFlow<ContactsUiState> = _state

    fun loadContacts() {
        _state.value = ContactsUiState.Loading
        viewModelScope.launch {
            val contacts = repository.getContacts()
            _state.value = ContactsUiState.Success(contacts)
        }
    }

    fun onPermissionDenied() {
        _state.value = ContactsUiState.PermissionDenied
    }
}