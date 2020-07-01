package net.htlgrieskirchen.at.jeschl17.nfcdroid.ui.tags.custom

import android.nfc.NdefRecord
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_text.button_cancel
import kotlinx.android.synthetic.main.activity_text.button_save
import kotlinx.android.synthetic.main.activity_website_link.*
import net.htlgrieskirchen.at.jeschl17.nfcdroid.R

class WebsiteLinkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_website_link)

        // Initialize cancel button
        button_cancel.setOnClickListener {
            this.finish()
        }

        // Initialize save button
        button_save.setOnClickListener {
            try {
                val text = text_link.text.toString()
                val record = NdefRecord.createUri(text)
                customProfileActivityInstance?.records?.add(record)
                customProfileActivityInstance?.adapter?.notifyDataSetChanged()
                dataTypeActivityInstance?.finish()
                this.finish()
            } catch (e: Exception) {
                text_link.error = resources.getString(R.string.invalid_link)
            }
        }
    }
}