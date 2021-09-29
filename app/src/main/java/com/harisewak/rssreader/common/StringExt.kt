package com.harisewak.rssreader.common

import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.Spanned

@Suppress("deprecation")
fun String?.withHtmlFormatting(): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(
            this,
            Html.FROM_HTML_MODE_COMPACT
        )
    } else {
        Html.fromHtml(
            this
        )
    }

}