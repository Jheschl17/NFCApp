package net.htlgrieskirchen.at.jeschl17.nfcdroid.ui.tags

import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.acticvity_custom_profile.*
import net.htlgrieskirchen.at.jeschl17.nfcdroid.R
import net.htlgrieskirchen.at.jeschl17.nfcdroid.db.AppDatabase
import net.htlgrieskirchen.at.jeschl17.nfcdroid.db.NfcTagDao
import net.htlgrieskirchen.at.jeschl17.nfcdroid.util.displayError
import net.htlgrieskirchen.at.jeschl17.nfcdroid.util.toSaveTag

class CustomProfileActivity : AppCompatActivity() {

    private lateinit var db: NfcTagDao

    private val records = mutableListOf<NdefRecord>()
    private lateinit var adapter: CustomProfileNdefAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acticvity_custom_profile)

        // Initialize database connection
        db = AppDatabase.getInstance(this).nfcTagDao!!

        // Initialize list view
        adapter = CustomProfileNdefAdapter(records, this, View.OnClickListener {
            records.add(NdefRecord.createTextRecord(null, "some textidabdidu"))
            adapter.notifyDataSetChanged()
        })
        list.adapter = adapter
        adapter.notifyDataSetChanged()

        // Initialize save button
        button_save_tag.setOnClickListener {
            val tagName = text_tag_name.text.toString()
            // Check if the tag can be saved
            // (name is not empty or already used and there is at least one ndef record)
            if (records.isEmpty()) {
                displayError(resources.getString(R.string.tag_must_not_be_empty), button_save_tag)
                displayError(resources.getString(R.string.tag_must_not_be_empty), text_button_error)
                return@setOnClickListener
            }
            if (tagName.isEmpty()) {
                displayError(resources.getString(R.string.name_required), text_tag_name)
                return@setOnClickListener
            }
            if (db.nfcTagByName(tagName) != null) {
                displayError(resources.getString(R.string.name_must_be_unique), text_tag_name)
                return@setOnClickListener
            }

            val tag = toSaveTag(
                name = text_tag_name.text.toString(),
                tag = null,
                ndefMessage = NdefMessage(records.toTypedArray()),
                activity = this
            )
            db.insertAll(tag)
            this.finish()
        }
    }
}