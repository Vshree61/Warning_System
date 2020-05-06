package com.example.warningsystem;

import android.app.Activity;
import android.app.Instrumentation;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

public class RegisterTest {
    @Rule
    public ActivityTestRule<Register> activityTestRule = new ActivityTestRule<Register>(Register.class);
    private Register manager = null;
    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(MainManager.class.getName(),null,false);
    @Before
    public void setUp() throws Exception {
        manager = activityTestRule.getActivity();
    }
    @Test
    public void onLoginBtnClicked(){
        assertNotNull(manager.findViewById(R.id.Email));
        assertNotNull(manager.findViewById(R.id.CreatePsw));
        assertNotNull(manager.findViewById(R.id.ConfirmPsw));

        assertNotNull(manager.findViewById(R.id.registerBtn));
        onView(withId(R.id.registerBtn)).perform(click());
        Activity mainManager = getInstrumentation().waitForMonitor(monitor);
        assertNotNull(mainManager);
        mainManager.finish();
    }
    @After
    public void tearDown() throws Exception {
        manager = null;
    }

}