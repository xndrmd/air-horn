package pe.edu.uesan.airhorn.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ContactDao {
    @Query("SELECT * FROM contact")
    fun getAll(): LiveData<List<Contact>>

    @Query("SELECT * FROM contact WHERE CONTENT_ID = :contentId")
    fun getByContentId(contentId: String): LiveData<Contact?>

    @Transaction
    @Query("SELECT * FROM contact")
    fun getAllWithPhones(): LiveData<List<ContactWithPhones>>

    @Transaction
    @Query("SELECT * FROM contact WHERE CONTENT_ID = :contentId")
    fun getAllWithPhonesByContentId(contentId: String): LiveData<ContactWithPhones?>

    @Insert
    suspend fun insert(contact: Contact): Long

    @Delete
    suspend fun delete(contact: Contact)
}
