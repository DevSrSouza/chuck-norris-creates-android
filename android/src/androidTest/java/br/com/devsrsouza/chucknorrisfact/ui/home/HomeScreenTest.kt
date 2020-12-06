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
import br.com.devsrsouza.chucknorrisfact.repository.FakeChuckNorrisFactsRepository
import br.com.devsrsouza.chucknorrisfact.util.DataBindingIdlingResource
import br.com.devsrsouza.chucknorrisfact.util.monitorActivity
import br.com.devsrsouza.chucknorrisfacts.ui.MainActivity
import br.com.devsrsouza.chucknorrisfacts.R
import br.com.devsrsouza.chucknorrisfacts.di.ServiceLocator
import br.com.devsrsouza.chucknorrisfacts.repository.model.Fact
import br.com.devsrsouza.chucknorrisfacts.repository.model.mainCategoryOrNull
import br.com.devsrsouza.chucknorrisfacts.ui.home.HomeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    private val fakeRepository: FakeChuckNorrisFactsRepository = FakeChuckNorrisFactsRepository()
    private val networkAvailabilityState: MutableStateFlow<Boolean> = MutableStateFlow(true)

    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun setup() {
        HomeViewModel.SEARCH_DEBOUNCE_TIME_MS = 0

        ServiceLocator.factsRepository = fakeRepository
        ServiceLocator.networkStateFlow = networkAvailabilityState
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
}