package pe.edu.uesan.airhorn.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import pe.edu.uesan.airhorn.contentresolver.ContactInfoRepository
import pe.edu.uesan.airhorn.data.ContactRepository
import pe.edu.uesan.airhorn.models.ContactInfo
import pe.edu.uesan.airhorn.models.PhoneNumber

class ContactListAllViewModel @ViewModelInject internal constructor(
    contactInfoRepository: ContactInfoRepository
): ViewModel() {
    val contacts = contactInfoRepository.getAll()
}