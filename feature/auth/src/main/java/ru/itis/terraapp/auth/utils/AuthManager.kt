/*
package ru.itis.terraapp.auth.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AuthManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "auth_prefs", Context.MODE_PRIVATE
    )

    companion object {
        private const val KEY_USERID = "userId"
    }

    fun saveUserId(userId: Int) {
        sharedPreferences.edit()
            .putInt(KEY_USERID, userId)
            .apply()
        Log.i("AuthManager", "save userId = $userId")
    }

    fun getUserId(): Int? {
        Log.i("AuthManager", "get userId = ${sharedPreferences.getInt(KEY_USERID, -1)}")
        return sharedPreferences.getInt(KEY_USERID, -1)
    }

    fun clearUserId() {
        sharedPreferences.edit()
            .remove(KEY_USERID)
            .apply()
    }
}*/
