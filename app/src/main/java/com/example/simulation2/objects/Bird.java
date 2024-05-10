package com.example.simulation2.objects;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.example.simulation2.R;
import com.example.simulation2.engine.GameObject;
import com.example.simulation2.main.Settings;
import com.example.simulation2.structs.Sprite;
import com.example.simulation2.structs.Vector2;

import java.util.ArrayList;

public class Bird extends GameObject
{
    // States
    public static final int FULL_HEALTH = 10;
    public int Health;
    public boolean IsDamaging;
    public boolean IsDead;

    // Physics
    private static final float FLAP_FORCE = 2.0f;

    // Render
    private static final int SPRITE_SIZE = 4;
    private static final int DAMAGE_BLINK_TIMES = 6;
    private final ArrayList<Bitmap> sprites = new ArrayList<Bitmap>();
    private final ArrayList<Bitmap> hurtSprites = new ArrayList<Bitmap>();
    private long lastAnimationTime;
    private int currentSpriteIndex;
    private int damageBlinkTimesLeft;

    /**
     * Constructs a new GameObject instance with the given position and size.
     *
     * @param position The top-left position of the game object
     * @param size     The size of the game object
     */
    public Bird(Vector2 position, Vector2 size)
    {
        super(position, size);

        // Initialize all sprites
        sprites.add(Sprite.loadSprite(R.drawable.bird_blue_downflap));
        sprites.add(Sprite.loadSprite(R.drawable.bird_blue_midflap));
        sprites.add(Sprite.loadSprite(R.drawable.bird_blue_upflap));
        sprites.add(Sprite.loadSprite(R.drawable.bird_blue_midflap));
        sprites.add(Sprite.loadSprite(R.drawable.bird_red_downflap));
        sprites.add(Sprite.loadSprite(R.drawable.bird_red_midflap));
        sprites.add(Sprite.loadSprite(R.drawable.bird_red_upflap));
        sprites.add(Sprite.loadSprite(R.drawable.bird_red_midflap));
        sprites.add(Sprite.loadSprite(R.drawable.bird_yellow_downflap));
        sprites.add(Sprite.loadSprite(R.drawable.bird_yellow_midflap));
        sprites.add(Sprite.loadSprite(R.drawable.bird_yellow_upflap));
        sprites.add(Sprite.loadSprite(R.drawable.bird_yellow_midflap));

        hurtSprites.add(Sprite.loadSprite(R.drawable.bird_blue_midflap_hurt));
        hurtSprites.add(Sprite.loadSprite(R.drawable.bird_red_midflap_hurt));
        hurtSprites.add(Sprite.loadSprite(R.drawable.bird_yellow_midflap_hurt));

        currentSpriteIndex = 0;
        sprite = sprites.get(0);
        spriteOffset = new Vector2(-18, -10);

        // Initialize States
        Health = FULL_HEALTH;
        IsDamaging = false;
        IsDead = false;
        setKinematic(false);
    }

    public void applyAnimation(long frameRate)
    {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastAnimationTime;

        if (elapsedTime >= 1000 / frameRate)
        {
            // Change and Rotate Sprite
            currentSpriteIndex = (currentSpriteIndex + 1) % SPRITE_SIZE + Settings.BirdAvatar * SPRITE_SIZE;
            if (damageBlinkTimesLeft > DAMAGE_BLINK_TIMES)
            {
                setSprite(hurtSprites.get(Settings.BirdAvatar), velocity.y * -15);
            }
            else
            {
                setSprite(sprites.get(currentSpriteIndex), velocity.y * -15);
            }

            // Damage Blinking Effect
            if (damageBlinkTimesLeft > 0 && (int) (lastAnimationTime / (1000 / frameRate)) % 2 == 0)
            {
                if (damageBlinkTimesLeft <= DAMAGE_BLINK_TIMES)
                {
                    setSpriteColor(Color.TRANSPARENT);
                }

                damageBlinkTimesLeft --;
            }
            else if (IsDamaging && damageBlinkTimesLeft <= 0)
            {
                IsDamaging = false;
            }

            lastAnimationTime = currentTime;
        }
    }

    public void flap()
    {
        if (position.y > 0)
        {
            velocity.y = FLAP_FORCE;
        }
    }

    public void onDamage()
    {
        Health--;

        if (Health <= 0)
        {
            IsDamaging = false;
            IsDead = true;
            return;
        }

        damageBlinkTimesLeft = DAMAGE_BLINK_TIMES + 2;
        IsDamaging = true;
    }

}
