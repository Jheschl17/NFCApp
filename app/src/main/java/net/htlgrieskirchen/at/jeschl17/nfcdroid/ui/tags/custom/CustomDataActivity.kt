package net.htlgrieskirchen.at.jeschl17.nfcdroid.ui.tags.custom

import android.nfc.NdefRecord
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_custom_data.*
import kotlinx.android.synthetic.main.activity_text.button_cancel
import kotlinx.android.synthetic.main.activity_text.button_save
import net.htlgrieskirchen.at.jeschl17.nfcdroid.R
import net.htlgrieskirchen.at.jeschl17.nfcdroid.util.showAlertDialog

class CustomDataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_data)

        // Initialize cancel button
        button_cancel.setOnClickListener {
            this.finish()
        }

        // Initialize save button
        button_save.setOnClickListener {
            val type = text_type.text.toString()
            val subtype = text_subtype.text.toString()
            val data = text_data.text.toString()

            try {
                val record = NdefRecord.createMime("$type/$subtype", data.toByteArray())
                customProfileActivityInstance?.records?.add(record)
                customProfileActivityInstance?.adapter?.notifyDataSetChanged()
                dataTypeActivityInstance?.finish()
                this.finish()
            } catch (e: IllegalArgumentException) {
                showAlertDialog(R.layout.dialog_invald_data, R.string.invalid_data, this)
            }
        }
    }
}