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
import br.com.devsrsouza.chucknorrisfacts.MainActivity
import br.com.devsrsouza.chucknorrisfacts.R
import br.com.devsrsouza.chucknorrisfacts.di.ServiceLocator
import br.com.devsrsouza.chucknorrisfacts.repository.model.Fact
import br.com.devsrsouza.chucknorrisfacts.repository.model.mainCategoryOrNull
import br.com.devsrsouza.chucknorrisfacts.ui.home.HomeViewModel
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    private lateinit var repository: FakeChuckNorrisFactsRepository

    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun initRepository() {
        repository = FakeChuckNorrisFactsRepository()
        HomeViewModel.SEARCH_DEBOUNCE_TIME_MS = 0
        ServiceLocator.factsRepository = repository
    }

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Test
    fun Should_display_the_fact_card_when_user_search_success() {
        val fact = Fact(
                "0001",
                listOf("Games"),
                "One Chuck Norris Fact"
        )
        repository.setSuccess(listOf(fact))

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        onView(withId(R.id.fact_recyclerview)).check(matches(not(isDisplayed())))
        onView(withId(R.id.search_item)).perform(click())
        onView(isAssignableFrom(EditText::class.java)).perform(typeText("Dota"))

        onView(withText(fact.value)).check(matches(isDisplayed()))
        onView(withText(fact.mainCategoryOrNull)).check(matches(isDisplayed()))
    }

    @Test
    fun Should_display_the_fact_card_with_no_category_as_uncategorized_when_search_result_contains_no_category() {
        val fact = Fact(
            "0002",
            emptyList(),
            "Second Chuck Norris Fact"
        )
        repository.setSuccess(listOf(fact))

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        onView(withId(R.id.fact_recyclerview)).check(matches(not(isDisplayed())))
        onView(withId(R.id.search_item)).perform(click())
        onView(isAssignableFrom(EditText::class.java)).perform(typeText("Dota"))

        onView(withText(fact.value)).check(matches(isDisplayed()))
        onView(withText(R.string.uncategorized_fact)).check(matches(isDisplayed()))
    }
}