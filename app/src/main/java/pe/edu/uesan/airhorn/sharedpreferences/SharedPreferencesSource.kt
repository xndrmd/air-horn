package pe.edu.uesan.airhorn.sharedpreferences

import android.content.SharedPreferences
import javax.inject.Inject

class SharedPreferencesSource @Inject constructor(private val sharedPreferences: SharedPreferences) {
    fun putInt(key: String, value: Int) = sharedPreferences.edit().putInt(key, value).apply()
    fun getIntLiveData(key: String) = sharedPreferences.intLiveData(key, 0)
    fun getInt(key: String) = sharedPreferences.getInt(key, 0)

    fun putString(key: String, value: String) = sharedPreferences.edit().putString(key, value).apply()
    fun getStringLiveData(key: String) = sharedPreferences.stringLiveData(key, "")
    fun getString(key: String) = sharedPreferences.getString(key, "")

    fun putBoolean(key: String, value: Boolean) = sharedPreferences.edit().putBoolean(key, value).apply()
    fun getBooleanLiveData(key: String) = sharedPreferences.booleanLiveData(key, false)
    fun getBoolean(key: String) = sharedPreferences.getBoolean(key, false)
}