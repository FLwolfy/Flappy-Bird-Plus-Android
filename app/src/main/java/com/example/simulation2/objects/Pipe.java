package com.example.simulation2.objects;

import android.graphics.Bitmap;

import com.example.simulation2.R;
import com.example.simulation2.engine.GameConstants;
import com.example.simulation2.engine.GameObject;
import com.example.simulation2.main.Settings;
import com.example.simulation2.structs.Sprite;
import com.example.simulation2.structs.Vector2;

import java.util.ArrayList;
import java.util.Random;

public class Pipe extends GameObject
{
    // Constants
    public static final float INTERVAL = GameConstants.SCREEN_WIDTH * 0.9f;
    public static final int GAP = (int) (GameConstants.SCREEN_HEIGHT / 3.8f);
    private static final float MOVE_PIPE_CHANCE = 0.1f;

    // Logic
    private boolean isGoldenGet;
    private final Random random;
    private final float[] range = new float[2];

    // Render
    public int spriteIndex;
    private final ArrayList<Bitmap> sprites = new ArrayList<Bitmap>();

    /**
     * Constructs a new GameObject instance with the given position and size.
     *
     * @param position The top-left position of the game object
     * @param size     The size of the game object
     */
    public Pipe(Vector2 position, Vector2 size)
    {
        super(position, size);

        // Initialize States
        range[0] = 0;
        range[1] = 0;
        random = new Random();

        // Initialize all sprites
        sprites.add(Sprite.loadSprite(R.drawable.pipe_green));
        sprites.add(Sprite.loadSprite(R.drawable.pipe_red));
        sprites.add(Sprite.loadSprite(R.drawable.pipe_golden));

        sprite = sprites.get(0);
        spriteIndex = 0;
        isGoldenGet = false;
    }

    public void logicUpdate()
    {
        // Parent Check
        Pipe parent = getParent();

        if (parent != null)
        {
            if (spriteIndex != parent.spriteIndex)
            {
                spriteIndex = parent.spriteIndex;
                sprite = sprites.get(spriteIndex);
            }
        }

        // Move Check
        if (parent == null && velocity.y != 0)
        {
            if (position.y < range[0] || position.y > range[1])
            {
                velocity.y = -velocity.y;
            }
        }
    }

    public void flip()
    {
        sprites.replaceAll(bitmap -> Sprite.flipSprite(bitmap, 1));
        sprite = sprites.get(spriteIndex);
    }

    public void boundYRange(float lower, float upper)
    {
        range[0] = lower;
        range[1] = upper;
    }

    public void randomizeY()
    {
        // Follow Parent
        if (getParent() != null)
        {
            return;
        }

        float randomValue = random.nextFloat();
        float range = this.range[1] - this.range[0];

        position.y = this.range[0] + randomValue * range;
    }

    public void randomizeToggleMove()
    {
        // Follow Parent
        if (getParent() != null)
        {
            return;
        }

        float compareIndex = random.nextFloat();

        if (compareIndex < MOVE_PIPE_CHANCE)
        {
            toggleMovePipe(true);
        }
        else
        {
            toggleMovePipe(false);
        }
    }

    public void toggleMovePipe(boolean isMove)
    {
        // Follow Parent
        if (getParent() != null)
        {
            return;
        }

        if (isMove)
        {
            spriteIndex = 1;
            int randomUpDown = random.nextInt(2);
            float randomSpeedRate = (float) (Math.random() * 2.5f + 10f * (Settings.Speed - 0.09f));

            // Down
            if (randomUpDown == 0)
            {
                velocity.y = Settings.Speed / randomSpeedRate;
            }

            // Up
            else
            {
                velocity.y = -Settings.Speed / randomSpeedRate;
            }
        }
        else
        {
            spriteIndex = 0;
            velocity.y = 0;
        }

        sprite = sprites.get(spriteIndex);
    }

    public void toggleGoldPipe(boolean isGolden)
    {
        // Follow Parent
        if (getParent() != null)
        {
            return;
        }

        if (isGolden)
        {
            spriteIndex = 2;
            sprite = sprites.get(spriteIndex);
        }
        else
        {
            randomizeToggleMove();
        }
    }

    public void toggleIsGoldenGet(boolean isGoldenGet)
    {
        Pipe parent = getParent();

        // Follow Parent
        if (parent != null)
        {
            parent.toggleIsGoldenGet(isGoldenGet);
        }

        this.isGoldenGet = isGoldenGet;
    }

    public boolean checkIsGoldenGet()
    {
        Pipe parent = getParent();

        // Follow Parent
        if (parent != null)
        {
            return parent.checkIsGoldenGet();
        }

        return isGoldenGet;
    }
}
