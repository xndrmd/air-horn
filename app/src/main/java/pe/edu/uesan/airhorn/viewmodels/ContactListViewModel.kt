package pe.edu.uesan.airhorn.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import pe.edu.uesan.airhorn.contentresolver.ContactInfoRepository
import pe.edu.uesan.airhorn.data.ContactRepository


class ContactListViewModel @ViewModelInject internal constructor(
    contactRepository: ContactRepository,
    contactInfoRepository: ContactInfoRepository
): ViewModel() {
    val contacts = contactInfoRepository.getAll().switchMap { contacts ->
        contactRepository.getAllWithPhones().map { contactsWithPhone ->
            contactsWithPhone.mapNotNull { contactWithPhones ->
                contacts.find { contact ->
                    contact.contentId == contactWithPhones.contact.contentId &&
                    contact.phoneNumbers.any { phoneNumber -> contactWithPhones.phones.any { phone -> phone.contentId == phoneNumber.contentId } }
                }
            }.sortedBy { it.name }
        }
    }
}