package com.getstoryteller.storytellershowcaseapp.analytics
import android.util.Log
import timber.log.Timber
import javax.inject.Inject

/**
 * A tree which logs important information for crash reporting.
 */
class CrashReportingTree @Inject constructor() : Timber.Tree() {
  override fun log(
    priority: Int,
    tag: String?,
    message: String,
    t: Throwable?,
  ) {
    if (priority == Log.VERBOSE || priority == Log.DEBUG) {
      return
    }
    // TODO Use crash reporting tree
    return
  }
}
