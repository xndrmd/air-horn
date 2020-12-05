package pe.edu.uesan.airhorn.data

import androidx.room.*

@Entity(tableName = "contact")
data class Contact(
    @ColumnInfo(name = "CONTENT_ID") val contentId: String?
) {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "_ID") var id: Long = 0
}