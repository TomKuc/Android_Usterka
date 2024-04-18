package com.ardgahgroup.usterka.ui.activities.login

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ardgahgroup.usterka.R
import com.ardgahgroup.usterka.data.LoginRepository
import com.ardgahgroup.usterka.data.model.api.ApiUserData
import com.ardgahgroup.usterka.data.model.api.LoginModel
import com.ardgahgroup.usterka.data.model.login.UserData
import com.ardgahgroup.usterka.data.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.isEmpty()
    }

    // Validate data entered by user
    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.empty_email_textView)
        } else if (isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.empty_password_textView)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    fun login(username: String, password: String) {
        authenticateUser(username, password)
    }

    // API request to get user token
    private fun authenticateUser(userEmail: String, userPassword: String) {
        val retrofitBuilder = RetrofitClient.getInstanceLogin()
        val loginData = LoginModel(email = userEmail, password = userPassword)

        val retrofitData = retrofitBuilder.Login(loginData)

        retrofitData.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                val responseBody = response.body()
                if (responseBody != null) {
                    getUserData(responseBody)

                } else {
                    _loginResult.value = LoginResult(error = R.string.incorrect_login_credentials)
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                _loginResult.value = LoginResult(error = R.string.server_issue_dialog_title)
                Log.d("LoginActivity", "Error while requesting user token: " + t.message)
            }
        })
    }

    // API request to get all data
    private fun getUserData(token: String) {
        val retrofitBuilder = RetrofitClient.getInstance(token)
        val retrofitData = retrofitBuilder.getAccountInfo()

        retrofitData.enqueue(object : Callback<ApiUserData> {
            override fun onResponse(call: Call<ApiUserData>, response: Response<ApiUserData>) {
                val responseBody = response.body()

                if (responseBody != null) {
                    LoginRepository.userData =
                        UserData(responseBody.id, responseBody.dział, responseBody.rola, token)

                    _loginResult.value = LoginResult(success = true)
                }
            }

            override fun onFailure(call: Call<ApiUserData>, t: Throwable) {
                _loginResult.value = LoginResult(error = R.string.server_issue_dialog_title)
                Log.d("LoginActivity", "Error while requesting user data: " + t.message)
            }
        })
    }
}