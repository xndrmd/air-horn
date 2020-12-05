package pe.edu.uesan.airhorn.sharedpreferences

import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesRepository @Inject constructor(
    private val sharedPreferencesSource: SharedPreferencesSource
) {
    fun get(key: String) = sharedPreferencesSource.get(key)

    fun put(key: String, value: Int) = sharedPreferencesSource.put(key, value)
}