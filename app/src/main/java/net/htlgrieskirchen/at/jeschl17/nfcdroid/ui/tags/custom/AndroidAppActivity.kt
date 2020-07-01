package net.htlgrieskirchen.at.jeschl17.nfcdroid.ui.tags.custom

import android.nfc.NdefRecord
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_android_app.*
import kotlinx.android.synthetic.main.activity_text.*
import kotlinx.android.synthetic.main.activity_text.button_cancel
import kotlinx.android.synthetic.main.activity_text.button_save
import net.htlgrieskirchen.at.jeschl17.nfcdroid.R
import net.htlgrieskirchen.at.jeschl17.nfcdroid.util.showAlertDialog

class AndroidAppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_android_app)

        // Initialize cancel button
        button_cancel.setOnClickListener {
            this.finish()
        }

        // Initialize save button
        button_save.setOnClickListener {
            try {
                val text = text_app_name.text.toString()
                val record = NdefRecord.createApplicationRecord(text)
                customProfileActivityInstance?.records?.add(record)
                customProfileActivityInstance?.adapter?.notifyDataSetChanged()
                dataTypeActivityInstance?.finish()
                this.finish()
            } catch (e: Exception) {
                text_app_name.error = resources.getString(R.string.invalid_app_name_headline)
            }
        }
    }
}