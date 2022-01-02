package dev.maxsiomin.ntc.fragments.tabs.settings

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import dagger.hilt.android.AndroidEntryPoint
import dev.maxsiomin.ntc.APK_LOCATION
import dev.maxsiomin.ntc.App
import dev.maxsiomin.ntc.R
import dev.maxsiomin.ntc.extensions.addOnPreferenceChangeListener
import dev.maxsiomin.ntc.extensions.openInBrowser
import dev.maxsiomin.ntc.extensions.setOnClickListener
import dev.maxsiomin.ntc.fragments.BaseViewModel

private const val DEVELOPER_EMAIL = "max@maxsiomin.dev"
private const val DEVELOPER_WEBSITE = "https://maxsiomin.dev/"

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    private val mViewModel by viewModels<BaseViewModel>()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        findPreference(R.string.key_theme).addOnPreferenceChangeListener {
            val app = requireActivity().application as App
            app.setupTheme(it as String)
        }

        findPreference(R.string.key_help_and_feedback).setOnClickListener {
            sendEmail()
        }

        findPreference(R.string.key_share_this_app).setOnClickListener {
            shareThisApp()
        }

        findPreference(R.string.key_more_apps).setOnClickListener {
            moreApps()
        }

        findPreference(R.string.key_app_version).summary = App.VERSION
    }

    private fun findPreference(key: Int) = findPreference<Preference>(mViewModel.getString(key))!!

    /**
     * Open mail client and write letter to me
     */
    private fun sendEmail() {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("mailto:$DEVELOPER_EMAIL")))
        } catch (e: ActivityNotFoundException) {
            mViewModel.toast(R.string.mail_client_not_found, Toast.LENGTH_SHORT)
        }
    }

    /**
     * Share link to .apk
     */
    private fun shareThisApp() {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, APK_LOCATION)
            type = "text/plain"
        }

        try {
            startActivity(Intent.createChooser(intent, null))
        } catch (e: ActivityNotFoundException) {
            mViewModel.toast(R.string.smth_went_wrong, Toast.LENGTH_LONG)
        }
    }

    private fun moreApps() = requireActivity().openInBrowser(DEVELOPER_WEBSITE)
}
