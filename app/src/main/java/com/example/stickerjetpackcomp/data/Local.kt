package com.example.stickerjetpackcomp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class Local(private val context: Context) {

    // to make sure there's only one instance
    companion object {
        private val Context.dataStoree: DataStore<Preferences> by preferencesDataStore("userLanguage")
        val LANGUAGE_KEY = intPreferencesKey("userLanguage")
    }

    //get the saved email
    val getLanguage: Flow<Int?> = context.dataStoree.data
        .map { preferences ->
            preferences[LANGUAGE_KEY] ?: 0
        }

    //save email into datastore
    suspend fun saveLanguage(lang: Int) {
        context.dataStoree.edit { preferences ->
            preferences[LANGUAGE_KEY] = lang
        }
    }
}