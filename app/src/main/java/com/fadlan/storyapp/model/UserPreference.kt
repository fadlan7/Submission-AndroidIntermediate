package com.fadlan.storyapp.model

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.fadlan.storyapp.helper.Constanta.STATE_KEY
import com.fadlan.storyapp.helper.Constanta.TOKEN_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


@SuppressLint("CommitPrefEdits")
class UserPreference(context: Context) {

    val preferences = context.getSharedPreferences("login_preferences", Context.MODE_PRIVATE)

    fun saveUser(user: UserModel) {
        preferences.edit().putString(TOKEN_KEY, user.token)
        preferences.edit().putBoolean(STATE_KEY, user.isLogin)
        preferences.edit().apply()
    }

    fun getUser(): UserModel {
        return UserModel(
            preferences.getString(TOKEN_KEY, "") ?: "",
            preferences.getBoolean(STATE_KEY,false)
        )
    }

//     fun login() {
//        dataStore.edit { preferences ->
//            preferences[STATE_KEY] = true
//        }
//    }

     fun logout() {
         preferences.edit().remove(TOKEN_KEY)
         preferences.edit().remove(STATE_KEY)
         preferences.edit().apply()
    }

}