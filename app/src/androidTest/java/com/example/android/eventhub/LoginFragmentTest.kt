package com.example.android.eventhub

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.eventhub.ui.login.LoginFragment
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginFragmentTest {
    @Before
    fun setUp() {
        launchFragmentInContainer<LoginFragment>(themeResId = R.style.AppTheme)
    }

    @Test
    fun testLogin_usernameEmpty() {
        onView(withId(R.id.txt_login_username)).perform(typeText(""))
        onView(withId(R.id.btn_login)).perform(click())
        onView(withId(R.id.txt_login_username_error)).check(matches(isDisplayed()))
    }

    @Test
    fun testLogin_passwordEmpty() {
        onView(withId(R.id.txt_login_password)).perform(typeText(""))
        onView(withId(R.id.btn_login)).perform(click())
        onView(withId(R.id.txt_login_password_error)).check(matches(isDisplayed()))
    }
}