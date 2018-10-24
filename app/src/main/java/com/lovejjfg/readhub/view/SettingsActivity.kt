/*
 *
 *   Copyright (c) 2017.  Joe
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package com.lovejjfg.readhub.view

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceActivity
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import com.lovejjfg.readhub.BuildConfig
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.utils.getStatusBarHeight
import com.lovejjfg.readhub.utils.inflate
import com.lovejjfg.readhub.view.widget.SwipeCoordinatorLayout
import com.tencent.bugly.beta.Beta
import kotlinx.android.synthetic.main.activity_setting.view.containerLayout
import kotlinx.android.synthetic.main.activity_setting.view.toolbar
import kotlinx.android.synthetic.main.layout_statusbar.view.statusBarProxy

/**
 * A [PreferenceActivity] that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 *
 *
 * See [
   * Android Design: Settings](http://developer.android.com/design/patterns/settings.html) for design guidelines and the [Settings
   * API Guide](http://developer.android.com/guide/topics/ui/settings.html) for more information on developing a Settings UI.
 */
class SettingsActivity : AppCompatPreferenceActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentManager.beginTransaction()
            .replace(android.R.id.content, GeneralPreferenceFragment())
            .commit()
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    override fun isValidFragment(fragmentName: String): Boolean {
        return GeneralPreferenceFragment::class.java.name == fragmentName
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    class GeneralPreferenceFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.pref_general)
            setHasOptionsMenu(true)
            bindPreferenceSummaryToValue(findPreference(getString(R.string.browser_use)))
            val checkUpdate = findPreference(getString(R.string.manual_update))//手动更新
//            checkUpdate.isEnabled = Beta.getStrategyTask() != null
            checkUpdate.setOnPreferenceClickListener {
                Beta.checkUpgrade()
                return@setOnPreferenceClickListener true
            }
            checkUpdate?.summary = getString(R.string.current_version) + BuildConfig.VERSION_NAME
        }

        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup, savedInstanceState: Bundle?): View {
            val view = super.onCreateView(inflater, container, savedInstanceState)
            val parent = container.inflate(R.layout.activity_setting) as SwipeCoordinatorLayout
            parent.setOnSwipeBackListener { activity?.onBackPressed() }
            parent.toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
            parent.containerLayout.addView(view)
            initStatusBar(parent.statusBarProxy, parent.toolbar)
            return parent
        }

        private fun initStatusBar(statusBarProxy: View, toolbar: Toolbar) {
            val statusBarHeight = statusBarProxy.context.getStatusBarHeight()
            val layoutParams = toolbar.layoutParams as MarginLayoutParams
            layoutParams.topMargin = statusBarHeight
            statusBarProxy.layoutParams.height = statusBarHeight
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            val id = item.itemId
            when (id) {
                android.R.id.home -> {
                    activity.finish()
                    return true
                }

            }
            return super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen
    }

    companion object {

        /**
         * Helper method to determine if the device has an extra-large screen. For
         * example, 10" tablets are extra-large.
         */
        private fun isXLargeTablet(context: Context): Boolean {
            return context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_XLARGE
        }

        /**
         * A preference value change listener that updates the preference's summary
         * to reflect its new value.
         */
        private val sBindPreferenceSummaryToValueListener = Preference.OnPreferenceChangeListener { preference, value ->
            val stringValue = value.toString()
            true
        }

        /**
         * Binds a preference's summary to its value. More specifically, when the
         * preference's value is changed, its summary (line of text below the
         * preference title) is updated to reflect the value. The summary is also
         * immediately updated upon calling this method. The exact display format is
         * dependent on the type of preference.

         * @see .sBindPreferenceSummaryToValueListener
         */
        private fun bindPreferenceSummaryToValue(preference: Preference) {
            // Set the listener to watch for value changes.
            preference.onPreferenceChangeListener = sBindPreferenceSummaryToValueListener

            // Trigger the listener immediately with the preference's
            // current value.
            sBindPreferenceSummaryToValueListener.onPreferenceChange(
                preference,
                PreferenceManager
                    .getDefaultSharedPreferences(preference.context)
                    .getBoolean(preference.key, false)
            )
        }
    }
}
