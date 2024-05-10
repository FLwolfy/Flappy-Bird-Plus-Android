package com.example.simulation2.main;

public class Settings
{
    public static int BirdAvatar = Avatar.BLUE;
    public static float Speed = SceneSpeed.SLOW;
    public static long EventRate = EventFrequency.LOW;

    public static class Avatar
    {
        public static final int BLUE = 0;
        public static final int RED = 1;
        public static final int YELLOW = 2;
    }

    public static class SceneSpeed
    {
        public static final float SLOW = 0.55f;
        public static final float MEDIUM = 0.7f;
        public static final float FAST = 0.85f;
    }

    public static class EventFrequency {
        public static final long LOW = 20;
        public static final long MEDIUM = 30;
        public static final long HIGH = 40;
    }
}
