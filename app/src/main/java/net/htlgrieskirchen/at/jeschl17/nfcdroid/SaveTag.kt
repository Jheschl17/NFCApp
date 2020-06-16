package net.htlgrieskirchen.at.jeschl17.nfcdroid

import android.nfc.NdefMessage
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class SaveTag(
    val name: String,
    val ndefMessage: NdefMessage,
    val tagId: String?,
    val technologies: String?,
    val dataFormat: String?,
    val memorySpace: String?,
    val atqa: String?,
    val sak: String?,
    val editable: String?,
    val canMakeReadOnly: String?) : Parcelable