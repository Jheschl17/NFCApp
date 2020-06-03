package net.htlgrieskirchen.at.jeschl17.nfcdroid

import android.R.attr.data
import android.R.attr.tag
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.*
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import net.htlgrieskirchen.at.jeschl17.nfcdroid.util.showAlertDialog
import net.htlgrieskirchen.at.jeschl17.nfcdroid.util.writeTag
import java.nio.charset.Charset


class MainActivity : AppCompatActivity() {
    
    private val TAG = this::class.qualifiedName

    private var nfcManager: NfcManager? = null
    private var nfcAdapter: NfcAdapter? = null
    private lateinit var intentFiltersArray: Array<IntentFilter>
    private lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

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
        nfcAdapter!!.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, null)
    }

    @SuppressLint("MissingSuperCall")
    public override fun onNewIntent(intent: Intent) {
        val tagFromIntent: Tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
        Toast.makeText(this, "dispatch", Toast.LENGTH_LONG).show()
        nav_view.menu.findItem(R.id.navigation_dashboard).isChecked = true
        nav_view.menu.performIdentifierAction(R.id.navigation_dashboard, 0)
        
        Log.i(TAG, tagFromIntent.toString())
    }

}
