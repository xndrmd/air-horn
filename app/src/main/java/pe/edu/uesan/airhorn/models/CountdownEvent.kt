package pe.edu.uesan.airhorn.models

sealed class CountdownEvent {
    object START: CountdownEvent()
    object COMPLETE: CountdownEvent()
    object STOP: CountdownEvent()
}