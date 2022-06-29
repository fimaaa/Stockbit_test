package com.stockbit.common.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

object ValidateUtill {
    private const val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&*()+=!_-{};:'\",<.>/?\\|])(?=\\S+$).{4,}$"

    fun isValidPassword(password: String): Boolean {
        val pattern = Pattern.compile(PASSWORD_PATTERN)
        val matcher: Matcher = pattern.matcher(password)
        return matcher.matches()
    }
}