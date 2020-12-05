package pe.edu.uesan.airhorn.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepository @Inject constructor(
    private val contactDao: ContactDao
) {
    fun getAll() = contactDao.getAll()

    fun getByContentId(contentId: String) = contactDao.getByContentId(contentId)

    fun getAllWithPhones() = contactDao.getAllWithPhones()

    fun getAllWithPhonesByContentId(contentId: String) = contactDao.getAllWithPhonesByContentId(contentId)

    suspend fun create(contentId: String): Long {
        return contactDao.insert(Contact(contentId))
    }

    suspend fun delete(contact: Contact) {
        contactDao.delete(contact)
    }
}