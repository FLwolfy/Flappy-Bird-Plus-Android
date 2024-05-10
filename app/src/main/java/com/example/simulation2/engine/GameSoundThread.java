package com.example.simulation2.engine;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.simulation2.R;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class GameSoundThread extends Thread
{
    private static final int MAX_SYNC_PLAYERS = 5;
    private final Context context;
    private final Map<Integer, MediaPlayer[]> idSoundPair = new HashMap<>();

    public GameSoundThread(GameView gameView)
    {
        this.context = gameView.getContext();

        // Load Raw Sound Files
        loadAllRawSound();
    }

    public synchronized void playMusic(int musicID)
    {
        if (idSoundPair.containsKey(musicID) && idSoundPair.get(musicID) != null)
        {
            MediaPlayer[] mpArray = idSoundPair.get(musicID);

            if (mpArray != null)
            {
                for (MediaPlayer mediaPlayer : mpArray)
                {
                    if (mediaPlayer != null && !mediaPlayer.isPlaying())
                    {
                        mediaPlayer.start();
                        break;
                    }
                }
            }
        }
    }

    public synchronized void stopMusic(int musicID)
    {
        if (idSoundPair.containsKey(musicID) && idSoundPair.get(musicID) != null)
        {
            MediaPlayer[] mpArray = idSoundPair.get(musicID);

            if (mpArray != null)
            {
                for (MediaPlayer mediaPlayer : mpArray)
                {
                    if (mediaPlayer != null && mediaPlayer.isPlaying())
                    {
                        mediaPlayer.stop();
                    }
                }
            }
        }
    }

    public synchronized void stopAllMusic()
    {
        for (MediaPlayer[] mpArray : idSoundPair.values())
        {
            if (mpArray != null)
            {
                for (MediaPlayer mediaPlayer : mpArray)
                {
                    if (mediaPlayer != null && mediaPlayer.isPlaying())
                    {
                        mediaPlayer.stop();
                    }
                }
            }
        }
    }

    private void loadAllRawSound()
    {
        idSoundPair.clear();

        // Get Resource IDs
        Field[] fields = R.raw.class.getFields();
        for (Field field : fields)
        {
            if (field.getName().endsWith("_sound"))
            {
                int rawResourceId;

                try
                {
                    rawResourceId = field.getInt(null);
                }
                catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                    continue;
                }

                MediaPlayer[] mpArray = new MediaPlayer[MAX_SYNC_PLAYERS];

                for (int i = 1; i < MAX_SYNC_PLAYERS; i++)
                {
                    mpArray[i] = MediaPlayer.create(context, rawResourceId);
                }

                idSoundPair.put(rawResourceId, mpArray);
            }
        }
    }
}
