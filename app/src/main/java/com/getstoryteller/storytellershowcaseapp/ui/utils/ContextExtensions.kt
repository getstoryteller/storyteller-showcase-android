package com.getstoryteller.storytellershowcaseapp.ui.utils

import android.content.Context
import androidx.core.content.pm.PackageInfoCompat

val Context.formatterApplicationVersion: String
  get() {
    val packageInfo = packageManager.getPackageInfo(packageName, 0)
    val versionCode = PackageInfoCompat.getLongVersionCode(packageInfo)
    return "v${packageInfo.versionName} ($versionCode)"
  }

fun Context.copyToClipboard(text: String) {
  val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
  val clip = android.content.ClipData.newPlainText("Copied Text", text)
  clipboard.setPrimaryClip(clip)
}

fun Context.toast(message: String) {
  android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_LONG).show()
}
