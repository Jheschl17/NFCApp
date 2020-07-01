package net.htlgrieskirchen.at.jeschl17.nfcdroid.ui.tags.custom

import android.Manifest
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.nfc.NdefRecord
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_location.*
import kotlinx.android.synthetic.main.activity_text.button_cancel
import kotlinx.android.synthetic.main.activity_text.button_save
import net.htlgrieskirchen.at.jeschl17.nfcdroid.R
import net.htlgrieskirchen.at.jeschl17.nfcdroid.util.getLocation
import net.htlgrieskirchen.at.jeschl17.nfcdroid.util.showAlertDialog

class LocationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        // Initialize "Update Location" button
        button_update_location.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
            ) {
                getSystemService(LocationManager::class.java).requestSingleUpdate(
                    LocationManager.GPS_PROVIDER,
                    object : LocationListener {
                        override fun onLocationChanged(location: Location?) {
                        }

                        override fun onStatusChanged(
                            provider: String?,
                            status: Int,
                            extras: Bundle?
                        ) {
                        }

                        override fun onProviderEnabled(provider: String?) {
                        }

                        override fun onProviderDisabled(provider: String?) {
                        }
                    },
                    null
                )

                val location = getLocation(this)
                if (location == null) {
                    showAlertDialog(
                        R.layout.dialog_could_not_get_location,
                        R.string.could_not_get_location,
                        this@LocationActivity
                    )
                    return@setOnClickListener
                }

                text_latitude.setText(location.latitude.toString(), TextView.BufferType.EDITABLE)
                text_longitude.setText(location.longitude.toString(), TextView.BufferType.EDITABLE)
            } else {
                this.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1337)
            }
        }

        // Initialize cancel button
        button_cancel.setOnClickListener {
            this.finish()
        }

        // Initialize save button
        button_save.setOnClickListener {
            val lat = text_latitude.text.toString().toDoubleOrNull()
            val lon = text_longitude.text.toString().toDoubleOrNull()

            if (lat == null) {
                text_latitude.error = resources.getString(R.string.please_enter_value)
                return@setOnClickListener
            }
            if (lon == null) {
                text_longitude.error = resources.getString(R.string.please_enter_value)
                return@setOnClickListener
            }

            val record = NdefRecord.createUri("geo:$lat,$lon")
            customProfileActivityInstance?.records?.add(record)
            customProfileActivityInstance?.adapter?.notifyDataSetChanged()
            dataTypeActivityInstance?.finish()
            this.finish()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1337 &&
            permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED)
            button_update_location.callOnClick()

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}