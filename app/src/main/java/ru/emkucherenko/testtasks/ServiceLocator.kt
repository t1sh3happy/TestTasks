package ru.emkucherenko.testtasks

import android.content.Context

object ServiceLocator {

    lateinit var contactsRepository: ContactsRepository
        private set

    fun init(context: Context) {
        contactsRepository = ContactsRepositoryImpl(context.applicationContext.contentResolver)
    }
}