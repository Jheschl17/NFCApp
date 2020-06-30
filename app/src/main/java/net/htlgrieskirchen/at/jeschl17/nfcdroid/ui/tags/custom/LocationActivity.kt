package net.htlgrieskirchen.at.jeschl17.nfcdroid.ui.tags.custom

import android.nfc.NdefRecord
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_location.*
import kotlinx.android.synthetic.main.activity_text.button_cancel
import kotlinx.android.synthetic.main.activity_text.button_save
import kotlinx.android.synthetic.main.activity_text.text_latitude
import net.htlgrieskirchen.at.jeschl17.nfcdroid.R
import net.htlgrieskirchen.at.jeschl17.nfcdroid.util.showAlertDialog

class LocationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        // Initialize cancel button
        button_cancel.setOnClickListener {
            this.finish()
        }

        // Initialize save button
        button_save.setOnClickListener {
            val lat = text_latitude.text.toString().toDoubleOrNull()
            val lon = text_longitude.text.toString().toDoubleOrNull()

            if (lat == null || lon == null) {
                showAlertDialog(
                    R.layout.dialog_invalid_location,
                    R.string.invalid_location_headline,
                    this
                )
                return@setOnClickListener
            }

            val record = NdefRecord.createUri("geo:$lat,$lon")
            customProfileActivityInstance?.records?.add(record)
            customProfileActivityInstance?.adapter?.notifyDataSetChanged()
            dataTypeActivityInstance?.finish()
            this.finish()
        }
    }
}