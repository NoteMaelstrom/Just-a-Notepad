package com.example.justanotepad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.EditText
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var editTextNote: EditText
    private val handler = Handler()
    private val saveRunnable = Runnable { saveTextToFile() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextNote = findViewById(R.id.editTextNote)

        initializeFile()
        loadTextFromFile()

        // Set up a text change listener to implement auto-save
        editTextNote.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used, but must be implemented
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not used, but must be implemented
            }

            override fun afterTextChanged(s: android.text.Editable?) {
                // Remove any existing callbacks to the handler
                handler.removeCallbacks(saveRunnable)
                // Post a new delayed task to the handler
                handler.postDelayed(saveRunnable, 1000) // 1000 milliseconds == 1 second
            }
        })
    }

    private fun saveTextToFile() {
        val text = editTextNote.text.toString()
        try {
            openFileOutput("notepad.txt", MODE_PRIVATE).use { output ->
                output.write(text.toByteArray())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun loadTextFromFile() {
        try {
            val fileInputStream = openFileInput("notepad.txt")
            val text = fileInputStream.bufferedReader().readText()
            editTextNote.setText(text)
            fileInputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun initializeFile() {
        val file = File(filesDir, "notepad.txt")
        if (!file.exists()) {
            try {
                FileOutputStream(file).use { stream ->
                    stream.write("".toByteArray())  // Creates an empty file if not exists
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}