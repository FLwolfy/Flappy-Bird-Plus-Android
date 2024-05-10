package com.example.simulation2.objects;

import com.example.simulation2.R;
import com.example.simulation2.engine.GameObject;
import com.example.simulation2.main.Settings;
import com.example.simulation2.structs.Sprite;
import com.example.simulation2.structs.Vector2;

public class BaseGround extends GameObject
{
    /**
     * Constructs a new GameObject instance with the given position and size.
     *
     * @param position The top-left position of the game object
     * @param size     The size of the game object
     */
    public BaseGround(Vector2 position, Vector2 size)
    {
        super(position, size);

        // Initialize  Sprite
        sprite = Sprite.loadSprite(R.drawable.base);
    }
}
