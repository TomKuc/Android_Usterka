package com.ardgahgroup.usterka.ui.activities.login


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ardgahgroup.usterka.R
import com.ardgahgroup.usterka.databinding.ActivityLoginBinding
import com.ardgahgroup.usterka.helpers.LoadingBarDialog
import com.ardgahgroup.usterka.helpers.SharedPreferences
import com.ardgahgroup.usterka.helpers.afterTextChanged
import com.ardgahgroup.usterka.ui.activities.UserMenuActivity
import es.dmoral.toasty.Toasty


class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loadingProgressBar: LoadingBarDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.userEmail
        val password = binding.password
        val login = binding.login
        val rememberMe = binding.rememberMeSwitch

        val emailData = SharedPreferences(this@LoginActivity)
        val email = emailData.getSavedEmail()

        // set saved email
        autoScaleEditText(username)
        username.setText(email)
        if (email != "") {
            rememberMe.isChecked = true
        }

        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        loadingProgressBar = LoadingBarDialog(this@LoginActivity, binding.root, window)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password are valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
                password.text.clear()
                loadingProgressBar.hideLoadingCircle()
            }
            if (loginResult.success) {
                startNewActivity(username.text.toString())
            }
            setResult(Activity.RESULT_OK)
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
            autoScaleEditText(username)
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                        loadingProgressBar.showLoadingCircle()
                        loadingProgressBar.setTextViewText(getString(R.string.login_screen_progress_bar_text))
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                    }
                }
                false
            }

            login.setOnClickListener {
                loadingProgressBar.showLoadingCircle()
                loadingProgressBar.setTextViewText(getString(R.string.login_screen_progress_bar_text))
                loginViewModel.login(username.text.toString(), password.text.toString())
            }
        }
    }

    private fun startNewActivity(username: String) {
        val emailData = SharedPreferences(this)
        when (binding.rememberMeSwitch.isChecked) {
            true -> emailData.setEmail(username)
            false -> emailData.setEmail("")
        }
        val intent = Intent(this, UserMenuActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toasty.error(
            this,
            errorString,
            Toast.LENGTH_LONG,
            true
        ).show()
    }

    private fun autoScaleEditText(editText: EditText) {
        val length = editText.length()
        if (length <= 18) {
            editText.textSize = 18F
        } else {
            if (length in 19..23) {
                editText.textSize = (editText.width / 50).toFloat()
            } else {
                editText.textSize = (editText.width / 57).toFloat()
            }
        }
    }
}