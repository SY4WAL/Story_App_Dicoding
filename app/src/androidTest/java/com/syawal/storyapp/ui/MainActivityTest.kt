package com.syawal.storyapp.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openContextualActionModeOverflowMenu
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasErrorText
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.syawal.storyapp.JsonConverter
import com.syawal.storyapp.R
import com.syawal.storyapp.data.api.ApiConfig
import com.syawal.storyapp.utils.EspressoIdlingResource
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class MainActivityTest {

    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        ActivityScenario.launch(MainActivity::class.java)
        mockWebServer.start(8080)
        ApiConfig.ENDPOINT = "http://127.0.0.1:8080/"
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun emailAndPassword_Error_inEnglish() {
        onView(withId(R.id.tagline))
            .check(matches(isDisplayed()))
        onView(withId(R.id.btn_login))
            .perform(click())

        onView(withId(R.id.ed_login_email))
            .perform(typeText("syawal"), closeSoftKeyboard())
        onView(withId(R.id.ed_login_email))
            .check(matches(hasErrorText("Invalid email address")))

        onView(withId(R.id.ed_login_password))
            .perform(typeText("123"), closeSoftKeyboard())
        onView(withId(R.id.ed_login_password))
            .check(matches(hasErrorText("Password must contain 8 character")))
    }

    @Test
    fun loginLogout_Success() {
        //do login
        onView(withId(R.id.btn_login))
            .perform(click())
        onView(withId(R.id.ed_login_email))
            .perform(typeText("syawal@test.com"), closeSoftKeyboard())
        onView(withId(R.id.ed_login_password))
            .perform(typeText("12345678"), closeSoftKeyboard())
        onView(withId(R.id.btn_login))
            .perform(click())

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("success_response.json"))
        mockWebServer.enqueue(mockResponse)

        //check is home fragment displayed
        onView(withId(R.id.toolbar))
            .check(matches(isDisplayed()))

        //do logout
        openContextualActionModeOverflowMenu()
        onView(withText("LogOut")).perform(click())

        //check is welcome fragment displayed
        onView(withId(R.id.app_logo))
            .check(matches(isDisplayed()))
    }
}