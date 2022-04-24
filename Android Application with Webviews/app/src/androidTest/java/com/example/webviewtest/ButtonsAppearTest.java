package com.example.webviewtest;


import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ButtonsAppearTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void buttonsAppearTest() {
        ViewInteraction textView = onView(
                allOf(withText("The Vault"),
                        withParent(allOf(withId(R.id.action_bar),
                                withParent(withId(R.id.action_bar_container)))),
                        isDisplayed()));
        textView.check(matches(withText("The Vault")));

        ViewInteraction button = onView(
                allOf(withId(R.id.lockbutton), withText("LOCKS"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.cambutton), withText("CAMERAS"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction button3 = onView(
                allOf(withId(R.id.alarmbutton), withText("ALARM"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button3.check(matches(isDisplayed()));

        ViewInteraction button4 = onView(
                allOf(withId(R.id.bluetoothbtn), withText("BLUETOOTH"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button4.check(matches(isDisplayed()));

        ViewInteraction button5 = onView(
                allOf(withId(R.id.profile), withText("REQUESTS"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button5.check(matches(isDisplayed()));

        ViewInteraction button6 = onView(
                allOf(withId(R.id.profile), withText("REQUESTS"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button6.check(matches(isDisplayed()));

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.lockbutton), withText("Locks"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withText("The Vault"),
                        withParent(allOf(withId(R.id.action_bar),
                                withParent(withId(R.id.action_bar_container)))),
                        isDisplayed()));
        textView2.check(matches(withText("The Vault")));

        ViewInteraction button7 = onView(
                allOf(withId(R.id.frontlock), withText("FRONT DOOR"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button7.check(matches(isDisplayed()));

        ViewInteraction button8 = onView(
                allOf(withId(R.id.garagelock), withText("GARAGE DOOR"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button8.check(matches(isDisplayed()));

        ViewInteraction button9 = onView(
                allOf(withId(R.id.backlock), withText("BACK DOOR"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button9.check(matches(isDisplayed()));

        pressBack();

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.cambutton), withText("Cameras"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction textView3 = onView(
                allOf(withText("The Vault"),
                        withParent(allOf(withId(R.id.action_bar),
                                withParent(withId(R.id.action_bar_container)))),
                        isDisplayed()));
        textView3.check(matches(withText("The Vault")));

        ViewInteraction button10 = onView(
                allOf(withId(R.id.camera1), withText("CAMERA 1"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button10.check(matches(isDisplayed()));

        ViewInteraction button11 = onView(
                allOf(withId(R.id.camera2), withText("CAMERA 2"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button11.check(matches(isDisplayed()));

        ViewInteraction button12 = onView(
                allOf(withId(R.id.camera3), withText("CAMERA 3"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button12.check(matches(isDisplayed()));

        pressBack();

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.alarmbutton), withText("Alarm"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction textView4 = onView(
                allOf(withText("The Vault"),
                        withParent(allOf(withId(R.id.action_bar),
                                withParent(withId(R.id.action_bar_container)))),
                        isDisplayed()));
        textView4.check(matches(withText("The Vault")));

        ViewInteraction button13 = onView(
                allOf(withId(R.id.masterarm), withText("MASTER ARM"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button13.check(matches(isDisplayed()));

        ViewInteraction button14 = onView(
                allOf(withId(R.id.garagedoor), withText("GARAGE DOOR"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button14.check(matches(isDisplayed()));

        ViewInteraction button15 = onView(
                allOf(withId(R.id.frontdoor), withText("FRONT DOOR"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button15.check(matches(isDisplayed()));

        pressBack();

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.bluetoothbtn), withText("Bluetooth"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction textView5 = onView(
                allOf(withText("The Vault"),
                        withParent(allOf(withId(R.id.action_bar),
                                withParent(withId(R.id.action_bar_container)))),
                        isDisplayed()));
        textView5.check(matches(withText("The Vault")));
/*
        ViewInteraction button16 = onView(
                allOf(withId(R.id.connectToSelected), withText("CONNECT TO SELECTED DEVICE"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button16.check(matches(isDisplayed()));*/

        pressBack();

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.profile), withText("Requests"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton5.perform(click());

        ViewInteraction textView6 = onView(
                allOf(withText("The Vault"),
                        withParent(allOf(withId(R.id.action_bar),
                                withParent(withId(R.id.action_bar_container)))),
                        isDisplayed()));
        textView6.check(matches(withText("The Vault")));

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.responses), withText("Hello World!"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView7.check(matches(isDisplayed()));

        ViewInteraction button17 = onView(
                allOf(withId(R.id.GetReq), withText("GET REQUEST"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button17.check(matches(isDisplayed()));

        ViewInteraction button18 = onView(
                allOf(withId(R.id.GetReq2), withText("GET REQUEST 2"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button18.check(matches(isDisplayed()));

        ViewInteraction button19 = onView(
                allOf(withId(R.id.PostReq), withText("POST REQUEST"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button19.check(matches(isDisplayed()));

        ViewInteraction button20 = onView(
                allOf(withId(R.id.PostReq), withText("POST REQUEST"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button20.check(matches(isDisplayed()));
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
