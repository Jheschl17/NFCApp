package net.htlgrieskirchen.at.jeschl17.nfcdroid.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.Tag
import android.nfc.TagLostException
import android.nfc.tech.*
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.message_ndef.view.*
import net.htlgrieskirchen.at.jeschl17.nfcdroid.R
import net.htlgrieskirchen.at.jeschl17.nfcdroid.db.NfcTag
import java.lang.Exception

fun showAlertDialog(viewId: Int, titleId: Int, context: Context): AlertDialog {
    val dialog = AlertDialog.Builder(context)
        .setTitle(titleId)
        .setView(viewId)
        .setPositiveButton("Ok", null)
        .create()
    dialog.show()
    return dialog
}

/**
 * Converts a byte array to a Hex-String separated by ':'.
 *
 * @return
 */
fun ByteArray.toHexString() : String {
    return this.joinToString(":") {
        String.format("%02x", it)
    }
}

@ExperimentalStdlibApi
fun records(rawRecords: Array<NdefRecord>?, activity: Activity): List<View> {
    val messages = mutableListOf<View>()

    messages.add(
        inflate(R.layout.attribute_headline_data, activity))

    rawRecords?.forEach { messages.add(view(it, activity)) }

    if (messages.size == 1)
        messages.add(inflate(R.layout.attribute_no_data, activity))

    return messages
}

@ExperimentalStdlibApi
private fun view(message: NdefRecord, activity: Activity): View {
    return LayoutInflater.from(activity).inflate(R.layout.message_ndef, null).apply {
        text_headline.text = message.toMimeType()
        text_detail.text = message.payload.decodeToString()
    }
}

/**
 * Extract data from the given SaveTag object and convert it to its corresponding attribute Views.
 *
 * @param nfcTag the scanned tag
 * @param activity a activity which is required for certain operations
 * @return a list of attribute views according to the given tag object
 */
fun attributes(nfcTag: NfcTag, activity: Activity): List<View> {
    val attributes = mutableListOf<View>()

    attributes.add(
        inflate(R.layout.attribute_headline_details, activity))

    nfcTag.tagId?.let {
        attributes.add(
            view(it, R.layout.attribute_id, activity))
    }
    nfcTag.technologies?.let {
        attributes.add(
            view(it, R.layout.attribute_technologies, activity))
    }
    nfcTag.dataFormat?.let {
        attributes.add(
            view(it, R.layout.attribute_data_format, activity))
    }
    nfcTag.memorySpace?.let {
        attributes.add(
            view(it, R.layout.attribute_memory_information, activity))
    }
    nfcTag.atqa?.let {
        attributes.add(
            view(it, R.layout.attribute_atqa, activity))
    }
    nfcTag.sak?.let {
        attributes.add(
            view(it, R.layout.attribute_sak, activity))
    }
    nfcTag.editable?.let {
        attributes.add(
            view(it, R.layout.attribute_editable, activity))
    }
    nfcTag.canMakeReadOnly?.let {
        attributes.add(
            view(it, R.layout.attribute_read_only, activity))
    }

    return attributes
}

/**
 * Extract data from the given Tag object and convert it to its corresponding attribute Views.
 *
 * @param tag the scanned tag
 * @param activity a activity which is required for certain operations
 * @return a list of attribute views according to the given tag object
 */
fun attributes(tag: Tag, activity: Activity): List<View> {
    val attributes = mutableListOf<View>()

    attributes.add(
        inflate(R.layout.attribute_headline_details, activity))

    extractId(tag)?.let {
        attributes.add(
            view(it, R.layout.attribute_id, activity))
    }
    extractTechnologies(tag)?.let {
        attributes.add(
            view(it, R.layout.attribute_technologies, activity))
    }
    extractDataFormat(tag, activity)?.let {
        attributes.add(
            view(it, R.layout.attribute_data_format, activity))
    }
    extractSize(tag, activity)?.let {
        attributes.add(
            view(it, R.layout.attribute_memory_information, activity))
    }
    extractATQA(tag)?.let {
        attributes.add(
            view(it, R.layout.attribute_atqa, activity))
    }
    extractSAK(tag)?.let {
        attributes.add(
            view(it, R.layout.attribute_sak, activity))
    }
    extractWritable(tag, activity)?.let {
        attributes.add(
            view(it, R.layout.attribute_editable, activity))
    }
    extractCanBeMadeReadOnly(tag, activity)?.let {
        attributes.add(
            view(it, R.layout.attribute_read_only, activity))
    }

    return attributes
}

