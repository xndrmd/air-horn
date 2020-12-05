package pe.edu.uesan.airhorn.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EmailAddress(val contentId: String, val address: String, val id: Long? = null): Parcelable