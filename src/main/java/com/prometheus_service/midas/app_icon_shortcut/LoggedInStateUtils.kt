package com.prometheus_service.midas.app_icon_shortcut

import android.webkit.CookieManager

object LoggedInStateUtils {

    fun isLoggedIn(baseUrl: String): Boolean {
        val cookies = CookieManager.getInstance().getCookie(baseUrl)
        return cookies.contains("pt_token")
    }
}