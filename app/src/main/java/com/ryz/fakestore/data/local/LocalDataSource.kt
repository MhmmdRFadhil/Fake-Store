package com.ryz.fakestore.data.local

interface LocalDataSource{
    fun saveToken(token: String?)
    fun getToken(): String?
    fun saveUsername(username: String?)
    fun getUsername(): String?
    fun saveUserId(userId: Int)
    fun getUserId(): Int

}