package com.example.simulation2.objects;

import android.graphics.Bitmap;

import com.example.simulation2.R;
import com.example.simulation2.engine.GameObject;
import com.example.simulation2.structs.Sprite;
import com.example.simulation2.structs.Vector2;

import java.util.ArrayList;

public class Heart extends GameObject
{
    // Render
    private final ArrayList<Bitmap> sprites = new ArrayList<Bitmap>();

    /**
     * Constructs a new GameObject instance with the given position and size.
     *
     * @param position The top-left position of the game object
     * @param size     The size of the game object
     */
    public Heart(Vector2 position, Vector2 size)
    {
        super(position, size);

        // Initialize all sprites
        sprites.add(Sprite.loadSprite(R.drawable.hearts0));
        sprites.add(Sprite.loadSprite(R.drawable.hearts1));
        sprites.add(Sprite.loadSprite(R.drawable.hearts2));
        sprites.add(Sprite.loadSprite(R.drawable.hearts3));
        sprites.add(Sprite.loadSprite(R.drawable.hearts4));
        sprites.add(Sprite.loadSprite(R.drawable.hearts5));
        sprites.add(Sprite.loadSprite(R.drawable.hearts6));
        sprites.add(Sprite.loadSprite(R.drawable.hearts7));
        sprites.add(Sprite.loadSprite(R.drawable.hearts8));
        sprites.add(Sprite.loadSprite(R.drawable.hearts9));
        sprites.add(Sprite.loadSprite(R.drawable.hearts10));

        sprite = sprites.get(10);
    }

    public void setHeartHealth(int health)
    {
        if (health >= 0 && health <= 10)
        {
            sprite = sprites.get(health);
        }
        else if (health < 0)
        {
            sprite = sprites.get(0);
        }
        else
        {
            sprite = sprites.get(10);
        }
    }
}
