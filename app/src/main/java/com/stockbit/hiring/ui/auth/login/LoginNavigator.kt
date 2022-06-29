package com.stockbit.hiring.ui.auth.login

import com.stockbit.common.base.BaseNavigator
import com.stockbit.model.common.UIText

interface LoginNavigator: BaseNavigator {
    fun setEmailError(message: UIText)
    fun setPasswordError(message: UIText)
}