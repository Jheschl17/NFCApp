package net.htlgrieskirchen.at.jeschl17.nfcdroid.ui.tags

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_tags.view.*
import net.htlgrieskirchen.at.jeschl17.nfcdroid.R

/**
 * A simple [Fragment] subclass.
 */
class TagsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_tags, container, false)

        layout.button_add_custom_tag.setOnClickListener {
            val intent = Intent(requireContext(), CustomProfileActivity::class.java)
            startActivity(intent)
        }

        return layout
    }

}
