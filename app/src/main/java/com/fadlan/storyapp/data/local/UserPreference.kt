package com.fadlan.storyapp.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.fadlan.storyapp.helper.Constanta.STATE_KEY
import com.fadlan.storyapp.helper.Constanta.TOKEN_KEY
import com.fadlan.storyapp.helper.Constanta.USER_PREF
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userPref")

class UserPreference @Inject constructor(@ApplicationContext val context: Context) {

    private val dataStore = context.dataStore

    suspend fun saveUser(user: UserModel) {
        dataStore.edit {
            it[TOKEN_KEY] = user.token
            it[STATE_KEY] = user.isLogin
        }
    }

    fun getUser(): Flow<UserModel> {
        return dataStore.data.map {
            UserModel(
                it[TOKEN_KEY] ?: "",
                it[STATE_KEY] ?: false
            )
        }
    }

    suspend fun logout() {
        dataStore.edit {
            it[TOKEN_KEY] = ""
            it[STATE_KEY] = false
        }
    }

}