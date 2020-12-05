package pe.edu.uesan.airhorn.sharedpreferences

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesRepository @Inject constructor(
    private val sharedPreferencesSource: SharedPreferencesSource
) {
    fun getIntLiveData(key: String) = sharedPreferencesSource.getIntLiveData(key)
    fun getInt(key: String) = sharedPreferencesSource.getInt(key)
    fun putInt(key: String, value: Int) = sharedPreferencesSource.putInt(key, value)

    fun getStringLiveData(key: String) = sharedPreferencesSource.getStringLiveData(key)
    fun getString(key: String) = sharedPreferencesSource.getString(key)
    fun putString(key: String, value: String) = sharedPreferencesSource.putString(key, value)

    fun getBooleanLiveData(key: String) = sharedPreferencesSource.getBooleanLiveData(key)
    fun getBoolean(key: String) = sharedPreferencesSource.getBoolean(key)
    fun putBoolean(key: String, value: Boolean) = sharedPreferencesSource.putBoolean(key, value)
}