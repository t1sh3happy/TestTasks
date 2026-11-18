package ru.emkucherenko.testtasks

import android.content.ContentResolver
import android.provider.ContactsContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContactsRepositoryImpl(
    private val contentResolver: ContentResolver
) : ContactsRepository {

    override suspend fun getContacts(): List<String> = withContext(Dispatchers.IO) {
        val contacts = mutableListOf<String>()
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            arrayOf(ContactsContract.Contacts.DISPLAY_NAME),
            null,
            null,
            null
        )
        cursor?.use {
            val nameIndex = it.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)

            while (it.moveToNext()) {
                contacts.add(it.getString(nameIndex) ?: "<no name>")
            }

        }
        contacts
    }
}