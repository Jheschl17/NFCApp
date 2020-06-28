package net.htlgrieskirchen.at.jeschl17.nfcdroid

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Typeface
import android.nfc.*
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import net.htlgrieskirchen.at.jeschl17.nfcdroid.ui.scan.ScanFragment
import net.htlgrieskirchen.at.jeschl17.nfcdroid.ui.settings.SettingsFragment
import net.htlgrieskirchen.at.jeschl17.nfcdroid.ui.tags.TagsFragment
import net.htlgrieskirchen.at.jeschl17.nfcdroid.util.dp
import net.htlgrieskirchen.at.jeschl17.nfcdroid.util.showAlertDialog


class MainActivity : AppCompatActivity() {
    
    private val TAG = this::class.qualifiedName

    private var nfcManager: NfcManager? = null
    private var nfcAdapter: NfcAdapter? = null
    private lateinit var intentFiltersArray: Array<IntentFilter>
    private lateinit var pendingIntent: PendingIntent

    lateinit var tagsFragment: TagsFragment
    lateinit var scanFragment: ScanFragment
    lateinit var settingsFragment: SettingsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize instance for fragment access
        instance = this

        // Initialize tabs
        tagsFragment = TagsFragment()
        scanFragment = ScanFragment()
        settingsFragment = SettingsFragment()

        supportFragmentManager.beginTransaction()
            .replace(frag_display.id, tagsFragment)
            .commit()

        fun makeBig(view: ImageView) {
            view.layoutParams.width = dp(35f, this)
            view.layoutParams.height = dp(35f, this)
        }
        fun makeBig(view: TextView) {
            view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f)
            view.setTypeface(null, Typeface.BOLD)
        }
        fun makeSmall(view: ImageView) {
            view.layoutParams.width = dp(30f, this)
            view.layoutParams.height = dp(30f, this)
        }
        fun makeSmall(view: TextView) {
            view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
            view.setTypeface(null, Typeface.NORMAL)
        }
        tab_tags.setOnClickListener {
            image_tab_tags.height
            supportFragmentManager.beginTransaction()
                .replace(frag_display.id, tagsFragment)
                .commit()
            makeBig(image_tab_tags)
            makeBig(text_tab_tags)
            makeSmall(image_tab_scan)
            makeSmall(text_tab_scan)
            makeSmall(image_tab_settings)
            makeSmall(text_tab_settings)
        }
        tab_scan.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(frag_display.id, scanFragment)
                .commitNow()

            // Refresh ScanFragment for when a tag is scanned while ScanFragment tab is opened
            supportFragmentManager
                .beginTransaction()
                .detach(scanFragment)
                .commitNowAllowingStateLoss()
            supportFragmentManager
                .beginTransaction()
                .attach(scanFragment)
                .commitAllowingStateLoss()

            makeSmall(image_tab_tags)
            makeSmall(text_tab_tags)
            makeBig(image_tab_scan)
            makeBig(text_tab_scan)
            makeSmall(image_tab_settings)
            makeSmall(text_tab_settings)
        }
        tab_settings.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(frag_display.id, settingsFragment)
                .commit()
            makeSmall(image_tab_tags)
            makeSmall(text_tab_tags)
            makeSmall(image_tab_scan)
            makeSmall(text_tab_scan)
            makeBig(image_tab_settings)
            makeBig(text_tab_settings)
        }
        tab_tags.callOnClick()

        // Check status of NFC support on device and react accordingly
        nfcManager = getSystemService(Context.NFC_SERVICE) as NfcManager?
        nfcAdapter = nfcManager?.defaultAdapter
        if (nfcAdapter != null && nfcAdapter!!.isEnabled) { // NFC available
        } else if (nfcAdapter != null && !nfcAdapter!!.isEnabled) { // NFC not enabled
            showAlertDialog(R.layout.dialog_nfcstatus_disabled, R.string.nfc_disabled_title, this)
        } else { // NFC not supported
            showAlertDialog(R.layout.dialog_nfcstatus_notavailable,R.string.nfc_not_available_title, this)
        }

        // Claim priority over NFC intents
        val nfcIntent = Intent(this, javaClass).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        pendingIntent = PendingIntent.getActivity(this, 0, nfcIntent, 0)
        val nfcFilter = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)

        intentFiltersArray = arrayOf(nfcFilter)
    }

    public override fun onPause() {
        super.onPause()
        nfcAdapter!!.disableForegroundDispatch(this)
    }

    public override fun onResume() {
        super.onResume()
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, null)
    }

    @SuppressLint("MissingSuperCall")
    public override fun onNewIntent(intent: Intent) {
        val tagFromIntent: Tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)

        // Switch to scan tab and modify scan fragment
        scanFragment.rawMessage = null
        intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMessages ->
            val messages: List<NdefMessage> = rawMessages.map { it as NdefMessage }
            scanFragment.rawMessage = messages.first()
        }
        scanFragment.tag = tagFromIntent
        scanFragment.mode = ScanFragment.Mode.DETAIL
        switchToTabScan()

        Log.i(TAG, "caught NFC intent: $tagFromIntent")
    }

    override fun onBackPressed() {
        val currentFrag = supportFragmentManager.findFragmentById(frag_display.id)
        if (currentFrag is ScanFragment && currentFrag.mode == ScanFragment.Mode.DETAIL) {
            scanFragment.mode = ScanFragment.Mode.SCAN
            switchToTabScan()
        } else {
            super.onBackPressed()
        }
    }

    fun switchToTabTags() {
        tab_tags.callOnClick()
    }

    fun switchToTabScan() {
       tab_scan.callOnClick()
    }

    fun switchToTabSettings() {
        tab_settings.callOnClick()
    }

}

lateinit var instance: MainActivity
