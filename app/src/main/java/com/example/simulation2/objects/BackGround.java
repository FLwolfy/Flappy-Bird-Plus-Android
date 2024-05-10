package com.example.simulation2.objects;

import android.graphics.Bitmap;

import com.example.simulation2.R;
import com.example.simulation2.engine.GameConstants;
import com.example.simulation2.engine.GameObject;
import com.example.simulation2.structs.Sprite;
import com.example.simulation2.structs.Vector2;

import java.util.ArrayList;
import java.util.Calendar;

public class BackGround extends GameObject
{
    // Time
    private Calendar calendar;

    // Render
    private ArrayList<Bitmap> themeSprites = new ArrayList<Bitmap>();
    private int CurrentSpriteIndex;

    public BackGround(Vector2 position, Vector2 size)
    {
        super(position, size);

        // Initialize Time
        calendar = Calendar.getInstance();

        // Initialize Sprite
        themeSprites.add(Sprite.loadSprite(R.drawable.background_day));
        themeSprites.add(Sprite.loadSprite(R.drawable.background_night));

        themeCheck();
    }

    public void themeCheck()
    {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        boolean isNightMode = (hour >= 18 || hour < 6);

        if (isNightMode)
        {
            CurrentSpriteIndex = 1;
        }
        else
        {
            CurrentSpriteIndex = 0;
        }

        sprite = themeSprites.get(CurrentSpriteIndex);
    }
}
