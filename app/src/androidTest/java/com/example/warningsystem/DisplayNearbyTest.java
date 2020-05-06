package com.example.warningsystem;

import android.view.View;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class DisplayNearbyTest {
    @Rule
    public ActivityTestRule<DisplayNearby> activityTestRule = new ActivityTestRule<DisplayNearby>(DisplayNearby.class);
    private DisplayNearby nearby = null;
    @Before
    public void setUp() throws Exception {
        nearby = activityTestRule.getActivity();
    }
    @Test
    public void TestDisplay(){
        View view = nearby.findViewById(R.id.nearbyLoc);
        assertNotNull(view);
    }
    @After
    public void tearDown() throws Exception {
        nearby = null;
    }
}