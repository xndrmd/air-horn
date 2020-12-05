package pe.edu.uesan.airhorn.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PhoneDao {
    @Query("SELECT * FROM phone")
    fun getAll(): LiveData<List<Phone>>

    @Query("SELECT * FROM phone WHERE CONTENT_ID = :contentId")
    suspend fun getByContentId(contentId: String): Phone

    @Insert
    suspend fun insertAll(vararg phone: Phone)

    @Delete
    suspend fun delete(phone: Phone)
}
