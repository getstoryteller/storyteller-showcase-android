package com.getstoryteller.storytellersampleapp.features.main.bottomnav

sealed class NavigationInterceptor {
    data object None : NavigationInterceptor()
    data class TargetRoute(
        val targetRoute: String,
        val shouldIntercept: () -> Boolean = { false },
        val onIntercepted: suspend () -> Unit = {},
    ) : NavigationInterceptor()
}
