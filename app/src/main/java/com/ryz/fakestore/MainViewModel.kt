package com.ryz.fakestore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ryz.fakestore.data.local.LocalDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val localDataSource: LocalDataSource) :
    ViewModel() {

    val token: Flow<String?> = localDataSource.authToken

    fun saveToken(token: String) = viewModelScope.launch {
        localDataSource.saveTokenAuth(token)
    }
}