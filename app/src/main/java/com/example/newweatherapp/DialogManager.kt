package com.example.newweatherapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import com.example.newweatherapp.fragments.MainFragment

object DialogManager {

    fun locationSettingsDialog(context: Context, listener: Listener) {
        val builder = AlertDialog.Builder(context)
        val dialog = builder.create()
        dialog.setTitle("Enabled Location")
        dialog.setMessage("Location disabled, do you want enabled location ?")
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK") { _, _ ->
            listener.onClick()
            dialog.dismiss()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel") { _, _ ->
            dialog.dismiss()
        }
        dialog.show()
    }

    fun setCity(context: Context, listener: Listener) {
        val builder = AlertDialog.Builder(context)
        val dialog = builder.create()
        dialog.setTitle("Enter city")
        val input = EditText(context)
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK") { _, _ ->
            listener.onClick()
            dialog.dismiss()
            val intent = Intent(context, MainFragment::class.java)
            intent.putExtra("city", input.text.toString())
            context.startActivity(intent)
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel") { _, _ ->
            dialog.dismiss()
        }
        dialog.show()

    }

    interface Listener {
        fun onClick()
    }
}