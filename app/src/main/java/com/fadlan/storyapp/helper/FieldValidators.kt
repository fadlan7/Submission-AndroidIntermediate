package com.fadlan.storyapp.helper

import android.util.Patterns
import java.util.regex.Pattern

object FieldValidators {

    /**
     * checking pattern of email
     * @param email input email
     * @return true if matches with email address else false
     */
    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}