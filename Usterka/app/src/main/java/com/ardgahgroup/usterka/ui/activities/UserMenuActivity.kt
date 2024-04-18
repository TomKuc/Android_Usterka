package com.ardgahgroup.usterka.ui.activities

import android.app.AlertDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ardgahgroup.usterka.R
import com.ardgahgroup.usterka.data.LoginRepository
import com.ardgahgroup.usterka.data.model.selectPlace.SelectPlaceRowData
import com.ardgahgroup.usterka.databinding.ActivityUserMenuBinding
import com.ardgahgroup.usterka.helpers.LoadingBarDialog
import com.ardgahgroup.usterka.ui.activities.login.LoginActivity
import com.google.android.material.appbar.MaterialToolbar

import java.util.*


class UserMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserMenuBinding

    var submissionTitle: String = ""
    var submissionDescription: String = ""
    var submissionDate: String = ""
    var submissionTime: String = ""
    var submissionPlace: SelectPlaceRowData = SelectPlaceRowData("Wybierz miejsce", -1, -1)
    var submissionImageUri: Uri? = null
    var submissionDateSend: String = ""
    lateinit var loadingProgressBar: LoadingBarDialog
    lateinit var toolbar: MaterialToolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        submissionDate = SimpleDateFormat("dd MMM yyyy", Locale.forLanguageTag("PL")).format(Date())
        submissionTime = SimpleDateFormat("HH:mm", Locale.forLanguageTag("PL")).format(Date())
        submissionDateSend =
            SimpleDateFormat("yyyy/dd/MM", Locale.forLanguageTag("PL")).format(Date())

        binding = ActivityUserMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingProgressBar = LoadingBarDialog(this@UserMenuActivity, binding.root, window)
        setUpView()

    }

    private fun setUpView() {
        val navController = findNavController(R.id.fragmentContainerView)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        val bottomNav = binding.navigationBar

        toolbar = binding.toolbar
        toolbar.setupWithNavController(navController, appBarConfiguration)
        bottomNav.setupWithNavController(navController)

        bottomNav.menu.findItem(R.id.allSubmissions).isVisible =
            LoginRepository.userData.Role == "bhp"

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.logout -> {
                    logoutDialog()
                    true
                }
                R.id.refresh -> {
                    true
                }
                else -> false
            }
        }
    }

    fun clearSubmissionData() {
        submissionTitle = ""
        submissionDescription = ""
        submissionDate = SimpleDateFormat("dd MMM yyyy", Locale.forLanguageTag("PL")).format(Date())
        submissionTime = SimpleDateFormat("HH:mm", Locale.forLanguageTag("PL")).format(Date())
        submissionDateSend =
            SimpleDateFormat("yyyy/dd/MM", Locale.forLanguageTag("PL")).format(Date())
        submissionPlace = SelectPlaceRowData("Wybierz miejsce", -1, -1)
        submissionImageUri = null
    }

    private fun logoutDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.logout_message)
            .setCancelable(false)
            .setPositiveButton(R.string.yes_message) { _, _ ->
                LoginRepository.logout()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            .setNegativeButton(R.string.no_message) { dialog, _ ->
                dialog.cancel()
            }
        val alert: AlertDialog = builder.create()
        alert.show()
    }
}