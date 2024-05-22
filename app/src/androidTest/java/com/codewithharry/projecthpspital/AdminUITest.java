package com.codewithharry.projecthpspital;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

@RunWith(AndroidJUnit4.class)
public class AdminUITest {

    @Rule
    public ActivityScenarioRule<Admin> activityRule =
            new ActivityScenarioRule<>(Admin.class);

    @Test
    public void testAdminIdEditTextIsDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.adminidID))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testAdminPasswordEditTextIsDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.adminPasswordID))
                .check(matches(isDisplayed()));
    }
}