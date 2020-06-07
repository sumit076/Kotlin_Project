package com.example.loginpage.ui.login

import android.provider.ContactsContract
import androidx.core.os.postDelayed
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.security.MessageDigest
import java.util.logging.Handler
import javax.security.auth.callback.Callback

class LoginViewModel : ViewModel() {
    var state: LoginViewState = LoginViewState.Idle

    private val _stateLiveData = MutableLiveData<LoginViewState>(
        LoginViewState.Idle
    )
    val stateLiveData: LiveData<LoginViewState> = _stateLiveData

    fun onSubmit(email: String, password: String) {
        when {
            isValidInvalid(email) -> _stateLiveData.value = LoginViewState.Failed(
                message = "Email is invalid"
            )
            isPasswordInvalid(password) -> _stateLiveData.value = LoginViewState.Failed(
                message = "Password is invalid"
            )
            else -> {
                _stateLiveData.value = LoginViewState.Progress
                processLogin { hasSucceed ->
                    if (hasSucceed) {
                        _stateLiveData.value = LoginViewState.Succeed(
                            message = "Yay welcome back"
                        )
                    } else {
                        _stateLiveData.value = LoginViewState.Failed(
                            message = "Login has Failed"
                        )
                    }

                }

            }
        }
    }

    private fun processLogin(callback: (Boolean) -> Unit) {
        android.os.Handler().postDelayed(2000) {
            callback(true)
        }
    }

    private fun isPasswordInvalid(password: String): Boolean {

        return password.count() < 4
    }

    private fun isValidInvalid(email: String): Boolean {

        return email.singleOrNull { it == '@' } == null
    }
}

sealed class LoginViewState {

    object Idle: LoginViewState()

    object Progress: LoginViewState()

    data class Failed(val message: String): LoginViewState()

    data class Succeed(val message: String): LoginViewState()
}