package net.htlgrieskirchen.at.jeschl17.nfcdroid.ui.tags.custom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_data_type.*
import net.htlgrieskirchen.at.jeschl17.nfcdroid.R

class DataTypeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_type)

        dataTypeActivityInstance = this

        layout_text.setOnClickListener {
            startActivity(Intent(this, TextActivity::class.java))
        }
    }
}

var dataTypeActivityInstance: DataTypeActivity? = null