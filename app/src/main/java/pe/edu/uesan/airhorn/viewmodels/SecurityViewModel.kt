package pe.edu.uesan.airhorn.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pe.edu.uesan.airhorn.sharedpreferences.SharedPreferencesRepository
import pe.edu.uesan.airhorn.utilities.SHARED_PREFERENCES_PIN
import pe.edu.uesan.airhorn.utilities.SHARED_PREFERENCES_USE_FINGERPRINT

class SecurityViewModel @ViewModelInject internal constructor(
    private val sharedPreferencesRepository: SharedPreferencesRepository
): ViewModel() {
    val config = MediatorLiveData<Pair<String, Any>>()

    init {
        config.addSource(sharedPreferencesRepository.getStringLiveData(SHARED_PREFERENCES_PIN)) { value ->
            config.value = Pair(SHARED_PREFERENCES_PIN, value)
        }

        config.addSource(sharedPreferencesRepository.getBooleanLiveData(SHARED_PREFERENCES_USE_FINGERPRINT)) { value ->
            config.value = Pair(SHARED_PREFERENCES_USE_FINGERPRINT, value)
        }
    }

    fun updateStringPreference(key: String, value: String) {
        viewModelScope.launch {
            sharedPreferencesRepository.putString(key, value)
        }
    }

    fun updateBooleanPreference(key: String, value: Boolean) {
        viewModelScope.launch {
            sharedPreferencesRepository.putBoolean(key, value)
        }
    }
}