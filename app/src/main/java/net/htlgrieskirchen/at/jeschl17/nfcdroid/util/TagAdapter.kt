package net.htlgrieskirchen.at.jeschl17.nfcdroid.util

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.tag.view.*
import net.htlgrieskirchen.at.jeschl17.nfcdroid.R
import net.htlgrieskirchen.at.jeschl17.nfcdroid.db.NfcTag
import net.htlgrieskirchen.at.jeschl17.nfcdroid.ui.tags.TagDetails

class TagAdapter(
    private val items: List<NfcTag>,
    private val activity: Activity
) : BaseAdapter() {

    override fun getItem(position: Int): NfcTag {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return activity.layoutInflater.inflate(R.layout.tag, null).apply {
            val item = getItem(position)

            text_name.text = item.name
            text_id.text = item.tagId
            text_technologies.text = item.technologies

            setOnClickListener {
                val intent = Intent(activity, TagDetails::class.java).putExtra("saveTag", item)
                activity.startActivity(intent)
            }
        }
    }

}


