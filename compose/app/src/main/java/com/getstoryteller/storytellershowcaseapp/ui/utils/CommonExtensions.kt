package com.getstoryteller.storytellershowcaseapp.ui.utils

import android.util.Log
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties

fun Any.printNonNullProperties(
  indent: String = "",
): String {
  val propertiesString = this::class.memberProperties
    .filter {
      it.visibility == KVisibility.PUBLIC && it.visibility != KVisibility.INTERNAL
    } // Only include public properties
    .mapNotNull { property ->
      val value = property.getter.call(this)
      if (value != null) {
        if (value::class.isData) {
          "$indent${property.name}:\n${value.printNonNullProperties("$indent  ")}"
        } else {
          "$indent${property.name} = $value"
        }
      } else {
        null
      }
    }.joinToString(separator = "\n\t")

  return "${this::class.simpleName} {\n\t$propertiesString\n}"
}

/**
 * Log a large string to the console
 * https://stackoverflow.com/questions/8888654/android-set-max-length-of-logcat-messages
 */
fun largeLog(
  tag: String?,
  content: String,
) {
  val length = 4000
  if (content.length > length) {
    Log.i(tag, content.substring(0, length))
    largeLog(tag, content.substring(length))
  } else {
    Log.i(tag, content)
  }
}
