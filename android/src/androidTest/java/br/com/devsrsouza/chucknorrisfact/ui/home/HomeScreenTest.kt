package br.com.devsrsouza.chucknorrisfact.ui.home

import android.widget.EditText
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.devsrsouza.chucknorrisfact.repository.FakeChuckNorrisFactsRepository
import br.com.devsrsouza.chucknorrisfact.testutil.DataBindingIdlingResource
import br.com.devsrsouza.chucknorrisfact.testutil.monitorActivity
import br.com.devsrsouza.chucknorrisfact.testutil.withFontSize
import br.com.devsrsouza.chucknorrisfact.util.share.FakeContentShare
import br.com.devsrsouza.chucknorrisfacts.ui.MainActivity
import br.com.devsrsouza.chucknorrisfacts.R
import br.com.devsrsouza.chucknorrisfacts.di.ServiceLocator
import br.com.devsrsouza.chucknorrisfacts.repository.model.Fact
import br.com.devsrsouza.chucknorrisfacts.repository.model.mainCategoryOrNull
import br.com.devsrsouza.chucknorrisfacts.ui.home.HomeViewModel
import br.com.devsrsouza.chucknorrisfacts.util.share.ContentShare
import kotlinx.coroutines.flow.MutableStateFlow
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    private val fakeRepository: FakeChuckNorrisFactsRepository = FakeChuckNorrisFactsRepository()
    private val networkAvailabilityState: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val fakeContentShare: FakeContentShare = FakeContentShare()

    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun setup() {
        HomeViewModel.SEARCH_DEBOUNCE_TIME_MS = 0

        ServiceLocator.factsRepository = fakeRepository
        ServiceLocator.networkStateFlow = networkAvailabilityState
        ServiceLocator.contentShare = fakeContentShare
    }

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @After
    fun reset() {
        fakeRepository.reset()
        networkAvailabilityState.value = true
        fakeContentShare.reset()
    }

    @Test
    fun Should_display_the_fact_card_when_user_search_success() {
        val fact = Fact(
                "0001",
                listOf("Games"),
                "One Chuck Norris Fact"
        )
        fakeRepository.setSuccess(listOf(fact))
        fakeRepository.setResponseDelay(1000)

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        onView(withId(R.id.fact_recyclerview)).check(matches(not(isDisplayed())))
        onView(withId(R.id.search_item)).perform(click())
        onView(isAssignableFrom(EditText::class.java)).perform(typeText("Dota"))

        onView(withId(R.id.loading_view)).check(matches(isDisplayed()))

        Thread.sleep(1100)

        onView(withText(fact.value)).check(matches(isDisplayed()))
        onView(withText(fact.mainCategoryOrNull)).check(matches(isDisplayed()))

        activityScenario.close()
    }

    @Test
    fun Should_display_the_fact_card_with_no_category_as_uncategorized_when_search_result_contains_no_category() {
        val fact = Fact(
            "0002",
            emptyList(),
            "Second Chuck Norris Fact"
        )
        fakeRepository.setSuccess(listOf(fact))
        fakeRepository.setResponseDelay(1000)

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        onView(withId(R.id.fact_recyclerview)).check(matches(not(isDisplayed())))
        onView(withId(R.id.search_item)).perform(click())
        onView(isAssignableFrom(EditText::class.java)).perform(typeText("Dota"))

        onView(withId(R.id.loading_view)).check(matches(isDisplayed()))

        Thread.sleep(1100)

        onView(withText(fact.value)).check(matches(isDisplayed()))
        onView(withText(R.string.uncategorized_fact)).check(matches(isDisplayed()))

        activityScenario.close()
    }

    @Test
    fun Should_display_network_error_message_when_there_is_no_network_available() {
        val fact = Fact(
            "0002",
            emptyList(),
            "Second Chuck Norris Fact"
        )
        fakeRepository.setSuccess(listOf(fact))
        fakeRepository.setResponseDelay(1000)
        networkAvailabilityState.value = false

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        onView(withId(R.id.fact_recyclerview)).check(matches(not(isDisplayed())))
        onView(withId(R.id.search_item)).perform(click())
        onView(isAssignableFrom(EditText::class.java)).perform(typeText("Dota"))

        onView(withId(R.id.error_layout)).check(matches(isDisplayed()))
        onView(withText(R.string.network_not_available)).check(matches(isDisplayed()))

        networkAvailabilityState.value = true

        onView(withId(R.id.loading_view)).check(matches(isDisplayed()))

        Thread.sleep(1100)

        onView(withText(fact.value)).check(matches(isDisplayed()))
        onView(withText(R.string.uncategorized_fact)).check(matches(isDisplayed()))

        activityScenario.close()
    }

    @Test
    fun Should_display_facts_not_found_when_the_search_result_is_empty() {
        fakeRepository.setSuccess(emptyList())
        fakeRepository.setResponseDelay(1000)

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        onView(withId(R.id.fact_recyclerview)).check(matches(not(isDisplayed())))
        onView(withId(R.id.search_item)).perform(click())
        onView(isAssignableFrom(EditText::class.java)).perform(typeText("Dota"))

        onView(withId(R.id.loading_view)).check(matches(isDisplayed()))

        Thread.sleep(1100)

        onView(withText(R.string.no_fact_found)).check(matches(isDisplayed()))

        activityScenario.close()
    }

    @Test
    fun Should_display_network_client_error_message_when_the_search_returns_network_client_error() {
        val clientErrorMessage = "Simple error message"
        fakeRepository.setClientError(clientErrorMessage)
        fakeRepository.setResponseDelay(1000)

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        onView(withId(R.id.fact_recyclerview)).check(matches(not(isDisplayed())))
        onView(withId(R.id.search_item)).perform(click())
        onView(isAssignableFrom(EditText::class.java)).perform(typeText("Dota"))

        onView(withId(R.id.loading_view)).check(matches(isDisplayed()))

        Thread.sleep(1100)

        onView(withText(clientErrorMessage)).check(matches(isDisplayed()))

        activityScenario.close()
    }

    @Test
    fun Should_display_error_message_when_the_search_returns_error() {
        fakeRepository.setError()
        fakeRepository.setResponseDelay(1000)

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        onView(withId(R.id.fact_recyclerview)).check(matches(not(isDisplayed())))
        onView(withId(R.id.search_item)).perform(click())
        onView(isAssignableFrom(EditText::class.java)).perform(typeText("Dota"))

        onView(withId(R.id.loading_view)).check(matches(isDisplayed()))

        Thread.sleep(1100)

        onView(withText(R.string.search_error)).check(matches(isDisplayed()))

        activityScenario.close()
    }

    @Test
    fun Should_display_the_fact_with_the_bigger_font() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val charCount = context.resources.getInteger(R.integer.fact_char_count_for_small_font)
        val expectedFontSize = context.resources.getDimension(R.dimen.fact_big_font_size)

        val text = "G".repeat(charCount-1)

        val fact = Fact(
                "0001",
                listOf("Games"),
                text
        )
        fakeRepository.setSuccess(listOf(fact))
        fakeRepository.setResponseDelay(1000)

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        onView(withId(R.id.fact_recyclerview)).check(matches(not(isDisplayed())))
        onView(withId(R.id.search_item)).perform(click())
        onView(isAssignableFrom(EditText::class.java)).perform(typeText("Dota"))

        onView(withId(R.id.loading_view)).check(matches(isDisplayed()))

        Thread.sleep(1100)

        onView(withText(fact.value)).check(matches(isDisplayed()))
        onView(withText(fact.mainCategoryOrNull)).check(matches(isDisplayed()))
        onView(withText(fact.value)).check(matches(withFontSize(expectedFontSize)))

        activityScenario.close()
    }

    @Test
    fun Should_display_the_fact_with_the_small_font() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val charCount = context.resources.getInteger(R.integer.fact_char_count_for_small_font)
        val expectedFontSize = context.resources.getDimension(R.dimen.fact_small_font_size)

        val text = "G".repeat(charCount)

        val fact = Fact(
                "0001",
                listOf("Games"),
                text
        )
        fakeRepository.setSuccess(listOf(fact))
        fakeRepository.setResponseDelay(1000)

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        onView(withId(R.id.fact_recyclerview)).check(matches(not(isDisplayed())))
        onView(withId(R.id.search_item)).perform(click())
        onView(isAssignableFrom(EditText::class.java)).perform(typeText("Dota"))

        onView(withId(R.id.loading_view)).check(matches(isDisplayed()))

        Thread.sleep(1100)

        onView(withText(fact.value)).check(matches(isDisplayed()))
        onView(withText(fact.mainCategoryOrNull)).check(matches(isDisplayed()))
        onView(withText(fact.value)).check(matches(withFontSize(expectedFontSize)))

        activityScenario.close()
    }

    @Test
    fun Should_share_the_fact_when_clicked_on_share_button() {
        val fact = Fact(
                "0001",
                listOf("Games"),
                "One Chuck Norris Fact"
        )
        fakeRepository.setSuccess(listOf(fact))
        fakeRepository.setResponseDelay(1000)

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        onView(withId(R.id.fact_recyclerview)).check(matches(not(isDisplayed())))
        onView(withId(R.id.search_item)).perform(click())
        onView(isAssignableFrom(EditText::class.java)).perform(typeText("Dota"))

        onView(withId(R.id.loading_view)).check(matches(isDisplayed()))

        Thread.sleep(1100)

        onView(withId(R.id.share_button))
                .check(matches(isDisplayed()))
                .perform(click())

        assertThat(fakeContentShare.textToBeShared, `is`(fact.value))

        activityScenario.close()
    }
}