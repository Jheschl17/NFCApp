package net.htlgrieskirchen.at.jeschl17.nfcdroid.ui.scan

import android.nfc.NdefMessage
import android.nfc.Tag
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_scan.view.*
import net.htlgrieskirchen.at.jeschl17.nfcdroid.R
import net.htlgrieskirchen.at.jeschl17.nfcdroid.util.TagAdapter
import net.htlgrieskirchen.at.jeschl17.nfcdroid.util.attributes
import net.htlgrieskirchen.at.jeschl17.nfcdroid.util.records

class ScanFragment : Fragment() {

    private lateinit var layout: View

    lateinit var rawMessage: NdefMessage
    private val messages = mutableListOf<View>()
    private val attributes = mutableListOf<View>()

    private val views = mutableListOf<View>()
    private lateinit var adapter: TagAdapter

    enum class Mode {
        DETAIL,
        SCAN
    }
    var mode: Mode = Mode.SCAN
    var tag: Tag? = null

    @ExperimentalStdlibApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layout = inflater.inflate(R.layout.fragment_scan, container, false)

        adapter = TagAdapter(views)
        layout.list.adapter = this.adapter
        adapter.notifyDataSetChanged()

        if (mode == Mode.SCAN)
            setModeScan()
        if (mode == Mode.DETAIL)
            setModeDetail(
                attributes(tag!!, requireActivity()),
                records(rawMessage.records, requireActivity()))

        return layout
    }

    private fun setModeDetail(attributes: List<View>, messages: List<View>) {
        layout.text_tap_message.visibility = View.GONE
        layout.constraintl_details.visibility = View.VISIBLE

        this.messages.clear()
        this.messages.addAll(messages)

        this.attributes.clear()
        this.attributes.addAll(attributes)

        this.views.clear()
        this.views.addAll(this.attributes)
        this.views.addAll(this.messages)

        adapter.notifyDataSetChanged()
    }

    private fun setModeScan() {
        layout.text_tap_message.visibility = View.VISIBLE
        layout.constraintl_details.visibility = View.GONE
    }

}
