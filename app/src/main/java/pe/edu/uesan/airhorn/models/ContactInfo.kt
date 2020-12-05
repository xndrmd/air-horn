package pe.edu.uesan.airhorn.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContactInfo(
        val contentId: String,
        val name: String,
        val phoneNumbers: List<PhoneNumber>,
        val photoThumbnailUri: String? = null,
        val photoUri: String? = null,
        val id: Long? = null
): Parcelable
