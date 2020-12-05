package pe.edu.uesan.airhorn.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhoneNumber(val contentId: String, val number: String, val id: Long? = null): Parcelable