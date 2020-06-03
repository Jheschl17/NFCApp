package net.htlgrieskirchen.at.jeschl17.nfcdroid.util

import android.content.Context
import android.nfc.NdefMessage
import android.nfc.Tag
import android.nfc.TagLostException
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import net.htlgrieskirchen.at.jeschl17.nfcdroid.R
import java.lang.Exception
import kotlin.coroutines.coroutineContext

/**
 * Writes the provided message to to provided tag.
 *
 * @param context the context required for writing to the tag
 * @param tag the tag to write to
 * @param message to NDEF message to write to the tag
 *
 * @return true if writing succeeds, false otherwise
 *
 * @see shanetully https://www.shanetully.com/2012/12/writing-custom-data-to-nfc-tags-with-android-example/
 */
fun writeTag(context: Context, tag: Tag, message: NdefMessage): Boolean {
    try {
        val ndef = Ndef.get(tag)
        if (ndef != null) { // tag is empty
            ndef.connect()

            if (!ndef.isWritable) {
                showAlertDialog(R.layout.dialog_no_write, R.string.nfc_tag_write_failed, context)
                return false
            }

            val size = message.toByteArray().size
            if (ndef.maxSize < size) {
                showAlertDialog(
                    R.layout.dialog_not_enough_storage,
                    R.string.nfc_tag_write_failed,
                    context
                )
                return false
            }

            try {
                ndef.writeNdefMessage(message)

                showAlertDialog(R.layout.dialog_write_successful, R.string.success, context)
                return true
            } catch (tle: TagLostException) {
                showAlertDialog(
                    R.layout.dialog_write_tag_disconnected,
                    R.string.nfc_tag_write_failed,
                    context
                )
            }
        } else { // tag must be formatted
            val format = NdefFormatable.get(tag)
            try {
                format.connect()
                format.format(message)

                showAlertDialog(R.layout.dialog_write_successful, R.string.success, context)
                return true
            } catch (tle: TagLostException) {
                showAlertDialog(
                    R.layout.dialog_write_tag_disconnected,
                    R.string.nfc_tag_write_failed,
                    context
                )
            }
        }
    } catch (ex: Exception) {
        showAlertDialog(
            R.layout.dialog_write_failed_unknown,
            R.string.nfc_tag_write_failed,
            context
        )
    }
    return false
}