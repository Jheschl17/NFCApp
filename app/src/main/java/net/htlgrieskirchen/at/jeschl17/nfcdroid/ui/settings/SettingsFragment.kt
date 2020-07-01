package net.htlgrieskirchen.at.jeschl17.nfcdroid.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

import net.htlgrieskirchen.at.jeschl17.nfcdroid.R

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        findPreference<Preference>(getString(R.string.licenses))?.setOnPreferenceClickListener {
            return@setOnPreferenceClickListener true
        }
        findPreference<Preference>(getString(R.string.source_code))?.setOnPreferenceClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://gitlab.com/_cicero/NFCApp"))
            startActivity(intent)
            return@setOnPreferenceClickListener true
        }
        findPreference<Preference>(getString(R.string.icons8))?.setOnPreferenceClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://icons8.com/"))
            startActivity(intent)
            return@setOnPreferenceClickListener true
        }
    }

}
