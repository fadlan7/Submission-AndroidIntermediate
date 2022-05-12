package com.fadlan.storyapp.helper

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object Constanta {
    val TOKEN_KEY = stringPreferencesKey("token")
    val STATE_KEY = booleanPreferencesKey("state")

    const val EXTRA_USER_NAME = "name"
    const val EXTRA_UPLOAD_DATE = "upload_date"
    const val EXTRA_CAPTION = "caption"
    const val EXTRA_IMAGE = "image"
}