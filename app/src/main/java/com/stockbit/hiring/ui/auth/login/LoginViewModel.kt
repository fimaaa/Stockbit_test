package com.stockbit.hiring.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.stockbit.common.base.BaseViewModel
import com.stockbit.common.utils.ValidateUtill
import com.stockbit.hiring.R
import com.stockbit.model.common.UIText

class LoginViewModel(): BaseViewModel() {

    private val _observableEmail = MutableLiveData<Boolean>()
    val observableEmail: LiveData<Boolean>
        get() = _observableEmail

    private val _observablePassword = MutableLiveData<Boolean>()
    val observablePassword: LiveData<Boolean>
        get() = _observablePassword

    fun validateLogin(
        email: String,
        password: String
    ) {
        if(!validateEmail(email) || !validatePassword(password)) {
            return
        }
        goToCryptoList()
    }

    private fun goToCryptoList() {
        navigate(LoginFragmentDirections.actionLoginFragmentToHomeCryptoFragment())
    }

    fun validateEmail(email: String): Boolean {
        val valid = email.isNotEmpty()
        if(!valid) (navigator as LoginNavigator).setEmailError(UIText.StringResource(R.string.error_email_notvalid))
        return valid
    }

    private fun validatePassword(password: String): Boolean {
        val valid = password.isNotEmpty()
        if(!valid)  (navigator as LoginNavigator).setPasswordError(UIText.StringResource(R.string.error_password_notvalid))
        return valid
    }
}