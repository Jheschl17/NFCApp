package net.htlgrieskirchen.at.jeschl17.nfcdroid.db

import android.nfc.NdefMessage
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class NfcTag(
    val name: String,
    val ndefMessage: NdefMessage?,
    val tagId: String?,
    val technologies: String?,
    val dataFormat: String?,
    val memorySpace: String?,
    val atqa: String?,
    val sak: String?,
    val editable: String?,
    val canMakeReadOnly: String?) : Parcelable