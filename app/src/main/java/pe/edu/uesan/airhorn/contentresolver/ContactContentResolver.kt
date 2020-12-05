package pe.edu.uesan.airhorn.contentresolver

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.ContactsContract
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import pe.edu.uesan.airhorn.avatargenerator.AvatarGenerator
import pe.edu.uesan.airhorn.models.ContactInfo
import pe.edu.uesan.airhorn.models.PhoneNumber

class ContactContentResolver {
    companion object {
        private lateinit var phoneUtils: PhoneNumberUtil

        fun contacts(context: Context): List<ContactInfo> {
            phoneUtils = PhoneNumberUtil.createInstance(context)

            return getContacts(context)
        }

        fun photo(context: Context, key: String, photoData: String?): Bitmap {
            return photoData?.let { loadContactPhoto(context, photoData) } ?: createContactPhoto(context, key)
        }

        private fun getContacts(context: Context): List<ContactInfo> {
            val list = mutableListOf<ContactInfo>()

            val contentResolver: ContentResolver = context.contentResolver

            val projection = arrayOf(
                ContactsContract.Profile._ID,
                ContactsContract.Profile.DISPLAY_NAME_PRIMARY,
                ContactsContract.Profile.HAS_PHONE_NUMBER,
                ContactsContract.Profile.LOOKUP_KEY,
                ContactsContract.Profile.PHOTO_THUMBNAIL_URI,
                ContactsContract.Profile.PHOTO_URI
            )

            val cursor = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                projection,
                null,
                null,
                null
            )

            while (cursor?.moveToNext() == true) {
                val hasPhoneNumber = cursor.getInt(2) == 1

                if (hasPhoneNumber) {
                    val id = cursor.getString(0)
                    val name = cursor.getString(1)
                    val photoThumbnailUri = cursor.getString(4)
                    val photoUri = cursor.getString(5)

                    val phoneNumbers = getPhoneNumbers(context, id);

                    if (phoneNumbers.isNotEmpty()) {
                        list.add(ContactInfo(id, name, phoneNumbers, photoThumbnailUri, photoUri))
                    }
                }
            }

            cursor?.close()

            return list
        }

        private fun getPhoneNumbers(context: Context, id: String): List<PhoneNumber> {
            val list = mutableListOf<PhoneNumber>()

            val contentResolver: ContentResolver = context.contentResolver

            val projection = arrayOf(
                ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER
            )

            val cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection,
                "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                arrayOf(id),
                null
            )

            val defaultCountryCode = phoneUtils.getRegionCodeForCountryCode(51)

            while (cursor?.moveToNext() == true) {
                val number = cursor.getString(1) ?: continue;

                val parsedNumber = phoneUtils.parse(number, defaultCountryCode)
                val isValidNumber = phoneUtils.isValidNumber(parsedNumber)
                val numberType = phoneUtils.getNumberType(parsedNumber)
                val isMobile = numberType == PhoneNumberUtil.PhoneNumberType.MOBILE || numberType == PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE;
                val formattedNumber = phoneUtils.format(parsedNumber, PhoneNumberUtil.PhoneNumberFormat.E164)

                if (isValidNumber && isMobile && list.all { it.number != formattedNumber }) {
                    list.add(PhoneNumber(id, formattedNumber))
                }
            }

            cursor?.close()

            return list;
        }

        private fun loadContactPhoto(context: Context, photoData: String): Bitmap? {
            val uri = Uri.parse(photoData)
            val asset = context.contentResolver.openAssetFileDescriptor(uri, "r")
            return asset?.fileDescriptor?.let { fileDescriptor -> BitmapFactory.decodeFileDescriptor(fileDescriptor, null, null) }
        }

        private fun createContactPhoto(context: Context, name: String): Bitmap {
            return AvatarGenerator.avatarImage(context, 150, AvatarGenerator.RECTANGLE, name)
        }
    }
}