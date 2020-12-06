package br.com.devsrsouza.chucknorrisfact.ui

import android.view.View
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.devsrsouza.chucknorrisfacts.R
import br.com.devsrsouza.chucknorrisfacts.model.UIState
import br.com.devsrsouza.chucknorrisfacts.repository.model.Fact
import br.com.devsrsouza.chucknorrisfacts.ui.*
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BindingAdaptersTest {

    @Test
    fun visibleIf_should_set_visibility_to_VISIBLE_when_true() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val view = View(context)
        view.visibility = View.GONE

        view.visibleIf(true)

        assertThat(view.visibility, `is`(View.VISIBLE))
    }

    @Test
    fun visibleIf_should_set_visibility_to_GONE_when_false() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val view = View(context)
        view.visibility = View.VISIBLE

        view.visibleIf(false)

        assertThat(view.visibility, `is`(View.GONE))
    }

    @Test
    fun factMainCategoryOrUncategorized_should_use_the_fact_first_category_when_categories_is_not_empty() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val view = TextView(context)
        view.setText("")

        val category = "Games"
        val fact = Fact(
                "00000",
                listOf(category),
                "some value"
        )

        view.factMainCategoryOrUncategorized(fact)

        assertThat(view.text, `is`(category))
    }

    @Test
    fun factMainCategoryOrUncategorized_should_use_the_uncategorized_text_when_fact_category_is_empty() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val view = TextView(context)
        view.setText("")

        val uncategorizedText = context.resources.getString(R.string.uncategorized_fact)

        val fact = Fact(
                "00000",
                emptyList(),
                "some value"
        )

        view.factMainCategoryOrUncategorized(fact)

        assertThat(view.text, `is`(uncategorizedText))
    }

    @Test
    fun textStateError_should_set_text_to_network_not_available_when_UIState_NetworkNotAvailable() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val view = TextView(context)
        view.setText("")

        val networkNotAvailableText = context.resources.getString(R.string.network_not_available)

        val uiState = UIState.NetworkNotAvailable<Any>()

        view.textStateError(uiState)

        assertThat(view.text, `is`(networkNotAvailableText))
    }

    @Test
    fun textStateError_should_set_text_to_search_error_when_UIState_Error() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val view = TextView(context)
        view.setText("")

        val searchErrorText = context.resources.getString(R.string.search_error)

        val uiState = UIState.Error<Any>(Throwable())

        view.textStateError(uiState)

        assertThat(view.text, `is`(searchErrorText))
    }

    @Test
    fun textStateError_should_set_text_to_the_received_client_error_message_when_UIState_NetworkClientError() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val view = TextView(context)
        view.setText("")

        val errorMessage = "Your phone is a potato!"
        val uiState = UIState.NetworkClientError<Any>(errorMessage)

        view.textStateError(uiState)

        assertThat(view.text, `is`(errorMessage))
    }

    @Test
    fun visibleIfContainsResult_should_set_visibility_to_VISIBLE_when_UIState_for_fact_list_is_success_and_is_not_empty() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val view = View(context)
        view.visibility = View.GONE

        val fact = Fact(
                "00000",
                emptyList(),
                "some value"
        )
        val uiState = UIState.Success<List<Fact>>(listOf(fact))

        view.visibleIfContainsResult(uiState)

        assertThat(view.visibility, `is`(View.VISIBLE))
    }

    @Test
    fun visibleIfContainsResult_should_set_visibility_to_GONE_when_UIState_is_null() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val view = View(context)
        view.visibility = View.VISIBLE

        val uiState: UIState<List<Fact>>? = null

        view.visibleIfContainsResult(uiState)

        assertThat(view.visibility, `is`(View.GONE))
    }

    @Test
    fun visibleIfContainsResult_should_set_visibility_to_GONE_when_UIState_is_not_success() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val view = View(context)
        view.visibility = View.VISIBLE

        val uiState = UIState.NetworkClientError<List<Fact>>("")

        view.visibleIfContainsResult(uiState)

        assertThat(view.visibility, `is`(View.GONE))
    }

    @Test
    fun visibleIfDoesNotContainsResult_should_set_visibility_to_VISIBLE_when_UIState_for_fact_list_is_success_and_is_empty() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val view = View(context)
        view.visibility = View.GONE

        val uiState = UIState.Success<List<Fact>>(emptyList())

        view.visibleIfDoesNotContainsResult(uiState)

        assertThat(view.visibility, `is`(View.VISIBLE))
    }

    @Test
    fun visibleIfDoesNotContainsResult_should_set_visibility_to_GONE_when_UIState_is_null() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val view = View(context)
        view.visibility = View.VISIBLE

        val uiState: UIState<List<Fact>>? = null

        view.visibleIfDoesNotContainsResult(uiState)

        assertThat(view.visibility, `is`(View.GONE))
    }

    @Test
    fun visibleIfDoesNotContainsResult_should_set_visibility_to_GONE_when_UIState_is_not_success() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val view = View(context)
        view.visibility = View.VISIBLE

        val uiState = UIState.NetworkClientError<List<Fact>>("")

        view.visibleIfDoesNotContainsResult(uiState)

        assertThat(view.visibility, `is`(View.GONE))
    }

    @Test
    fun factTextWithAdaptedSize_should_set_the_font_size_to_fact_small_font_size_when_the_char_count_is_biggerOrEqual_a_fact_char_count_for_small_font() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val view = TextView(context)
        view.setText("")

        val charCount = context.resources.getInteger(R.integer.fact_char_count_for_small_font)

        val text = "G".repeat(charCount)

        val smallFontSize = context.resources.getDimension(R.dimen.fact_small_font_size)

        view.factTextWithAdaptedSize(text)

        assertThat(view.textSize, `is`(smallFontSize))
    }

    @Test
    fun factTextWithAdaptedSize_should_set_the_font_size_to_fact_big_font_size_when_the_char_count_is_lessOrEqual_a_fact_char_count_for_small_font() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val view = TextView(context)
        view.setText("")

        val charCount = context.resources.getInteger(R.integer.fact_char_count_for_small_font)

        val text = "G".repeat(charCount-1)

        val bigFontSize = context.resources.getDimension(R.dimen.fact_big_font_size)

        view.factTextWithAdaptedSize(text)

        assertThat(view.textSize, `is`(bigFontSize))
    }
}