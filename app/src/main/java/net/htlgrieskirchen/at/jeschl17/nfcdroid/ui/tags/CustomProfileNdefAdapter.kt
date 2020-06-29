package net.htlgrieskirchen.at.jeschl17.nfcdroid.ui.tags

import android.app.Activity
import android.nfc.NdefRecord
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import net.htlgrieskirchen.at.jeschl17.nfcdroid.R
import net.htlgrieskirchen.at.jeschl17.nfcdroid.util.inflate
import net.htlgrieskirchen.at.jeschl17.nfcdroid.util.view

class CustomProfileNdefAdapter (
    private val records: List<NdefRecord>,
    private val activity: Activity,
    private val onPlusClick: View.OnClickListener
) : BaseAdapter() {

    override fun getItem(position: Int): NdefRecord? {
        return if (position == records.size + 1|| position == 0) // Return "plus button"
            null
        else // Return record
            records[position - 1]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return records.size + 2
    }

    @ExperimentalStdlibApi
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val item = getItem(position)
        return when (position) {
            // Item is the "Data" headline
            0 -> inflate(R.layout.attribute_headline_data, activity)
            // Item is the "plus button"
            records.size + 1 -> inflate(R.layout.message_plus_button, activity).apply {
                this.setOnClickListener(onPlusClick)
            }
            // Item is a regular ndef record
            else -> view(item!!, activity)
        }
    }
}