package net.htlgrieskirchen.at.jeschl17.nfcdroid.ui.tags

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_tag_details.*
import net.htlgrieskirchen.at.jeschl17.nfcdroid.R
import net.htlgrieskirchen.at.jeschl17.nfcdroid.db.NfcTag
import net.htlgrieskirchen.at.jeschl17.nfcdroid.util.GenericAdapter
import net.htlgrieskirchen.at.jeschl17.nfcdroid.util.attributes
import net.htlgrieskirchen.at.jeschl17.nfcdroid.util.records
import net.htlgrieskirchen.at.jeschl17.nfcdroid.util.writeTag


class TagDetailsActivity : AppCompatActivity() {

    lateinit var adapter: GenericAdapter
    private val items = mutableListOf<View>()

    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag_details)

        // Initialize "Export" Button
        // TODO

        // Initialize "Write To Tag" Button
        button_write_to_tag.setOnClickListener {
            //writeTag()
        }

        // Initialize "Emulate" Button
        // TODO

        // Initialize tag data list view
        val saveTag = intent.getParcelableExtra<NfcTag>("saveTag")

        text_tag_name.text = saveTag.name
        items.addAll(records(saveTag.ndefMessage?.records, this))

        adapter = GenericAdapter(items)
        list.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }
}