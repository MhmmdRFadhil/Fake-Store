package com.ryz.fakestore.data.local

import android.content.SharedPreferences

class LocalDataSourceImpl(private val sharedPreferences: SharedPreferences) : LocalDataSource {
    override fun saveToken(token: String?) {
        sharedPreferences.edit().putString(TOKEN, token).apply()
    }

    override fun getToken(): String? {
        return sharedPreferences.getString(TOKEN, null)
    }

    override fun saveUsername(username: String?) {
        sharedPreferences.edit().putString(USERNAME, username).apply()
    }

    override fun getUsername(): String? {
        return sharedPreferences.getString(USERNAME, null)
    }

    override fun saveUserId(userId: Int) {
        sharedPreferences.edit().putInt(USERID, userId).apply()
    }

    override fun getUserId(): Int {
        return sharedPreferences.getInt(USERID, 0)
    }

    companion object {
        const val TOKEN = "token"
        const val USERNAME = "username"
        const val USERID = "userid"
    }
}