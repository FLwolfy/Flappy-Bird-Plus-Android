package com.example.simulation2.engine;

import static com.example.simulation2.engine.GameConstants.GRAVITY_SCALE;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;

import androidx.annotation.Nullable;

import com.example.simulation2.structs.Sprite;
import com.example.simulation2.structs.Vector2;

/**
 * Represents a game object.
 */
public abstract class GameObject
{
    private GameObject parent = null;

    /**
     * The kinematic physics state of the game object
     */
    protected boolean isKinematic = true;

    /**
     * The top-left corner local position of the game object
     */
    protected Vector2 position = Vector2.Zero.copy();

    /**
     * The size of the game object
     */
    protected Vector2 size = Vector2.One.copy();

    /**
     * The local velocity of the game object
     */
    protected Vector2 velocity = Vector2.Zero.copy();

    /**
     * The width and height offset of the Rect of the sprite
     */
    protected Vector2 spriteOffset = Vector2.Zero.copy();

    /**
     * The sprite of the game object
     */
    protected Bitmap sprite;

    /**
     * Constructs a new GameObject instance with the given position and size.
     *
     * @param position The top-left position of the game object
     * @param size     The size of the game object
     */
    public GameObject(Vector2 position, Vector2 size)
    {
        this.position.duplicate(position);
        this.size.duplicate(size);

        sprite = Bitmap.createBitmap(GameConstants.SCREEN_WIDTH / 2, GameConstants.SCREEN_HEIGHT / 2, Bitmap.Config.ARGB_8888);
    }

    /**
     * Gets a copy of the local position of the game object.
     *
     * @return A copy of the position
     */
    public Vector2 getPosition()
    {
        return position.copy();
    }

    /**
     * Gets a copy of the local size of the game object.
     *
     * @return A copy of the size
     */
    public Vector2 getSize()
    {
        return size.copy();
    }

    /**
     * Gets the local velocity of the game object.
     *
     * @return The velocity
     */
    public Vector2 getVelocity()
    {
        return velocity.copy();
    }

    /**
     * Gets the rectangular area of the game object with sprite offset.
     *
     * @return The Rect representing the area of the object with sprite offset
     */
    public Rect getRect()
    {
        if (parent != null)
        {
            return new Rect((int) (parent.position.x + position.x - parent.size.x * size.x * spriteOffset.x / 2), (int) (parent.position.y + position.y - parent.size.y * size.y * spriteOffset.y / 2), (int) (parent.position.x + position.x + parent.size.x * size.x * (sprite.getWidth() + spriteOffset.x / 2)), (int) (parent.position.y + position.y + parent.size.y * size.y * (sprite.getHeight() + spriteOffset.y / 2)));
        }
        else
        {
            return new Rect((int) (position.x - size.x * spriteOffset.x / 2), (int) (position.y - size.y * spriteOffset.y / 2), (int) (position.x + size.x * (sprite.getWidth() + spriteOffset.x / 2)), (int) (position.y + size.y * (sprite.getHeight() + spriteOffset.y / 2)));
        }
    }

    /**
     * Gets the raw rectangular area of the game object sprite
     *
     * @return The raw Rect representing the area of the sprite of the game object
     */
    public Rect getRawRect()
    {
        if (parent != null)
        {
            return new Rect((int) (parent.position.x + position.x), (int) (parent.position.y + position.y), (int) (parent.position.x + position.x + parent.size.x * size.x * sprite.getWidth()), (int) (parent.position.y + position.y + parent.size.y * size.y * sprite.getHeight()));
        }
        else
        {
            return new Rect((int) position.x, (int) position.y, (int) (position.x + size.x * sprite.getWidth()), (int) (position.y + size.y * sprite.getHeight()));
        }
    }

    /**
     * Gets the copy of the sprite of the game object.
     *
     * @return The copy of the sprite of this game object
     */
    public Bitmap getSprite()
    {
        return Bitmap.createBitmap(sprite);
    }

    /**
     * Gets the parent of the game object.
     * @return Return the parent of the game object. If no parent, then return null.
     */
    public <T extends GameObject> T getParent()
    {
        if (parent != null)
        {
            return (T) parent;
        }
        else
        {
            return null;
        }
    }

    /**
     * Gets the kinematic states of the game object.
     */
    public boolean isKinematic()
    {
        return isKinematic;
    }

    /**
     * Sets the local position of the game object.
     *
     * @param position The new position
     */
    public void setPosition(Vector2 position)
    {
        this.position.duplicate(position);
    }

    /**
     * Sets the local size of the game object.
     *
     * @param size The new size
     */
    public void setSize(Vector2 size)
    {
        this.size.duplicate(size);
    }

    /**
     * Sets the local velocity of the game object.
     *
     * @param velocity The new velocity
     */
    public void setVelocity(Vector2 velocity)
    {
        this.velocity.duplicate(velocity);
    }

    /**
     * Sets the sprite of the game object.
     *
     * @param sprite The new sprite
     */
    public void setSprite(Bitmap sprite)
    {
        this.sprite = sprite;
    }

    /**
     * Sets the sprite of the game object with given degree angle.
     *
     * @param sprite The new sprite
     * @param degree Angle in clockwise
     */
    public void setSprite(Bitmap sprite, float degree)
    {
        this.sprite = Sprite.rotateSprite(sprite, degree);
    }

    /**
     * Sets the sprite offsets of the game object with given width-height offset.
     *
     * @param offset The width-height offset
     */
    public void setSpriteOffset(Vector2 offset)
    {
        spriteOffset = offset.copy();
    }

    /**
     * Sets the sprite of the game object with given color.
     */
    public void setSpriteColor(int color)
    {
        sprite = Sprite.colorSprite(sprite, color);
    }

    /**
     * Sets the parent game object of this object. This will determine the base position, velocity, and size.
     * @param parent The parent to be assigned.
     */
    public void setParent(@Nullable GameObject parent)
    {
        this.parent = parent;
    }

    /**
     * Sets the game object to be kinematic or not based on the given boolean value.
     *
     * @param isKinematic The given boolean value standing for the kinematic state.
     */
    public void setKinematic(boolean isKinematic)
    {
        this.isKinematic = isKinematic;
    }

    /**
     * Check whether the current game object and the other game object collide.
     *
     * @param other The other game object.
     * @return Return true if collides. Otherwise, false.
     */
    public boolean collisionCheck(GameObject other)
    {
        return Rect.intersects(getRect(), other.getRect());
    }

    /**
     * Draw the sprite of this game object onto the given canvas.
     *
     * @param canvas The canvas this object will be drawn onto
     */
    private void draw(Canvas canvas)
    {
        canvas.drawBitmap(sprite, null, getRawRect(), null);
    }

    /**
     * Update the physics like velocity and etc. of the game object
     */
    private void physics()
    {
        // Velocity Update
        position.x += velocity.x * GameConstants.FIXED_DELTA_TIME;
        position.y -= velocity.y * GameConstants.FIXED_DELTA_TIME;

        // Gravity Update
        if (!isKinematic)
        {
            velocity.y -= GRAVITY_SCALE * GameConstants.FIXED_DELTA_TIME;
            velocity.y = Math.max(velocity.y, -GameConstants.MAX_FALL_SPEED);
        }
    }
}
