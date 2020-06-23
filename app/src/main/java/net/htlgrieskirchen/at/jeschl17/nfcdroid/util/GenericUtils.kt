package net.htlgrieskirchen.at.jeschl17.nfcdroid.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.Tag
import android.nfc.tech.*
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.message_ndef.view.*
import net.htlgrieskirchen.at.jeschl17.nfcdroid.R
import net.htlgrieskirchen.at.jeschl17.nfcdroid.db.SaveTag

fun showAlertDialog(viewId: Int, titleId: Int, context: Context) {
   AlertDialog.Builder(context)
      .setTitle(titleId)
      .setView(viewId)
      .setPositiveButton("Ok", null)
      .create()
      .show()
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
 * @param saveTag the scanned tag
 * @param activity a activity which is required for certain operations
 * @return a list of attribute views according to the given tag object
 */
fun attributes(saveTag: SaveTag, activity: Activity): List<View> {
   val attributes = mutableListOf<View>()

   attributes.add(
      inflate(R.layout.attribute_headline_details, activity))

   saveTag.tagId?.let {
      attributes.add(
         view(it, R.layout.attribute_id, activity))
   }
   saveTag.technologies?.let {
      attributes.add(
         view(it, R.layout.attribute_technologies, activity))
   }
   saveTag.dataFormat?.let {
      attributes.add(
         view(it, R.layout.attribute_data_format, activity))
   }
   saveTag.memorySpace?.let {
      attributes.add(
         view(it, R.layout.attribute_memory_information, activity))
   }
   saveTag.atqa?.let {
      attributes.add(
         view(it, R.layout.attribute_atqa, activity))
   }
   saveTag.sak?.let {
      attributes.add(
         view(it, R.layout.attribute_sak, activity))
   }
   saveTag.editable?.let {
      attributes.add(
         view(it, R.layout.attribute_editable, activity))
   }
   saveTag.canMakeReadOnly?.let {
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
   extractTagType(tag)?.let {
      attributes.add(
         view(it, R.layout.attribute_tag_type, activity))
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

fun extractTagType(tag: Tag): String? {
   return null // TODO ?
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

fun toSaveTag(name: String, tag: Tag, ndefMessage: NdefMessage?, activity: Activity): SaveTag {
   return SaveTag(
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
