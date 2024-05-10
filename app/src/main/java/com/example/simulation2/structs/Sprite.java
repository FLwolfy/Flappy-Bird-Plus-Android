package com.example.simulation2.structs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;

import com.example.simulation2.engine.GameConstants;

public class Sprite
{
    /**
     * Load the sprite from the resource drawable.
     *
     * @param drawableID The drawable ID of the sprite in the resource file.
     * @return The Bitmap of the loaded sprite.
     */
    public static Bitmap loadSprite(int drawableID)
    {
        return BitmapFactory.decodeResource(GameConstants.CONTEXT.getResources(), drawableID);
    }

    /**
     * Rotates the copy of the bitmap and returns the rotated bitmap.
     *
     * @param sprite The bitmap to rotate
     * @param degree The rotation angle in degrees, clockwise positive and counterclockwise negative
     * @return The rotated bitmap
     */
    public static Bitmap rotateSprite(Bitmap sprite, float degree)
    {
        Matrix rotation = new Matrix();
        rotation.postRotate(degree);
        return Bitmap.createBitmap(sprite, 0, 0, sprite.getWidth(), sprite.getHeight(), rotation, true);
    }

    /**
     * Flip the sprite horizontally or vertically.
     *
     * @param sprite      The bitmap to flip
     * @param orientation 0 for horizontal flip, 1 for vertical flip
     * @return The flipped bitmap
     */
    public static Bitmap flipSprite(Bitmap sprite, int orientation)
    {
        Matrix matrix = new Matrix();
        if (orientation == 0)
        {
            matrix.postScale(-1, 1, sprite.getWidth() / 2f, sprite.getHeight() / 2f);
        }
        else if (orientation == 1)
        {
            matrix.postScale(1, -1, sprite.getWidth() / 2f, sprite.getHeight() / 2f);
        }
        else
        {
            return sprite;
        }

        return Bitmap.createBitmap(sprite, 0, 0, sprite.getWidth(), sprite.getHeight(), matrix, true);
    }

    /**
     * Colorizes a sprite bitmap by blending it with the specified color.
     * If the color is Color.WHITE, returns the original sprite without any color blending.
     *
     * @param sprite The original sprite bitmap to be colorized.
     * @param color  The color to blend with the sprite. It should be in ARGB format.
     * @return A new bitmap with the colorized sprite, or the original sprite if Color.WHITE is passed.
     */
    public static Bitmap colorSprite(Bitmap sprite, int color)
    {
        int width = sprite.getWidth();
        int height = sprite.getHeight();
        Bitmap coloredBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        int alpha = Color.alpha(color);

        // Transparent Case
        if (alpha == 0)
        {
            return Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8);
        }

        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                int pixelColor = sprite.getPixel(x, y);

                int originalAlpha = Color.alpha(pixelColor);
                int originalRed = Color.red(pixelColor);
                int originalGreen = Color.green(pixelColor);
                int originalBlue = Color.blue(pixelColor);

                int newAlpha = originalAlpha * alpha / 255;
                int blendedRed = originalRed * red * newAlpha / 255 / 255;
                int blendedGreen = originalGreen * green * newAlpha / 255 / 255;
                int blendedBlue = originalBlue * blue * newAlpha / 255 / 255;

                coloredBitmap.setPixel(x, y, Color.argb(newAlpha, blendedRed, blendedGreen, blendedBlue));
            }
        }

        return coloredBitmap;
    }
}
