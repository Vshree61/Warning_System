package com.example.warningsystem;

import android.view.View;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import junit.extensions.ActiveTestSuite;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
public class LocationManagerTest {
    @Rule
    public ActivityTestRule<LocationManager> activityTestRule = new ActivityTestRule<LocationManager>(LocationManager.class);
    private LocationManager manager = null;
    @Before
    public void setUp() throws Exception {
        manager = activityTestRule.getActivity();
    }

    @Test
    public void testLaunch(){
        View  latView = manager.findViewById(R.id.latTextView);
        View lonView = manager.findViewById(R.id.lonTextView);
        assertNotNull(latView);
        assertNotNull(lonView);
    }

    @After
    public void tearDown() throws Exception {
        manager = null;
    }
}