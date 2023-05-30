package com.example.remotemonitoringapp;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.remotemonitoringapp.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest4 {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void mainActivityTest4() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.employeeLogin), withText("USER"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        3),
                                1),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.employeeLogin), withText("USER"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        3),
                                1),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.Email),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText.perform(replaceText(""), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.Email), withText("test@gmail.com"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText2.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.Email), withText("test@gmail.com"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("test@"));

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.Email), withText("test@"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText4.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.Email), withText("test@"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText5.perform(click());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.Email), withText("test@"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText("test@gmail.com"));

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.Email), withText("test@gmail.com"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText7.perform(closeSoftKeyboard());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.employeeLoginBtn), withText("LOGIN"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction cardView = onView(
                allOf(withId(R.id.Tasks),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                0),
                        isDisplayed()));
        cardView.perform(click());

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.task_desc), withText("Appointment title"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.swiperefresh_recycler),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText8.perform(replaceText(""));

        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.task_desc),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.swiperefresh_recycler),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText9.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText10 = onView(
                allOf(withId(R.id.task_desc),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.swiperefresh_recycler),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText10.perform(click());

        ViewInteraction appCompatEditText11 = onView(
                allOf(withId(R.id.task_desc),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.swiperefresh_recycler),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText11.perform(click());

        ViewInteraction appCompatEditText12 = onView(
                allOf(withId(R.id.task_desc),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.swiperefresh_recycler),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText12.perform(replaceText("running anespresso"), closeSoftKeyboard());

        ViewInteraction appCompatEditText13 = onView(
                allOf(withId(R.id.task_desc), withText("running anespresso"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.swiperefresh_recycler),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText13.perform(click());

        ViewInteraction appCompatEditText14 = onView(
                allOf(withId(R.id.task_desc), withText("running anespresso"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.swiperefresh_recycler),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText14.perform(click());

        ViewInteraction appCompatEditText15 = onView(
                allOf(withId(R.id.task_desc), withText("running anespresso"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.swiperefresh_recycler),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText15.perform(click());

        ViewInteraction appCompatEditText16 = onView(
                allOf(withId(R.id.task_desc), withText("running anespresso"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.swiperefresh_recycler),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText16.perform(click());

        ViewInteraction appCompatEditText17 = onView(
                allOf(withId(R.id.task_desc), withText("running anespresso"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.swiperefresh_recycler),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText17.perform(replaceText("running an espresso"));

        ViewInteraction appCompatEditText18 = onView(
                allOf(withId(R.id.task_desc), withText("running an espresso"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.swiperefresh_recycler),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText18.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText19 = onView(
                allOf(withId(R.id.task_desc), withText("running an espresso"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.swiperefresh_recycler),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText19.perform(click());

        ViewInteraction appCompatEditText20 = onView(
                allOf(withId(R.id.task_desc), withText("running an espresso"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.swiperefresh_recycler),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText20.perform(replaceText("running an espresso record  test on the application "));

        ViewInteraction appCompatEditText21 = onView(
                allOf(withId(R.id.task_desc), withText("running an espresso record  test on the application "),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.swiperefresh_recycler),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText21.perform(closeSoftKeyboard());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.btn_calender_stop_date), withText("SET DATE"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton5.perform(scrollTo(), click());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.btn_calender_stop_time), withText("SET TIME"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                0),
                        isDisplayed()));
        appCompatButton6.perform(click());

        ViewInteraction appCompatButton7 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton7.perform(scrollTo(), click());

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.employee),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.swiperefresh_recycler),
                                        0),
                                4),
                        isDisplayed()));
        appCompatSpinner.perform(click());

        DataInteraction appCompatTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(2);
        appCompatTextView.perform(click());

        ViewInteraction appCompatButton8 = onView(
                allOf(withId(R.id.create_task), withText("SET APPOINTMENT"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.swiperefresh_recycler),
                                        0),
                                5),
                        isDisplayed()));
        appCompatButton8.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
