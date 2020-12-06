package br.com.devsrsouza.chucknorrisfact.util

import android.view.View
import android.widget.TextView

import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher


fun withFontSize(expectedSize: Float): Matcher<View?>? {
    return object : BoundedMatcher<View?, View>(View::class.java) {
        override fun matchesSafely(target: View): Boolean {
            if (target !is TextView) {
                return false
            }
            val targetEditText = target as TextView
            return targetEditText.textSize == expectedSize
        }

        override fun describeTo(description: Description) {
            description.appendText("with fontSize: ")
            description.appendValue(expectedSize)
        }
    }
}