package com.example.parsler.parsler;

import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.CloseKeyboardAction;
import android.test.ActivityInstrumentationTestCase2;
import android.text.format.DateUtils;
import android.view.View;

import com.example.parsler.parsler.Commons.StringConstants;
import com.example.parsler.parsler.Utility.ElapsedTimeIdlingResource;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Espresso 2.0 Functional Test Cases
 */
public class FunctionalTestCases extends ActivityInstrumentationTestCase2<LoginActivity> {

    private LoginActivity mActivity;

    public FunctionalTestCases() {
        super(LoginActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
    }

    public void loginFlow() {
        // Input dummy username in EditText
        ViewInteraction username = onView(withId(R.id.username));
        username.perform(typeText(StringConstants.TEST_USERNAME), closeSoftKeyboard());
        username.check(matches(withText(StringConstants.TEST_USERNAME)));

        // Input dummy password in EditText
        ViewInteraction password = onView(withId(R.id.password));
        password.perform(scrollTo(), typeText(StringConstants.TEST_PASSWORD), closeSoftKeyboard());
        password.check(matches(withText(StringConstants.TEST_PASSWORD)));

        waitFor(R.id.signinButton, StringConstants.SELECTOR_ID, DateUtils.SECOND_IN_MILLIS * 3, true);
        SystemClock.sleep(5000);
    }


    public void logoutFlow() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        waitFor(R.string.action_logout, StringConstants.SELECTOR_TEXT, DateUtils.SECOND_IN_MILLIS * 3, true);
    }


    @Test
    public void testCompleteAppFlow() {
        loginFlow();

        //waitFor(R.string.pickup_loc, StringConstants.SELECTOR_TEXT, DateUtils.SECOND_IN_MILLIS * 5, true);
        //onView(withText(R.string.pickup_loc)).perform(click());

        //onView(withId(R.id.active_pickup_summary)).perform(swipeLeft());
        //onView(withId(R.id.complete_pickup)).perform(swipeRight());
        //waitFor(R.id.active_pickup_summary, StringConstants.SELECTOR_ID, DateUtils.SECOND_IN_MILLIS * 3, true);
        //onView(withText(R.string.pickup_progress)).perform(swipeUp());
        //onView(withText(R.string.pickup_progress)).perform(swipeDown());

        //waitFor(R.id.pickup_done, StringConstants.SELECTOR_ID, DateUtils.SECOND_IN_MILLIS * 3, true);
        //waitFor(R.string.cancel, StringConstants.SELECTOR_TEXT, DateUtils.SECOND_IN_MILLIS * 3, true);

        logoutFlow();
    }

    @Test
    public void testNotificationFlow() {
        loginFlow();

        //waitFor(R.id.action_notification, StringConstants.SELECTOR_ID, DateUtils.SECOND_IN_MILLIS * 3, true);

        logoutFlow();
    }








    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }


    /**
     * Elapsed Time Idling Resource Espresso
     * @param view
     * @param id
     * @param waitingTime
     * @param success
     */
    private static void waitFor(int view, String id, long waitingTime, boolean success) {
        // Make sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(waitingTime * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(waitingTime * 2, TimeUnit.MILLISECONDS);

        // Now we wait
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(waitingTime);
        Espresso.registerIdlingResources(idlingResource);

        // Stop and verify
        if(id.equalsIgnoreCase(StringConstants.SELECTOR_ID)) {
            onView(withId(view)).perform(click());
        } else {
            onView(withText(view)).perform(click());
        }

        // Clean up
        Espresso.unregisterIdlingResources(idlingResource);
    }


    public static ViewAction closeSoftKeyboard() {
        return new ViewAction() {

            private static final long KEYBOARD_DISMISSAL_DELAY_MILLIS = 1000L;
            private final ViewAction mCloseSoftKeyboard = new CloseKeyboardAction();

            @Override
            public Matcher<View> getConstraints() {
                return mCloseSoftKeyboard.getConstraints();
            }

            @Override
            public String getDescription() {
                return mCloseSoftKeyboard.getDescription();
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                mCloseSoftKeyboard.perform(uiController, view);
                uiController.loopMainThreadForAtLeast(KEYBOARD_DISMISSAL_DELAY_MILLIS);
            }
        };
    }
}

