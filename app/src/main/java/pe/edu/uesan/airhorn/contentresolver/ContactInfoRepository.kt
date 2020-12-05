package pe.edu.uesan.airhorn.contentresolver

import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactInfoRepository @Inject constructor(
    private val contactInfoSource: ContactInfoSource
) {
    fun getAll() = liveData {
        withContext(Dispatchers.IO) {
            emit(contactInfoSource.getAll())
        }
    }

    fun getById(id: String) = liveData {
        withContext(Dispatchers.IO) {
            emit(contactInfoSource.getById(id))
        }
    }
}