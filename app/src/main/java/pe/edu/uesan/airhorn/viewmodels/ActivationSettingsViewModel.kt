package pe.edu.uesan.airhorn.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pe.edu.uesan.airhorn.sharedpreferences.SharedPreferencesRepository
import pe.edu.uesan.airhorn.utilities.SHARED_PREFERENCES_EVENT_POWER_BUTTON
import pe.edu.uesan.airhorn.utilities.SHARED_PREFERENCES_EVENT_SHAKE
import pe.edu.uesan.airhorn.utilities.SHARED_PREFERENCES_PARAMS_EVENTS_THRESHOLD
import pe.edu.uesan.airhorn.utilities.SHARED_PREFERENCES_PARAMS_SECONDS_THRESHOLD

class ActivationSettingsViewModel @ViewModelInject internal constructor(
    private val sharedPreferencesRepository: SharedPreferencesRepository
): ViewModel() {
    val config = MediatorLiveData<Pair<String, Int>>()

    init {
        config.addSource(sharedPreferencesRepository.get(SHARED_PREFERENCES_EVENT_SHAKE)) { value ->
            config.value = Pair(SHARED_PREFERENCES_EVENT_SHAKE, value)
        }

        config.addSource(sharedPreferencesRepository.get(SHARED_PREFERENCES_EVENT_POWER_BUTTON)) { value ->
            config.value = Pair(SHARED_PREFERENCES_EVENT_POWER_BUTTON, value)
        }

        config.addSource(sharedPreferencesRepository.get(SHARED_PREFERENCES_PARAMS_EVENTS_THRESHOLD)) { value ->
            config.value = Pair(SHARED_PREFERENCES_PARAMS_EVENTS_THRESHOLD, value)
        }

        config.addSource(sharedPreferencesRepository.get(SHARED_PREFERENCES_PARAMS_SECONDS_THRESHOLD)) { value ->
            config.value = Pair(SHARED_PREFERENCES_PARAMS_SECONDS_THRESHOLD, value)
        }
    }

    fun updatePreference(key: String, value: Int) {
        viewModelScope.launch {
            sharedPreferencesRepository.put(key, value)
        }
    }
}