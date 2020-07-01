package net.htlgrieskirchen.at.jeschl17.nfcdroid.ui.tags.custom

import android.nfc.NdefRecord
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_address.*
import kotlinx.android.synthetic.main.activity_text.button_cancel
import kotlinx.android.synthetic.main.activity_text.button_save
import net.htlgrieskirchen.at.jeschl17.nfcdroid.R
import net.htlgrieskirchen.at.jeschl17.nfcdroid.util.DetermineLocationTask
import net.htlgrieskirchen.at.jeschl17.nfcdroid.util.showAlertDialog

class AddressActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)

        // Initialize cancel button
        button_cancel.setOnClickListener {
            this.finish()
        }

        // Initialize save button
        button_save.setOnClickListener {
            val text = text_address.text.toString()
            val location = DetermineLocationTask()
                .execute(text)
                .get()
            if (location == null) {
                showAlertDialog(R.layout.dialog_location_failed,
                    R.string.determine_location_failed,
                    this)
                return@setOnClickListener
            }
            val record = NdefRecord.createUri("geo:${location.lat},${location.lon}")
            customProfileActivityInstance?.records?.add(record)
            customProfileActivityInstance?.adapter?.notifyDataSetChanged()
            dataTypeActivityInstance?.finish()
            this.finish()
        }
    }
}