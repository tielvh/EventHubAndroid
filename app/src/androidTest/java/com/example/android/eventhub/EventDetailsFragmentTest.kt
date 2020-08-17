package com.example.android.eventhub

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.eventhub.domain.Event
import com.example.android.eventhub.ui.eventdetails.EventDetailsFragment
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class EventDetailsFragmentTest {
    private lateinit var context: Context

    private lateinit var editor: SharedPreferences.Editor

    private lateinit var args: Bundle

    @Before
    fun setUp() {
        context = App.getInstance().applicationContext
        editor = context.getSharedPreferences(
            context.getString(R.string.shared_preferences_name),
            Context.MODE_PRIVATE
        ).edit()

        val event = Event(
            name = "Test",
            dateTime = LocalDateTime.of(2020, 1, 1, 0, 0),
            place = "Test",
            description = "Test",
            imgPath = "https://eventhubweb4.azurewebsites.net/img/events/1.jpg"
        )
        args = Bundle().apply {
            putSerializable("event", event)
        }
    }

    @After
    fun clear() {
        editor.clear().apply()
    }

    private fun startWithUserLoggedIn() {
        editor.putString(
            context.getString(R.string.saved_user_token_key),
            // This JWT token is valid until 31/12/2020 23:59:59
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1bmlxdWVfbmFtZSI6IjEiLCJuYmYiOjE1OTcwNjk4NDcsImV4cCI6MTYwOTQ1NTU5OSwiaWF0IjoxNTk3MDY5ODQ3fQ.MoUNTFo242G-F_0lnaW9XGTxakK-bUZK7noZ51yPdt8"
        ).apply()

        launchFragmentInContainer<EventDetailsFragment>(
            fragmentArgs = args,
            themeResId = R.style.AppTheme
        )
    }

    private fun startWithUserLoggedOut() {
        editor.clear().apply()

        launchFragmentInContainer<EventDetailsFragment>(
            fragmentArgs = args,
            themeResId = R.style.AppTheme
        )
    }

    @Test
    fun testCommentsVisible_userLoggedIn() {
        startWithUserLoggedIn()

        onView(withId(R.id.comments)).check(matches(isDisplayed()))
    }

    @Test
    fun testCommentsNotVisible_userLoggedOut() {
        startWithUserLoggedOut()

        onView(withId(R.id.comments)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testCommentsNotVisible() {
        startWithUserLoggedIn()

        onView(withId(R.id.comments_content)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testCommentListVisible_buttonPressed() {
        startWithUserLoggedIn()

        onView(withId(R.id.btn_toggle_comments)).perform(click())

        onView(withId(R.id.comments_content)).check(matches(isDisplayed()))
    }
}