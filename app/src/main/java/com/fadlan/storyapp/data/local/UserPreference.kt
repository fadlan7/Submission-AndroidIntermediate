package com.fadlan.storyapp.data.local

import android.content.Context
import com.fadlan.storyapp.helper.Constanta.STATE_KEY
import com.fadlan.storyapp.helper.Constanta.TOKEN_KEY
import com.fadlan.storyapp.helper.Constanta.USER_PREF

class UserPreference(context: Context) {

    private val preferences = context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE)
    private val pref_edit = preferences.edit()

    fun saveUser(user: UserModel) {
        pref_edit.putString(TOKEN_KEY, user.token)
        pref_edit.putBoolean(STATE_KEY, user.isLogin)
        pref_edit.apply()
    }

    fun getUser(): UserModel {
        return UserModel(
            preferences.getString(TOKEN_KEY, "") ?: "",
            preferences.getBoolean(STATE_KEY,false)
        )
    }

     fun logout() {
         pref_edit.remove(TOKEN_KEY)
         pref_edit.putBoolean(STATE_KEY, false)
         pref_edit.apply()
    }

}