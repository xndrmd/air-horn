package pe.edu.uesan.airhorn.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhoneRepository @Inject constructor(
    private val phoneDao: PhoneDao
) {
    fun getAll() = phoneDao.getAll()

    suspend fun getByContentId(contentId: String) = phoneDao.getByContentId(contentId)

    suspend fun create(contentId: String, contactId: Long) {
        phoneDao.insertAll(Phone(contentId, contactId))
    }

    suspend fun delete(phone: Phone) {
        phoneDao.delete(phone)
    }
}
