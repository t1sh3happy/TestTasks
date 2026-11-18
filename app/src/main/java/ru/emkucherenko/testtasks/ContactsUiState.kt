package ru.emkucherenko.testtasks

sealed interface ContactsUiState {
    data object Idle : ContactsUiState
    data object Loading : ContactsUiState
    data object PermissionDenied : ContactsUiState
    data class Success(val contactList: List<String>) : ContactsUiState
}