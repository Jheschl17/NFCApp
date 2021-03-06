package net.htlgrieskirchen.at.jeschl17.nfcdroid.ui.tags

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.NfcManager
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_tag_details.*
import net.htlgrieskirchen.at.jeschl17.nfcdroid.R
import net.htlgrieskirchen.at.jeschl17.nfcdroid.db.NfcTag
import net.htlgrieskirchen.at.jeschl17.nfcdroid.util.*


class TagDetailsActivity : AppCompatActivity() {

    private lateinit var intentFiltersArray: Array<IntentFilter>
    private lateinit var pendingIntent: PendingIntent
    private var nfcManager: NfcManager? = null
    private var nfcAdapter: NfcAdapter? = null

    private var tapAgainstPhoneDialog: AlertDialog? = null

    lateinit var adapter: GenericAdapter
    private val items = mutableListOf<View>()
    private var dataToWrite: NdefMessage? = null

    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag_details)

        // Initialize tag data list view
        val saveTag = intent.getParcelableExtra<NfcTag>("saveTag")!!

        text_tag_name.text = saveTag.name
        items.addAll(records(saveTag.ndefMessage.records, this))

        adapter = GenericAdapter(items)
        list.adapter = adapter
        adapter.notifyDataSetChanged()

        // Initialize "Export" Button
        // TODO

        // Initialize "Write To Tag" Button
        button_write_to_tag.setOnClickListener {
            dataToWrite = saveTag.ndefMessage
            tapAgainstPhoneDialog = AlertDialog.Builder(this)
                .setTitle(R.string.write_nfc)
                .setView(R.layout.dialog_tap_tag_against_phone)
                .setNegativeButton(R.string.cancel) { _, _ ->
                    dataToWrite = null
                }
                .create()
            tapAgainstPhoneDialog?.show()
       }

        // Claim priority over NFC intents
        nfcManager = getSystemService(Context.NFC_SERVICE) as NfcManager?
        nfcAdapter = nfcManager?.defaultAdapter

        val nfcIntent = Intent(this, javaClass).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        pendingIntent = PendingIntent.getActivity(this, 0, nfcIntent, 0)
        val nfcFilter = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)

        intentFiltersArray = arrayOf(nfcFilter)
    }

    public override fun onNewIntent(intent: Intent) {
        val tagFromIntent = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)

        if (tagFromIntent != null) {
            if (dataToWrite != null) {
                tapAgainstPhoneDialog?.dismiss()
                val success = writeTag(this, tagFromIntent, dataToWrite!!)
                val showNotification = PreferenceManager.getDefaultSharedPreferences(this)
                        .getBoolean("switch_preference_display_notifications", true)

                if (success && showNotification) {
                    var notification = NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.icons8_quill_pen_96)
                        .setContentTitle(resources.getString(R.string.data_written))
                        .setContentText(resources.getString(R.string.data_written_detail))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .build()

                    with(NotificationManagerCompat.from(this)) {
                        notify(1234, notification)
                    }
                }
                dataToWrite = null
            }
        } else {
            super.onNewIntent(intent)
        }
    }

    public override fun onPause() {
        super.onPause()
        nfcAdapter!!.disableForegroundDispatch(this)
    }

    public override fun onResume() {
        super.onResume()
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, null)
    }
}