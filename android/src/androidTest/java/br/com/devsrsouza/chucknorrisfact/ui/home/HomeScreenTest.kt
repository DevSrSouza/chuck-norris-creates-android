package br.com.devsrsouza.chucknorrisfact.ui.home

import android.widget.EditText
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.devsrsouza.chucknorrisfact.repository.FakeChuckNorrisFactsRepository
import br.com.devsrsouza.chucknorrisfacts.R
import br.com.devsrsouza.chucknorrisfacts.di.ServiceLocator
import br.com.devsrsouza.chucknorrisfacts.repository.ChuckNorrisFactsRepository
import br.com.devsrsouza.chucknorrisfacts.ui.home.HomeFragment
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {

    private lateinit var repository: ChuckNorrisFactsRepository

    @Before
    fun initRepository() {
        repository = FakeChuckNorrisFactsRepository()
        ServiceLocator.factsRepository = repository
    }

    @After

    @Test
    fun User_search_should_result_in_one_fact_being_displayed() {
        launchFragmentInContainer<HomeFragment>()

        onView(withId(R.id.search_item)).perform(click())

        onView(isAssignableFrom(EditText::class.java)).perform(typeText("Dota"))

        Thread.sleep(5000)
    }
}