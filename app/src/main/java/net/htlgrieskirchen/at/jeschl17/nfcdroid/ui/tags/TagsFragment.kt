package net.htlgrieskirchen.at.jeschl17.nfcdroid.ui.tags

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_tags.view.*
import net.htlgrieskirchen.at.jeschl17.nfcdroid.R
import net.htlgrieskirchen.at.jeschl17.nfcdroid.db.NfcTag
import net.htlgrieskirchen.at.jeschl17.nfcdroid.util.TagAdapter

/**
 * A simple [Fragment] subclass.
 */
class TagsFragment : Fragment() {

    lateinit var adapter: TagAdapter
    val tags = mutableListOf<NfcTag>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_tags, container, false)

        // Initialize floating action button
        layout.button_add_custom_tag.setOnClickListener {
            val intent = Intent(requireContext(), CustomProfileActivity::class.java)
            startActivity(intent)
        }

        // Initialize tags list view
        adapter = TagAdapter(tags, requireActivity())
        layout.list_tags.adapter = adapter
        adapter.notifyDataSetChanged()

        // Remove text view if there are scanned tags
        if (tags.isNotEmpty())
            layout.text_no_nfc_tags_yet.visibility = View.GONE

        return layout
    }

}
