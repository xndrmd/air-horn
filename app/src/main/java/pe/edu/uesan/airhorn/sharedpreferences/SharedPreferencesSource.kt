package pe.edu.uesan.airhorn.sharedpreferences

import android.content.SharedPreferences
import javax.inject.Inject

class SharedPreferencesSource @Inject constructor(private val sharedPreferences: SharedPreferences) {
    fun put(key: String, value: Int) = sharedPreferences.edit().putInt(key, value).apply()
    fun get(key: String) = sharedPreferences.intLiveData(key, 0)
}