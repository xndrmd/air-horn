package pe.edu.uesan.airhorn.viewmodels

import androidx.lifecycle.*
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.launch
import pe.edu.uesan.airhorn.contentresolver.ContactInfoRepository
import pe.edu.uesan.airhorn.data.ContactRepository
import pe.edu.uesan.airhorn.data.PhoneRepository
import pe.edu.uesan.airhorn.models.ContactInfo
import pe.edu.uesan.airhorn.models.PhoneNumber

class EditContactViewModel @AssistedInject internal constructor(
        private val contactRepository: ContactRepository,
        private val phoneRepository: PhoneRepository,
        contactInfoRepository: ContactInfoRepository,
        @Assisted private val contactContentId: String
): ViewModel() {
    val contact = contactInfoRepository.getById(contactContentId).switchMap { contactInfo ->
        contactRepository.getAllWithPhonesByContentId(contactInfo!!.contentId).map { contactWithPhones ->
            contactWithPhones?.let {
                val phoneNumbers = contactInfo.phoneNumbers.map { phoneNumber ->
                    val linked = contactWithPhones.phones.find { phone -> phone.contentId == phoneNumber.contentId }

                    PhoneNumber(phoneNumber.contentId, phoneNumber.number, linked?.id)
                }

                ContactInfo(contactInfo.contentId, contactInfo.name, phoneNumbers, contactInfo.photoThumbnailUri, contactInfo.photoUri, it.contact.id)
            } ?: contactInfo
        }
    }

    fun create(phoneContentId: String, contactId: Long?) {
        viewModelScope.launch {
            val id: Long = contactId ?: contactRepository.create(contactContentId)
            phoneRepository.create(phoneContentId, id)
        }
    }

    fun delete(phoneNumber: PhoneNumber) {
        viewModelScope.launch {
            val phone = phoneRepository.getByContentId(phoneNumber.contentId)
            phoneRepository.delete(phone)
        }
    }

    @AssistedInject.Factory
    interface AssistedFactory {
        fun create(contactContentId: String): EditContactViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            contactContentId: String
        ): ViewModelProvider.Factory = object: ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(contactContentId) as T
            }
        }
    }
}
