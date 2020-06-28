package net.htlgrieskirchen.at.jeschl17.nfcdroid.util

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.tag.view.*
import net.htlgrieskirchen.at.jeschl17.nfcdroid.R
import net.htlgrieskirchen.at.jeschl17.nfcdroid.db.AppDatabase
import net.htlgrieskirchen.at.jeschl17.nfcdroid.db.NfcTag
import net.htlgrieskirchen.at.jeschl17.nfcdroid.ui.tags.TagDetailsActivity

class TagAdapter(
    private val activity: Activity
) : BaseAdapter() {

    private val db = AppDatabase.getInstance(activity).nfcTagDao!!

    override fun getItem(position: Int): NfcTag {
        return db.getAll()[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return db.getAll().size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return activity.layoutInflater.inflate(R.layout.tag, null).apply {
            val item = getItem(position)

            text_name.text = item.name
            text_num_records.text = item.ndefMessage.records.size.toString()
            text_size.text = "${calculateSize(item.ndefMessage.toByteArray())} byte"

            setOnClickListener {
                val intent = Intent(activity, TagDetailsActivity::class.java)
                    .putExtra("saveTag", item as Parcelable)
                activity.startActivity(intent)
            }
        }
    }

}


