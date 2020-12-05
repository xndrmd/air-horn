package pe.edu.uesan.airhorn.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import pe.edu.uesan.airhorn.services.AlertModeService
import pe.edu.uesan.airhorn.services.CountdownService
import pe.edu.uesan.airhorn.sharedpreferences.SharedPreferencesRepository
import pe.edu.uesan.airhorn.utilities.SHARED_PREFERENCES_EVENT_SHAKE

class HomeViewModel @ViewModelInject internal constructor(
        sharedPreferencesRepository: SharedPreferencesRepository
): ViewModel() {
    val isShakeEventAction = sharedPreferencesRepository
            .getIntLiveData(SHARED_PREFERENCES_EVENT_SHAKE)
            .map { it == 1 }
    val title = MediatorLiveData<Any>()
    val millis = MediatorLiveData<Long>()

    init {
        title.addSource(CountdownService.countdownEvent) { value ->
            title.value = value
        }

        title.addSource(AlertModeService.alertModeEvent) { value ->
            title.value = value
        }

        millis.addSource(CountdownService.millisUntilFinished) { value ->
            millis.value = value
        }

        millis.addSource(AlertModeService.elapsedTimeMillis) { value ->
            millis.value = value
        }
    }
}
