package net.htlgrieskirchen.at.jeschl17.nfcdroid.ui.tags

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.*
import android.widget.AdapterView
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_tags.view.*
import net.htlgrieskirchen.at.jeschl17.nfcdroid.R
import net.htlgrieskirchen.at.jeschl17.nfcdroid.db.AppDatabase
import net.htlgrieskirchen.at.jeschl17.nfcdroid.db.NfcTagDao
import net.htlgrieskirchen.at.jeschl17.nfcdroid.ui.tags.custom.CustomProfileActivity
import net.htlgrieskirchen.at.jeschl17.nfcdroid.util.TagAdapter

/**
 * A simple [Fragment] subclass.
 */
class TagsFragment : Fragment() {

    private lateinit var db: NfcTagDao

    lateinit var adapter: TagAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_tags, container, false)

        db = AppDatabase.getInstance(requireContext()).nfcTagDao!!

        // Initialize floating action button
        layout.button_add_custom_tag.setOnClickListener {
            val intent = Intent(requireContext(), CustomProfileActivity::class.java)
            startActivity(intent)
        }

        // Initialize tags list view
        adapter = TagAdapter(requireActivity())
        layout.list_tags.adapter = adapter
        adapter.notifyDataSetChanged()
        registerForContextMenu(layout.list_tags)
        layout.list_tags.setOnItemClickListener { _, _, position, _ ->
            val name = adapter.getItem(position).name
            val intent = Intent(activity, TagDetailsActivity::class.java)
                .putExtra("saveTag", db.nfcTagByName(name) as Parcelable)
            startActivity(intent)
        }

        // Remove text view if there are scanned tags
        if (db.getAll().isNotEmpty())
            layout.text_no_nfc_tags_yet.visibility = View.GONE

        return layout
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        requireActivity().menuInflater.inflate(R.menu.tag_delete, menu)
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val name = adapter.getItem((item.menuInfo as AdapterView.AdapterContextMenuInfo).position).name
        db.deleteByName(name)
        adapter.notifyDataSetChanged()
        return true
    }

}
