package br.com.devsrsouza.chucknorrisfacts.util.share

import android.app.Activity
import androidx.core.app.ActivityCompat
import androidx.core.app.ShareCompat

class ContentShareImpl(
        private val activity: Activity
) : ContentShare {

    companion object {
        private const val TEXT_MIME_TYPE = "text/plain"
    }

    override fun shareText(text: String) {
        val intent = ShareCompat.IntentBuilder
                .from(activity)
                .setType(TEXT_MIME_TYPE)
                .setText(text)
                .intent

        ActivityCompat.startActivity(activity, intent, null)
    }
}