package com.example.simulation2.engine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.simulation2.R;
import com.example.simulation2.structs.Vector2;

public abstract class GameEvent
{
    /**
     * This is the event duration time. (milliseconds)
     */
    public static final long EVENT_DURATION = 8000;

    /**
     * This is the event slogan moving speed.
     */
    public static final float SLOGAN_SPEED = 2.0f;

    /**
     * Only one Event happens at a moment.
     */
    public static boolean isEventHappening;

    /**
     * This is used for temporary storage of some constants.
     */
    public Object temporaryStorage;
    private boolean isHappening;
    private boolean isAnimationComplete;
    private long startTime;
    private long trueDuration;
    private Bitmap eventSlogan;
    private Vector2 sloganSize = Vector2.Zero.copy();
    private float currentY;

    public GameEvent()
    {
        eventSlogan = Bitmap.createBitmap(GameConstants.SCREEN_WIDTH / 2, GameConstants.SCREEN_HEIGHT / 2, Bitmap.Config.ARGB_8888);
        currentY = (float) -GameConstants.SCREEN_HEIGHT / 20;
        isEventHappening = false;
        isHappening = false;
        isAnimationComplete = true;
    }

    /**
     * Start the event.
     */
    public void start()
    {
        if (!isEventHappening)
        {
            onCalled();
            isEventHappening = true;
            isHappening = true;
            isAnimationComplete = false;
            startTime = System.currentTimeMillis();
            trueDuration = (long) (EVENT_DURATION - GameConstants.SCREEN_HEIGHT / 10 / SLOGAN_SPEED);
        }
    }

    /**
     * Force to stop the event.
     */
    public void stop()
    {
        currentY = (float) -GameConstants.SCREEN_HEIGHT / 20;
        isEventHappening = false;
        isHappening = false;

        if (!isAnimationComplete)
        {
            onAfter();
            isAnimationComplete = true;
        }
    }

    /**
     * The function will be called for initialization (on registration).
     */
    public abstract void onInit();

    /**
     * The function will be called when event is called.
     */
    public abstract void onCalled();

    /**
     * The function will be called when the event happens.
     */
    public abstract void onHappen();

    /**
     * The function will be called when the event is over.
     */
    public abstract void onAfter();

    /**
     * Set the event slogan with the given sprite.
     * @param sprite The given sprite
     */
    protected void setEventSlogan(Bitmap sprite, Vector2 size)
    {
        eventSlogan = sprite;
        sloganSize = size.copy();
    }

    /**
     * Handle draw of the slogan of this event onto the given canvas.
     *
     * @param canvas The canvas this object will be drawn onto
     */
    private void draw(Canvas canvas)
    {
        // Draw
        canvas.drawBitmap(eventSlogan, null, new Rect((int) (GameConstants.SCREEN_WIDTH - eventSlogan.getWidth() * sloganSize.x) / 2, (int) (currentY - eventSlogan.getHeight() * sloganSize.y / 2), (int) (GameConstants.SCREEN_WIDTH + eventSlogan.getWidth() * sloganSize.x) / 2, (int) (currentY + eventSlogan.getHeight() * sloganSize.y / 2)), null);
    }

    /**
     * Update the physics check of the game event
     */
    private void physics()
    {
        // Position Change
        updateSloganY();

        // Check
        checkDuration();
    }

    private void updateSloganY()
    {
        if (isHappening && currentY < (float) GameConstants.SCREEN_HEIGHT / 20)
        {
            currentY += (SLOGAN_SPEED * GameConstants.FIXED_DELTA_TIME);
            currentY = Math.min(currentY, (float) GameConstants.SCREEN_HEIGHT / 20);
        }
        else if (!isHappening && currentY > (float) -GameConstants.SCREEN_HEIGHT / 20)
        {
            currentY -= (SLOGAN_SPEED * GameConstants.FIXED_DELTA_TIME);
            currentY = Math.max(currentY, (float) -GameConstants.SCREEN_HEIGHT / 20);
        }
        else if (!isHappening && !isAnimationComplete)
        {
            isAnimationComplete = true;
            isEventHappening = false;
            onAfter();
        }
    }

    private void checkDuration()
    {
        if (!isAnimationComplete)
        {
            if (isHappening && System.currentTimeMillis() - startTime > trueDuration)
            {
                isHappening = false;
            }

            onHappen();
        }
    }
}
