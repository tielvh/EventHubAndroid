package com.example.android.eventhub

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.eventhub.ui.eventcreation.EventCreationFragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EventCreationFragmentTest {
    @Test
    fun testCreateEvent_nameEmpty() {
        launchFragmentInContainer<EventCreationFragment>(themeResId = R.style.AppTheme)
        onView(withId(R.id.txt_event_creation_name)).perform(typeText(""))
        onView(withId(R.id.btn_event_creation_create)).perform(click())
        onView(withId(R.id.txt_event_creation_name_error)).check(matches(isDisplayed()))
    }
}