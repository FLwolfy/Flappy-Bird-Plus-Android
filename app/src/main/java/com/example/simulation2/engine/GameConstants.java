package com.example.simulation2.engine;

import android.annotation.SuppressLint;
import android.content.Context;

/**
 * Class storing global constants. Constants inside need to be initialized during OnCreate() in MainActivity.
 */
public class GameConstants
{
    /**
     * Screen width of the user device.
     */
    public static int SCREEN_WIDTH;

    /**
     * Screen height of the user device.
     */
    public static int SCREEN_HEIGHT;

    /**
     * The context of the current application.
     */
    @SuppressLint("StaticFieldLeak")
    public static Context CONTEXT;

    /**
     * The frame rate of the game.
     */
    public static long MAX_FRAME_RATE = 60;

    /**
     * The elapsed time for functioning between two fixed logical update. (millisecond)
     */
    public static long FIXED_DELTA_TIME = 20;

    /**
     * The elapsed time for functioning between two frame. (millisecond)
     */
    public static long DELTA_TIME;

    public static float GRAVITY_SCALE = 0.0075f;

    public static float MAX_FALL_SPEED = 3.5f;

}
