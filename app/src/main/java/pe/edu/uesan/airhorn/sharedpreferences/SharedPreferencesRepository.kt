package pe.edu.uesan.airhorn.sharedpreferences

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesRepository @Inject constructor(
    private val sharedPreferencesSource: SharedPreferencesSource
) {
    fun getLiveData(key: String) = sharedPreferencesSource.getLiveData(key)

    fun get(key: String) = sharedPreferencesSource.get(key)

    fun put(key: String, value: Int) = sharedPreferencesSource.put(key, value)
}