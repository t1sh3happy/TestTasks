package ru.emkucherenko.testtasks

interface ContactsRepository {
    suspend fun getContacts(): List<String>
}