package pe.edu.uesan.airhorn.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import pe.edu.uesan.airhorn.sharedpreferences.SharedPreferencesRepository
import pe.edu.uesan.airhorn.utilities.SHARED_PREFERENCES_NAME
import pe.edu.uesan.airhorn.utilities.SHARED_PREFERENCES_PIN

class WelcomeViewModel @ViewModelInject internal constructor(
    private val sharedPreferencesRepository: SharedPreferencesRepository
): ViewModel() {
    val shouldNavigateToLogin = sharedPreferencesRepository
        .getStringLiveData(SHARED_PREFERENCES_NAME)
        .switchMap { name ->
            sharedPreferencesRepository.getStringLiveData(SHARED_PREFERENCES_PIN)
                .map { pin -> name.trim().isNotEmpty() && pin.trim().isNotEmpty() }
        }

    val shouldNavigateToHome = sharedPreferencesRepository
        .getStringLiveData(SHARED_PREFERENCES_NAME)
        .map { name -> name.trim().isNotEmpty() }

    fun updateStringPreference(key: String, value: String) {
        viewModelScope.launch {
            sharedPreferencesRepository.putString(key, value)
        }
    }
}