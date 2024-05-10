package com.example.simulation2.objects;

import android.graphics.Bitmap;

import com.example.simulation2.R;
import com.example.simulation2.engine.GameObject;
import com.example.simulation2.structs.Sprite;
import com.example.simulation2.structs.Vector2;

import java.util.ArrayList;
import java.util.Random;

public class Coin extends GameObject
{
    // Logic
    private float[] range = new float[2];

    // Render
    private final ArrayList<Bitmap> sprites = new ArrayList<Bitmap>();
    private long lastAnimationTime;
    private int currentSpriteIndex;

    /**
     * Constructs a new GameObject instance with the given position and size.
     *
     * @param position The top-left position of the game object
     * @param size     The size of the game object
     */
    public Coin(Vector2 position, Vector2 size)
    {
        super(position, size);

        // Initialize States
        range[0] = 0;
        range[1] = 0;

        // Initialize all sprites
        sprites.add(Sprite.loadSprite(R.drawable.coin0));
        sprites.add(Sprite.loadSprite(R.drawable.coin1));
        sprites.add(Sprite.loadSprite(R.drawable.coin2));
        sprites.add(Sprite.loadSprite(R.drawable.coin3));
        sprites.add(Sprite.loadSprite(R.drawable.coin4));
        sprites.add(Sprite.loadSprite(R.drawable.coin5));
        sprites.add(Sprite.loadSprite(R.drawable.coin6));
        sprites.add(Sprite.loadSprite(R.drawable.coin7));

        currentSpriteIndex = 0;
        sprite = sprites.get(0);
        spriteOffset = new Vector2(-8, -8);
    }

    public void applyAnimation(long frameRate)
    {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastAnimationTime;

        if (elapsedTime >= 1000 / frameRate)
        {
            // Change Coin Subsequent Sprite
            currentSpriteIndex = (currentSpriteIndex + 1) % sprites.size();
            setSprite(sprites.get(currentSpriteIndex));

            lastAnimationTime = currentTime;
        }
    }

    public void boundYRange(float lower, float upper)
    {
        range[0] = lower;
        range[1] = upper;
    }

    public void randomizeY()
    {
        Random random = new Random();
        float randomValue = random.nextFloat();
        float range = this.range[1] - this.range[0];

        position.y = this.range[0] + randomValue * range;
    }
}
