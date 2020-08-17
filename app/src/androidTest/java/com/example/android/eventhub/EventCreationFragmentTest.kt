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
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EventCreationFragmentTest {
     @Before
     fun init() {
         launchFragmentInContainer<EventCreationFragment>(themeResId = R.style.AppTheme)
     }

    @Test
    fun testCreateEvent_nameEmpty() {
        onView(withId(R.id.txt_event_creation_name)).perform(typeText(""))
        onView(withId(R.id.btn_event_creation_create)).perform(click())
        onView(withId(R.id.txt_event_creation_name_error)).check(matches(isDisplayed()))
    }

    @Test
    fun testCreateEvent_placeEmpty() {
        onView(withId(R.id.txt_event_creation_place)).perform(typeText(""))
        onView(withId(R.id.btn_event_creation_create)).perform(click())
        onView(withId(R.id.txt_event_creation_place_error)).check(matches(isDisplayed()))
    }

    @Test
    fun testCreateEvent_descriptionEmpty() {
        onView(withId(R.id.txt_Event_creation_description)).perform(typeText(""))
        onView(withId(R.id.btn_event_creation_create)).perform(click())
        onView(withId(R.id.txt_event_creation_description_error)).check(matches(isDisplayed()))
    }

    @Test
    fun testCreateEvent_dateEmpty() {
        onView(withId(R.id.btn_event_creation_create)).perform(click())
        onView(withId(R.id.txt_event_creation_date_error)).check(matches(isDisplayed()))
    }

    @Test
    fun testCreateEvent_timeEmpty() {
        onView(withId(R.id.btn_event_creation_create)).perform(click())
        onView(withId(R.id.txt_event_creation_time_error)).check(matches(isDisplayed()))
    }

    @Test
    fun testCreateEvent_imageEmpty() {
        onView(withId(R.id.btn_event_creation_create)).perform(click())
        onView(withId(R.id.txt_event_creation_image_error)).check(matches(isDisplayed()))
    }
}