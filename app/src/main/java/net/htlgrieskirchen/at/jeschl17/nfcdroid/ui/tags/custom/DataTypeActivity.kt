package net.htlgrieskirchen.at.jeschl17.nfcdroid.ui.tags.custom

import android.content.Intent
import android.location.Address
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_data_type.*
import net.htlgrieskirchen.at.jeschl17.nfcdroid.R

class DataTypeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_type)

        dataTypeActivityInstance = this

        // Initialize "Text"
        layout_text.setOnClickListener {
            startActivity(Intent(this, TextActivity::class.java))
        }

        // Initialize "Website Link"
        layout_website_link.setOnClickListener {
            startActivity(Intent(this, WebsiteLinkActivity::class.java))
        }

        // Initialize "Android App"
        layout_android_app.setOnClickListener {
            startActivity(Intent(this, AndroidAppActivity::class.java))
        }

        // Initialize "Location"
        layout_location.setOnClickListener {
            startActivity(Intent(this, LocationActivity::class.java))
        }

        // Initialize "Address"
        layout_address.setOnClickListener {
            startActivity(Intent(this, AddressActivity::class.java))
        }

        // Initialize "Custom Data"
        layout_custom_data.setOnClickListener {
            startActivity(Intent(this, CustomDataActivity::class.java))
        }
    }
}

var dataTypeActivityInstance: DataTypeActivity? = null