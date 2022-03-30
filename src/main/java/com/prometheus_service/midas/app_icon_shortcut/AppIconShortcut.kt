package com.prometheus_service.midas.app_icon_shortcut

import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.webkit.WebView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.prometheus_service.midas.app_icon_shortcut.model.ShortcutsModel

class AppIconShortcut {

    companion object {
        const val KEY_IS_SOURCE_APP_SHORTCUT = "isSourceAppShortcut"
    }

    /**
     * Use pendingRoute as route when calling "loadAppShortcutRoute" on memberLoggedIn
     */
    var pendingRoute: String? = null

    /**
     * Delay is for call this method on logged in callback which needs some time
     * for webView to be reset and be able to load correctly
     */
    fun loadAppShortcutRoute(
        activity: AppCompatActivity,
        route: String?,
        webView: WebView,
        baseUrl: String
    ) {
        if (route.isNullOrEmpty()) return
        if (route == activity.getString(R.string.route_promotions)) {
            webView.post { webView.loadUrl("javascript:$route") }
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                val isLoggedIn = LoggedInStateUtils.isLoggedIn(baseUrl)
                if (isLoggedIn) {
                    webView.post { webView.loadUrl("javascript:$route") }
                    pendingRoute = null
                } else {
                    pendingRoute = route
                    val loginRoute = activity.getString(R.string.route_login)
                    webView.post { webView.loadUrl("javascript:$loginRoute") }
                }
            }, 1000)

        }
        activity.intent.removeExtra(KEY_IS_SOURCE_APP_SHORTCUT)
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    fun createAppIconShortcuts(activity: AppCompatActivity, model: ShortcutsModel) {
        val shortcutManager = activity.getSystemService(ShortcutManager::class.java)
        val intent = Intent(activity.applicationContext, activity::class.java)

        val depositShortcut = ShortcutInfo.Builder(activity, "depositShortcut")
            .setIntent(
                createShortcutIntent(
                    intent,
                    activity.getString(R.string.route_funds_deposit)
                )
            )
            .setShortLabel(model.depositLabel)
            .setIcon(Icon.createWithResource(activity, model.cashResourceIcon))
            .build()

        val transferShortcut = ShortcutInfo.Builder(activity, "withdrawalShortcut")
            .setIntent(
                createShortcutIntent(
                    intent,
                    activity.getString(R.string.route_funds_withdrawal)
                )
            )
            .setShortLabel(model.withdrawalLabel)
            .setIcon(Icon.createWithResource(activity, model.walletResourceIcon))
            .build()

        val promotionShortcut = ShortcutInfo.Builder(activity, "promotionShortcut")
            .setIntent(createShortcutIntent(intent, activity.getString(R.string.route_promotions)))
            .setShortLabel(model.promotionsLabel)
            .setIcon(Icon.createWithResource(activity, model.bullHornResourceIcon))
            .build()

        shortcutManager.dynamicShortcuts =
            listOf(depositShortcut, transferShortcut, promotionShortcut)
    }

    private fun createShortcutIntent(intent: Intent, route: String): Intent {
        intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP
                or Intent.FLAG_ACTIVITY_SINGLE_TOP
                or Intent.FLAG_ACTIVITY_NEW_TASK
                or Intent.FLAG_ACTIVITY_NO_ANIMATION)
        intent.data = Uri.parse(route)
        intent.action = Intent.ACTION_VIEW
        intent.putExtra(KEY_IS_SOURCE_APP_SHORTCUT, true)
        return intent
    }
}