fun extractId(tag: Tag): String? {
    return when {
        tag.id.isNotEmpty() -> tag.id.toHexString()
        else -> null
    }
}

fun extractTechnologies(tag: Tag): String? {
    return when {
        tag.techList.isNotEmpty() -> tag.techList.joinToString(separator = ", ") {
            it.split(".").last()
        }
        else -> null
    }
}

fun extractDataFormat(tag: Tag, activity: Activity): String? {
    fun beautifyFormat(format: String): String {
        return when (format) {
            Ndef.NFC_FORUM_TYPE_1 -> activity.getString(R.string.org_nfcorum_ndef_type1)
            Ndef.NFC_FORUM_TYPE_2 -> activity.getString(R.string.org_nfcorum_ndef_type2)
            Ndef.NFC_FORUM_TYPE_3 -> activity.getString(R.string.org_nfcorum_ndef_type3)
            Ndef.NFC_FORUM_TYPE_4 -> activity.getString(R.string.org_nfcorum_ndef_type4)
            Ndef.MIFARE_CLASSIC -> activity.getString(R.string.com_nxp_ndef_mifareclassic)
            "com.nxp.ndef.icodesli" -> activity.getString(R.string.com_nxp_ndef_icodesli)
            "android.ndef.unknown" -> activity.getString(R.string.android_ndef_unknown)
            else -> activity.getString(R.string.android_ndef_unknown)
        }
    }

    return when {
        Ndef.get(tag) != null -> beautifyFormat(Ndef.get(tag).type)
        else -> null
    }
}

fun extractATQA(tag: Tag): String? {
    return when {
        NfcA.get(tag) != null -> NfcA.get(tag).atqa.toHexString()
        else -> null
    }
}

fun extractSAK(tag: Tag): String? {
    return when {
        NfcA.get(tag) != null -> NfcA.get(tag).sak.toString()
        else -> null
    }
}

fun extractSize(tag: Tag, activity: Activity): String? {
    return when {
        Ndef.get(tag) != null -> "${Ndef.get(tag).maxSize} ${activity.getString(R.string.bytes)}"
        else -> null
    }
}

fun extractWritable(tag: Tag, activity: Activity): String? {
    return if (Ndef.get(tag) != null)
        when (Ndef.get(tag).isWritable) {
            true -> activity.getString(R.string.writable)
            false -> activity.getString(R.string.not_writable)
        }
    else
        null
}

fun extractCanBeMadeReadOnly(tag: Tag, activity: Activity): String? {
    return if (Ndef.get(tag) != null)
        when (Ndef.get(tag).canMakeReadOnly()) {
            true -> activity.getString(R.string.yes)
            false -> activity.getString(R.string.no)
        }
    else
        null
}

fun inflate(layoutId: Int, activity: Activity) : View {
    return LayoutInflater.from(activity).inflate(layoutId, null)
}

private fun view(detail: String, layoutId: Int, activity: Activity): View {
    return LayoutInflater.from(activity).inflate(layoutId, null).apply {
        findViewById<TextView>(R.id.text_detail).text = detail
    }
}

fun dp(dp: Float, context: Context): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
        .toInt()
}

fun toSaveTag(name: String, tag: Tag, ndefMessage: NdefMessage, activity: Activity): NfcTag {
    return NfcTag(
        name = name,
        ndefMessage = ndefMessage,
        tagId = extractId(tag),
        technologies = extractTechnologies(tag),
        dataFormat = extractDataFormat(tag, activity),
        memorySpace = extractSize(tag, activity),
        atqa = extractATQA(tag),
        sak = extractSAK(tag),
        editable = extractWritable(tag, activity),
        canMakeReadOnly = extractCanBeMadeReadOnly(tag, activity)
    )
}

fun displayError(errorMessage: String, view: TextView) {
    view.requestFocus()
    view.error = errorMessage
}

fun calculateSize(data: ByteArray): Int {
    return data.size * 8
}

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