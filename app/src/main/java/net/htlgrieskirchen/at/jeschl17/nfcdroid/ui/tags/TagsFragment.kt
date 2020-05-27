package net.htlgrieskirchen.at.jeschl17.nfcdroid.ui.tags

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.htlgrieskirchen.at.jeschl17.nfcdroid.R

/**
 * A simple [Fragment] subclass.
 */
class TagsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tags, container, false)
    }

}
