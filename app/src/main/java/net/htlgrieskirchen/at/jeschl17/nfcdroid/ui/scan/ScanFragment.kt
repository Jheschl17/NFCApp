package net.htlgrieskirchen.at.jeschl17.nfcdroid.ui.scan

import android.content.Context
import android.nfc.NdefMessage
import android.nfc.Tag
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_scan.view.*
import net.htlgrieskirchen.at.jeschl17.nfcdroid.R
import net.htlgrieskirchen.at.jeschl17.nfcdroid.SaveTag
import net.htlgrieskirchen.at.jeschl17.nfcdroid.instance
import net.htlgrieskirchen.at.jeschl17.nfcdroid.util.GenericAdapter
import net.htlgrieskirchen.at.jeschl17.nfcdroid.util.attributes
import net.htlgrieskirchen.at.jeschl17.nfcdroid.util.records
import net.htlgrieskirchen.at.jeschl17.nfcdroid.util.toSaveTag

class ScanFragment : Fragment() {

    private lateinit var layout: View

    lateinit var rawMessage: NdefMessage
    private val messages = mutableListOf<View>()
    private val attributes = mutableListOf<View>()

    private val views = mutableListOf<View>()
    private lateinit var adapter: GenericAdapter

    enum class Mode {
        DETAIL,
        SCAN
    }
    var mode: Mode = Mode.SCAN
    lateinit var tag: Tag

    @ExperimentalStdlibApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layout = inflater.inflate(R.layout.fragment_scan, container, false)

        // Initialize save button
        layout.button_save_tag.setOnClickListener {
            if (layout.text_tag_name.text.isEmpty()) {
                Toast.makeText(
                    requireActivity(),
                    requireActivity().getString(R.string.name_required),
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            val tag = toSaveTag(
                layout.text_tag_name.text.toString(),
                tag,
                rawMessage,
                requireActivity())
            // Hide keyboard
            requireActivity().currentFocus?.let { v ->
                val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(v.windowToken, 0)
            }
            layout.text_tag_name.setText("", TextView.BufferType.EDITABLE)
            // Change mode
            setModeScan()
            mode = Mode.SCAN
            instance.tagsFragment.tags.add(tag)
            instance.tab_tags.callOnClick()
        }

        // Initialize list view
        adapter = GenericAdapter(views)
        layout.list.adapter = this.adapter
        adapter.notifyDataSetChanged()

        // Set display mode
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

    fun reset() {
        layout.text_tag_name.setText("", TextView.BufferType.EDITABLE)
        layout.list.clearFocus()
        layout.list.post {
            layout.list.setSelection(0)
        }
    }

}
