package pe.edu.uesan.airhorn.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "phone")
data class Phone(
    @ColumnInfo(name = "CONTENT_ID") val contentId: String?,
    @ColumnInfo(name = "CONTACT_ID") val contactId: Long
) {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "_ID") var id: Long = 0
}