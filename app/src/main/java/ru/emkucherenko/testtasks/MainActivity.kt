package ru.emkucherenko.testtasks

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: ContactsViewModel by viewModels {
        ContactsViewModelFactory(ServiceLocator.contactsRepository)
    }

    private lateinit var loadButton: Button
    private lateinit var statusText: TextView
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) viewModel.loadContacts() else viewModel.onPermissionDenied()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadButton = findViewById(R.id.loadButton)
        statusText = findViewById(R.id.statusText)
        listView = findViewById(R.id.contactList)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf<String>())
        listView.adapter = adapter

        loadButton.setOnClickListener { onLoadClicked() }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { render(it) }
            }
        }
    }

    private fun onLoadClicked() {
        val granted = ContextCompat.checkSelfPermission(
            this, Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED

        if (granted) {
            viewModel.loadContacts()
        } else {
            permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }

    private fun render(state: ContactsUiState) {
        loadButton.isEnabled = state !is ContactsUiState.Loading
        when (state) {
            ContactsUiState.Idle -> {
                statusText.visibility = View.GONE
                listView.visibility = View.GONE
            }
            ContactsUiState.Loading -> showStatus(R.string.loading)
            ContactsUiState.PermissionDenied -> showStatus(R.string.permission_denied)
            is ContactsUiState.Success -> {
                if (state.contactList.isEmpty()) {
                    showStatus(R.string.no_contacts)
                } else {
                    adapter.clear()
                    adapter.addAll(state.contactList)
                    statusText.visibility = View.GONE
                    listView.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showStatus(textRes: Int) {
        statusText.setText(textRes)
        statusText.visibility = View.VISIBLE
        listView.visibility = View.GONE
    }
}