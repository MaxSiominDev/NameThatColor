package dev.maxsiomin.ntc.activity

import android.os.Bundle
import android.widget.Toast
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maxsiomin.ntc.BuildConfig
import dev.maxsiomin.ntc.R
import dev.maxsiomin.ntc.fragments.BaseViewModel
import dev.maxsiomin.ntc.repository.Failure
import dev.maxsiomin.ntc.repository.Success
import dev.maxsiomin.ntc.repository.UpdateRepository
import dev.maxsiomin.ntc.util.SharedPrefsConfig.DATE_UPDATE_SUGGESTED
import dev.maxsiomin.ntc.util.UiActions
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(uiActions: UiActions): BaseViewModel(uiActions) {

    /**
     * Used by fragments in order to save data
     */
    val dataBundle = Bundle()

    /**
     * Checks for updates. If updates found calls [onUpdateFound]
     */
    fun checkForUpdates(onUpdateFound: (String) -> Unit) {

        if (LocalDate.now().toString() == sharedPrefs.getString(DATE_UPDATE_SUGGESTED, null))
            return

        UpdateRepository(this) { result ->
            if (result is Success) {
                val currentVersionName = BuildConfig.VERSION_NAME
                if (currentVersionName != result.latestVersionName) {
                    onUpdateFound(result.latestVersionName)
                }
            } else {
                toast(R.string.last_version_checking_failed, Toast.LENGTH_LONG)
                Timber.e((result as Failure).errorMessage)
            }
        }.getLastVersion()
    }
}
