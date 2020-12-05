package pe.edu.uesan.airhorn.contentresolver

import android.content.ContentResolver
import android.provider.ContactsContract
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import pe.edu.uesan.airhorn.models.ContactInfo
import pe.edu.uesan.airhorn.models.PhoneNumber

class ContactInfoSource(
    private val contentResolver: ContentResolver,
    private val phoneUtils: PhoneNumberUtil
) {
    fun getAll(): List<ContactInfo> {
        return fetchContacts()
    }

    fun getById(id: String): ContactInfo? {
        return fetchContacts(id).firstOrNull()
    }

    private fun fetchContacts(id: String? = null): List<ContactInfo> {
        val list = mutableListOf<ContactInfo>()

        val projection = arrayOf(
            ContactsContract.Profile._ID,
            ContactsContract.Profile.DISPLAY_NAME_PRIMARY,
            ContactsContract.Profile.HAS_PHONE_NUMBER,
            ContactsContract.Profile.PHOTO_THUMBNAIL_URI,
            ContactsContract.Profile.PHOTO_URI
        )

        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            projection,
            if (id != null) "${ContactsContract.Profile._ID} = ?" else null,
            if (id != null) arrayOf(id) else null,
            null
        )

        while (cursor?.moveToNext() == true) {
            val hasPhoneNumber = cursor.getInt(cursor.getColumnIndex(ContactsContract.Profile.HAS_PHONE_NUMBER)) > 0
            if (!hasPhoneNumber) continue

            val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Profile._ID))
            val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Profile.DISPLAY_NAME_PRIMARY))
            val photoThumbnailUri = cursor.getString(cursor.getColumnIndex(ContactsContract.Profile.PHOTO_THUMBNAIL_URI))
            val photoUri = cursor.getString(cursor.getColumnIndex(ContactsContract.Profile.PHOTO_URI))

            val mobilePhoneNumbers = fetchMobilePhoneNumbers(id)
            if(mobilePhoneNumbers.isEmpty()) continue

            list.add(ContactInfo(id, name, mobilePhoneNumbers, photoThumbnailUri, photoUri))
        }

        cursor?.close()

        return list
    }

    private fun fetchMobilePhoneNumbers(contactId: String, countryCallingCode: Int = 51): List<PhoneNumber> {
        val list = mutableListOf<PhoneNumber>()

        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone._ID,
            ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER
        )

        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
            arrayOf(contactId),
            null
        )

        val defaultRegion = phoneUtils.getRegionCodeForCountryCode(countryCallingCode)

        while (cursor?.moveToNext() == true) {
            val normalizedNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER)) ?: continue
            val parsedNumber = phoneUtils.parse(normalizedNumber, defaultRegion)
            val isValidNumber = phoneUtils.isValidNumber(parsedNumber)
            val numberType = phoneUtils.getNumberType(parsedNumber)
            val isMobile = numberType == PhoneNumberUtil.PhoneNumberType.MOBILE || numberType == PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE;
            val formattedNumber = phoneUtils.format(parsedNumber, PhoneNumberUtil.PhoneNumberFormat.E164)

            if (isValidNumber && isMobile && list.all { it.number != formattedNumber }) {
                list.add(PhoneNumber(contactId, formattedNumber))
            }
        }

        cursor?.close()

        return list
    }
}