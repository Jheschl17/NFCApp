package net.htlgrieskirchen.at.jeschl17.nfcdroid

import android.app.Dialog
import android.content.Context
import android.nfc.NfcAdapter
import android.nfc.NfcManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import net.htlgrieskirchen.at.jeschl17.nfcdroid.util.showAlertDialog


class MainActivity : AppCompatActivity() {

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

        val nfcManager: NfcManager? = getSystemService(Context.NFC_SERVICE) as NfcManager?
        val nfcAdapter: NfcAdapter? = nfcManager?.defaultAdapter

        // Check status of NFC support on device and react accordingly
        if (nfcAdapter != null && nfcAdapter.isEnabled) { // NFC available
        } else if (nfcAdapter != null && !nfcAdapter.isEnabled) { // NFC not enabled
            showAlertDialog(R.layout.dialog_nfcstatus_disabled, R.string.nfc_disabled_title, this)
        } else { // NFC not supported
            showAlertDialog(R.layout.dialog_nfcstatus_notavailable,R.string.nfc_not_available_title, this)
        }
    }
}
