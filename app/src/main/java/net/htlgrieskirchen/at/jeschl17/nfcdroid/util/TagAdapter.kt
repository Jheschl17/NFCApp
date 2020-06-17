package net.htlgrieskirchen.at.jeschl17.nfcdroid.util

import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.ArrayAdapter
import android.widget.BaseAdapter

class TagAdapter(
    private val items: List<View>
) : BaseAdapter() {

    override fun getItem(position: Int): View {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return items[position]
    }

}


