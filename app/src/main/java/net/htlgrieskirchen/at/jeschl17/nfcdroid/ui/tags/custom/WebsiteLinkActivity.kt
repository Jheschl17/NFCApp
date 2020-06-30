package net.htlgrieskirchen.at.jeschl17.nfcdroid.ui.tags.custom

import android.nfc.NdefRecord
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_text.*
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
            val text = text_latitude.text.toString()
            val record = NdefRecord.createUri(text)
            customProfileActivityInstance?.records?.add(record)
            customProfileActivityInstance?.adapter?.notifyDataSetChanged()
            dataTypeActivityInstance?.finish()
            this.finish()
        }
    }
}