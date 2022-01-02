package dev.maxsiomin.ntc.activity

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint
import dev.maxsiomin.ntc.APK_LOCATION
import dev.maxsiomin.ntc.BuildConfig
import dev.maxsiomin.ntc.R
import dev.maxsiomin.ntc.extensions.openInBrowser
import dev.maxsiomin.ntc.fragments.tabs.TabsFragment
import dev.maxsiomin.ntc.util.SharedPrefsConfig.DATE_UPDATE_SUGGESTED
import java.time.LocalDate
import java.util.regex.Pattern
import javax.inject.Inject

typealias DialogBuilder = AlertDialog.Builder

/**
 * Container for all screens in the app.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Updater {

    @Inject
    lateinit var analytics: FirebaseAnalytics

    private val topLevelDestinations = setOf(R.id.tabsFragment)

    // NavController of the current screen
    private var navController: NavController? = null

    val viewModel by viewModels<MainViewModel>()

    // fragment listener is sued for tracking current nav controller
    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            if (f is TabsFragment || f is NavHostFragment) return
            onNavControllerActivated(f.findNavController())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Prepare root nav controller
        val navController = getRootNavController()
        onNavControllerActivated(navController)

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)

       viewModel.checkForUpdates { latestVersionName ->
            suggestUpdating(latestVersionName)
        }
    }

    private fun suggestUpdating(latestVersionName: String) {
        // Save when update was suggested last time
        viewModel.sharedPrefs.edit().apply {
            putString(DATE_UPDATE_SUGGESTED, LocalDate.now().toString())
        }.apply()

        UpdateDialog.newInstance(latestVersionName).show(supportFragmentManager)
    }

    /**
     * Opens direct uri to .apk in browser. .apk should be automatically downloaded
     */
    override fun update() {
        openInBrowser(APK_LOCATION)
    }

    override fun onDestroy() {
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
        navController = null
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (isStartDestination(navController?.currentDestination)) {
            super.onBackPressed()
        } else {
            navController?.popBackStack()
        }
    }

    override fun onSupportNavigateUp(): Boolean = (navController?.navigateUp() ?: false) || super.onSupportNavigateUp()

    private fun onNavControllerActivated(navController: NavController) {
        if (this.navController == navController) return
        this.navController?.removeOnDestinationChangedListener(destinationListener)
        navController.addOnDestinationChangedListener(destinationListener)
        this.navController = navController
    }

    private fun getRootNavController(): NavController {
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        return navHost.navController
    }

    private val destinationListener = NavController.OnDestinationChangedListener { _, destination, arguments ->
        supportActionBar?.title = prepareTitle(destination.label, arguments)
        supportActionBar?.setDisplayHomeAsUpEnabled(!isStartDestination(destination))
    }

    private fun isStartDestination(destination: NavDestination?): Boolean {
        if (destination == null) return false
        val graph = destination.parent ?: return false
        val startDestinations = topLevelDestinations + graph.startDestinationId
        return startDestinations.contains(destination.id)
    }

    /** Code for this method has been copied from Google sources :) */
    private fun prepareTitle(label: CharSequence?, arguments: Bundle?): String {

        if (label == null) return ""
        val title = StringBuffer()
        val fillInPattern = Pattern.compile("\\{(.+?)\\}")
        val matcher = fillInPattern.matcher(label)
        while (matcher.find()) {
            val argName = matcher.group(1)
            if (arguments != null && arguments.containsKey(argName)) {
                matcher.appendReplacement(title, "")
                title.append(arguments[argName].toString())
            } else {
                throw IllegalArgumentException(
                    "Could not find $argName in $arguments to fill label $label"
                )
            }
        }
        matcher.appendTail(title)
        return title.toString()
    }

    class UpdateDialog : DialogFragment() {

        private val updater get() = requireActivity() as Updater

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

            val currentVersionName = BuildConfig.VERSION_NAME
            val latestVersionName = requireArguments().getString(LATEST_VERSION_NAME)!!

            val dialog = DialogBuilder(requireContext())
                .setMessage(getString(R.string.update_app, currentVersionName, latestVersionName))
                .setNegativeButton(R.string.no_thanks) { _, _ -> }
                .setPositiveButton(R.string.update) { _, _ ->
                    updater.update()
                }
                .create()

            dialog.setCanceledOnTouchOutside(false)

            return dialog
        }

        fun show(manager: FragmentManager) {
            show(manager, TAG)
        }

        companion object {

            const val TAG = "UpdateDialog"

            /** Key for args */
            private const val LATEST_VERSION_NAME = "latestVersion"

            /**
             * Puts [latestVersionName] to args
             */
            @JvmStatic
            fun newInstance(latestVersionName: String) = UpdateDialog().apply {
                arguments = bundleOf(LATEST_VERSION_NAME to latestVersionName)
            }
        }
    }
}
