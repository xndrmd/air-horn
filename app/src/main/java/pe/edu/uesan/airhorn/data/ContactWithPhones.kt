package pe.edu.uesan.airhorn.data

import androidx.room.Embedded
import androidx.room.Relation

data class ContactWithPhones(
    @Embedded val contact: Contact,
    @Relation(
        parentColumn = "_ID",
        entityColumn = "CONTACT_ID"
    )
    val phones: List<Phone>
)