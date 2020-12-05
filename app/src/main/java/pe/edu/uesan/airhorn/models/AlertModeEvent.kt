package pe.edu.uesan.airhorn.models

sealed class AlertModeEvent {
    object START: AlertModeEvent()
    object STOP: AlertModeEvent()
}