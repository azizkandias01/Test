package com.azizapp.test.utill

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


object Session {

    private const val TOKEN_BEARER_KEY = "token"

    // At the top level of your kotlin file:

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore("session")
    var dataStore: DataStore<Preferences>? = null

    fun init(context: Context) {
        dataStore = context.dataStore
    }

    fun unset() {
        GlobalScope.launch {
            dataStore?.edit { session ->
                session.clear()
            }
        }
    }

    var bearer: String?
        get() =
            runBlocking {
                val dataStoreKey = stringPreferencesKey(TOKEN_BEARER_KEY)
                val preferences = dataStore?.data?.first()

                preferences?.get(dataStoreKey)
            }
        set(value) {
            GlobalScope.launch {
                val da = stringPreferencesKey(TOKEN_BEARER_KEY)
                dataStore?.edit { session ->
                    session[da] = value ?: ""
                }
            }
        }

}