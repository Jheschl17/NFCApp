package net.htlgrieskirchen.at.jeschl17.nfcdroid.ui.tags.custom

import android.nfc.NdefRecord
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_text.*
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
            val text = text_latitude.text.toString()
            val location = DetermineLocationTask()
                .execute(text)
                .get()
            if (location == null) {
                showAlertDialog(R.layout.dialog_location_failed,
                    R.string.determine_location_failed,
                    dataTypeActivityInstance!!)
                this.finish()
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