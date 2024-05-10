package com.example.simulation2.objects;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.example.simulation2.R;
import com.example.simulation2.engine.GameObject;
import com.example.simulation2.main.Settings;
import com.example.simulation2.structs.Sprite;
import com.example.simulation2.structs.Vector2;

import java.util.ArrayList;

public class Timer extends GameObject
{
    // Render
    public final int[] NUM_SIZE = new int[2];
    public boolean CanGlow;
    private int timeCount;
    private Vector2 alignTopLeft = Vector2.Zero.copy();
    private Vector2 alignTopCenter = null;
    private Vector2 alignTopRight = null;
    private final int GAP = 5;
    private final ArrayList<Bitmap> numSprites = new ArrayList<Bitmap>();

    /**
     * Constructs a new GameObject instance with the given position and size.
     *
     * @param position The top-left position of the game object
     * @param size     The size of the game object
     */
    public Timer(Vector2 position, Vector2 size)
    {
        super(position, size);

        // Initialize Sprites
        numSprites.add(Sprite.loadSprite(R.drawable.number0));
        numSprites.add(Sprite.loadSprite(R.drawable.number0_glow));
        numSprites.add(Sprite.loadSprite(R.drawable.number1));
        numSprites.add(Sprite.loadSprite(R.drawable.number1_glow));
        numSprites.add(Sprite.loadSprite(R.drawable.number2));
        numSprites.add(Sprite.loadSprite(R.drawable.number2_glow));
        numSprites.add(Sprite.loadSprite(R.drawable.number3));
        numSprites.add(Sprite.loadSprite(R.drawable.number3_glow));
        numSprites.add(Sprite.loadSprite(R.drawable.number4));
        numSprites.add(Sprite.loadSprite(R.drawable.number4_glow));
        numSprites.add(Sprite.loadSprite(R.drawable.number5));
        numSprites.add(Sprite.loadSprite(R.drawable.number5_glow));
        numSprites.add(Sprite.loadSprite(R.drawable.number6));
        numSprites.add(Sprite.loadSprite(R.drawable.number6_glow));
        numSprites.add(Sprite.loadSprite(R.drawable.number7));
        numSprites.add(Sprite.loadSprite(R.drawable.number7_glow));
        numSprites.add(Sprite.loadSprite(R.drawable.number8));
        numSprites.add(Sprite.loadSprite(R.drawable.number8_glow));
        numSprites.add(Sprite.loadSprite(R.drawable.number9));
        numSprites.add(Sprite.loadSprite(R.drawable.number9_glow));

        NUM_SIZE[0] = numSprites.get(0).getWidth();
        NUM_SIZE[1] = numSprites.get(0).getHeight();
        timeCount = 0;
        CanGlow = false;
    }

    public void setAlignTopLeft(Vector2 alignPos)
    {
        alignTopLeft = alignPos;
        alignTopCenter = null;
        alignTopRight = null;
    }

    public void setAlignTopCenter(Vector2 alignPos)
    {
        alignTopLeft = null;
        alignTopCenter = alignPos;
        alignTopRight = null;
    }

    public void setAlignTopRight(Vector2 alignPos)
    {
        alignTopLeft = null;
        alignTopCenter = null;
        alignTopRight = alignPos;
    }

    public void timerUpdate(int time)
    {
        timeCount = time;

        // Create a New Thread for calculation Image Results
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                rawTimerUpdate(time);
            }
        }).start();
    }

    public void rawTimerUpdate(int time)
    {
        int digitWidth = NUM_SIZE[0];
        int digitHeight = NUM_SIZE[1];
        int bitmapWidth = (digitWidth + GAP) * String.valueOf(time).length() - GAP;
        Bitmap timerBitmap = Bitmap.createBitmap(bitmapWidth, digitHeight, Bitmap.Config.ARGB_8888);
        int[] pixels = new int[bitmapWidth * digitHeight];
        int index = 0;

        for (char digit : String.valueOf(time).toCharArray())
        {
            int digitInt = Character.getNumericValue(digit) * 2;

            // Set glowing
            if (CanGlow && timeCount > 0 && timeCount % Settings.EventRate == 0)
            {
                digitInt++;
            }

            Bitmap digitBitmap = numSprites.get(digitInt);
            int originalDigitWidth = digitBitmap.getWidth();
            int originalDigitHeight = digitBitmap.getHeight();
            int x = (digitWidth - originalDigitWidth) / 2 + index;

            for (int i = 0; i < originalDigitHeight; i++)
            {
                for (int j = 0; j < originalDigitWidth; j++)
                {
                    int pixelX = x + j;
                    if (pixelX >= 0 && pixelX < bitmapWidth)
                    {
                        pixels[i * bitmapWidth + pixelX] = digitBitmap.getPixel(j, i);
                    }
                }
            }

            index += digitWidth + GAP;
        }

        timerBitmap.setPixels(pixels, 0, bitmapWidth, 0, 0, bitmapWidth, digitHeight);
        sprite = timerBitmap;

        // Align Position
        if (alignTopCenter != null)
        {
            position.x = alignTopCenter.x - (float) getRect().width() / 2;
            position.y = alignTopCenter.y;
        }
        else if (alignTopLeft != null)
        {
            position.x = alignTopLeft.x;
            position.y = alignTopLeft.y;
        }
        else
        {
            position.x = alignTopRight.x - getRect().width();
            position.y = alignTopRight.y;
        }
    }

    public void setDebugGreen()
    {
        numSprites.replaceAll(sprite -> Sprite.colorSprite(sprite, Color.GREEN));
    }
}
