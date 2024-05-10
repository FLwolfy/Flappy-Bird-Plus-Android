package com.example.simulation2.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.simulation2.R;
import com.example.simulation2.engine.GameConstants;
import com.example.simulation2.engine.GameObject;
import com.example.simulation2.objects.Timer;
import com.example.simulation2.structs.Vector2;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
{
    //////////////////////////////////////////////////////////////
    // Thread Element
    //////////////////////////////////////////////////////////////
    public static final int GAME_OVER_MESSAGE = 0;
    private Handler handler;

    //////////////////////////////////////////////////////////////
    // UI Element
    //////////////////////////////////////////////////////////////
    private RelativeLayout main;
    private FrameLayout titlePage;
    private LinearLayout startScene;
    private FrameLayout optionScene;
    private GameScene gameScene;
    private FrameLayout gameOverView;
    private ImageButton startButton;
    private ImageButton optionButton;

    //////////////////////////////////////////////////////////////
    // Data Element
    //////////////////////////////////////////////////////////////
    private Timer scoreShower;
    private int record;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Window Initialization
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        GameConstants.SCREEN_WIDTH = displayMetrics.widthPixels;
        GameConstants.SCREEN_HEIGHT = displayMetrics.heightPixels;
        GameConstants.CONTEXT = getApplicationContext();

        setContentView(R.layout.activity_main);

        // UI Initialization
        main = findViewById(R.id.main);
        titlePage = findViewById(R.id.title_page);
        startScene = findViewById(R.id.start_scene);
        gameOverView = findViewById(R.id.game_over);
        gameScene = findViewById(R.id.game_scene);

        startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                gameScene.playSound(R.raw.select_sound);
                onBegin();
            }
        });

        optionButton = findViewById(R.id.option_button);
        optionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                gameScene.playSound(R.raw.select_sound);
                onOption();
            }
        });
        optionButton.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                gameScene.playSound(R.raw.select_sound);
                gameScene.toggleDebugFrame();
                return true;
            }
        });

        // Options Initialization
        optionScene = findViewById(R.id.option_scene);
        optionScene.findViewById(R.id.avatar_blue).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                gameScene.playSound(R.raw.select_sound);
                onSelectAvatar(Settings.Avatar.BLUE);
                storeData();
            }
        });
        optionScene.findViewById(R.id.avatar_red).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                gameScene.playSound(R.raw.select_sound);
                onSelectAvatar(Settings.Avatar.RED);
                storeData();
            }
        });
        optionScene.findViewById(R.id.avatar_yellow).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                gameScene.playSound(R.raw.select_sound);
                onSelectAvatar(Settings.Avatar.YELLOW);
                storeData();
            }
        });
        optionScene.findViewById(R.id.speed_one).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                gameScene.playSound(R.raw.select_sound);
                onSelectSpeed(Settings.SceneSpeed.SLOW);
                storeData();
            }
        });
        optionScene.findViewById(R.id.speed_two).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                gameScene.playSound(R.raw.select_sound);
                onSelectSpeed(Settings.SceneSpeed.MEDIUM);
                storeData();
            }
        });
        optionScene.findViewById(R.id.speed_three).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                gameScene.playSound(R.raw.select_sound);
                onSelectSpeed(Settings.SceneSpeed.FAST);
                storeData();
            }
        });
        optionScene.findViewById(R.id.frequency_one).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                gameScene.playSound(R.raw.select_sound);
                onSelectFrequency(Settings.EventFrequency.LOW);
                storeData();
            }
        });
        optionScene.findViewById(R.id.frequency_two).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                gameScene.playSound(R.raw.select_sound);
                onSelectFrequency(Settings.EventFrequency.MEDIUM);
                storeData();
            }
        });
        optionScene.findViewById(R.id.frequency_three).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                gameScene.playSound(R.raw.select_sound);
                onSelectFrequency(Settings.EventFrequency.HIGH);
                storeData();
            }
        });

        // Load Data
        loadData();
        onSelectAvatar(Settings.BirdAvatar);
        onSelectSpeed(Settings.Speed);
        onSelectFrequency(Settings.EventRate);

        scoreShower = new Timer(Vector2.Zero, Vector2.One);
        scoreShower.rawTimerUpdate(record);

        ImageView recordView = gameOverView.findViewById(R.id.record);
        recordView.setImageBitmap(scoreShower.getSprite());

        // Thread Initialization
        handler = new Handler(Looper.getMainLooper())
        {
            @Override
            public void handleMessage(@NonNull Message msg)
            {
                super.handleMessage(msg);

                switch (msg.what)
                {
                    // Handle GameOver Event
                    case GAME_OVER_MESSAGE:
                        // GameOver View
                        ImageView scoreView = gameOverView.findViewById(R.id.score);

                        scoreShower.rawTimerUpdate(gameScene.score);
                        scoreView.setImageBitmap(scoreShower.getSprite());

                        if (gameScene.score > record)
                        {
                            record = gameScene.score;
                            storeData();

                            gameOverView.findViewById(R.id.new_record).setVisibility(View.VISIBLE);
                            ImageView recordView = gameOverView.findViewById(R.id.record);
                            recordView.setImageBitmap(scoreShower.getSprite());
                        }

                        // Game Over Animation
                        gameOverView.setVisibility(View.VISIBLE);
                        Animation slideDownAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
                        slideDownAnimation.setDuration(1000);
                        gameOverView.findViewById(R.id.game_over_board).startAnimation(slideDownAnimation);

                        // Restart to Title Page
                        postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                gameScene.reset();
                                titlePage.setVisibility(View.VISIBLE);
                                gameOverView.findViewById(R.id.new_record).setVisibility(View.INVISIBLE);
                                gameOverView.setVisibility(View.INVISIBLE);
                            }
                        }, 5000);
                        break;

                    default:
                        break;
                }
            }
        };
    }

    public void onGameOverHandler()
    {
        handler.sendEmptyMessage(GAME_OVER_MESSAGE);
    }

    private void onBegin()
    {
        titlePage.setVisibility(View.INVISIBLE);
        if (gameScene.IsStart)
        {
            gameScene.restart();
        }
        else
        {
            gameScene.start();
        }
    }

    private void onOption()
    {
        if (optionScene.getVisibility() == View.INVISIBLE)
        {
            optionScene.setVisibility(View.VISIBLE);
            startScene.setVisibility(View.INVISIBLE);
        }
        else if (optionScene.getVisibility() == View.VISIBLE)
        {
            startScene.setVisibility(View.VISIBLE);
            optionScene.setVisibility(View.INVISIBLE);
        }
    }

    private void onSelectAvatar(int avatar)
    {
        ImageView indicator_blue = optionScene.findViewById(R.id.avatar_blue_indicator);
        ImageView indicator_red = optionScene.findViewById(R.id.avatar_red_indicator);
        ImageView indicator_yellow = optionScene.findViewById(R.id.avatar_yellow_indicator);

        switch (avatar)
        {
            case Settings.Avatar.BLUE:
                Settings.BirdAvatar = avatar;
                indicator_blue.setVisibility(View.VISIBLE);
                indicator_red.setVisibility(View.INVISIBLE);
                indicator_yellow.setVisibility(View.INVISIBLE);
                break;

            case Settings.Avatar.RED:
                Settings.BirdAvatar = avatar;
                indicator_blue.setVisibility(View.INVISIBLE);
                indicator_red.setVisibility(View.VISIBLE);
                indicator_yellow.setVisibility(View.INVISIBLE);
                break;

            case Settings.Avatar.YELLOW:
                Settings.BirdAvatar = avatar;
                indicator_blue.setVisibility(View.INVISIBLE);
                indicator_red.setVisibility(View.INVISIBLE);
                indicator_yellow.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void onSelectSpeed(float speed)
    {
        ImageView indicator_one = optionScene.findViewById(R.id.speed_one_indicator);
        ImageView indicator_two = optionScene.findViewById(R.id.speed_two_indicator);
        ImageView indicator_three = optionScene.findViewById(R.id.speed_three_indicator);

        if (speed == Settings.SceneSpeed.SLOW)
        {
            Settings.Speed = speed;
            indicator_one.setVisibility(View.VISIBLE);
            indicator_two.setVisibility(View.INVISIBLE);
            indicator_three.setVisibility(View.INVISIBLE);
        }
        else if (speed == Settings.SceneSpeed.MEDIUM)
        {
            Settings.Speed = speed;
            indicator_one.setVisibility(View.INVISIBLE);
            indicator_two.setVisibility(View.VISIBLE);
            indicator_three.setVisibility(View.INVISIBLE);
        }
        else if (speed == Settings.SceneSpeed.FAST)
        {
            Settings.Speed = speed;
            indicator_one.setVisibility(View.INVISIBLE);
            indicator_two.setVisibility(View.INVISIBLE);
            indicator_three.setVisibility(View.VISIBLE);
        }
    }

    private void onSelectFrequency(long frequency)
    {
        ImageView indicator_one = optionScene.findViewById(R.id.frequency_one_indicator);
        ImageView indicator_two = optionScene.findViewById(R.id.frequency_two_indicator);
        ImageView indicator_three = optionScene.findViewById(R.id.frequency_three_indicator);

        if (frequency == Settings.EventFrequency.LOW)
        {
            Settings.EventRate = frequency;
            indicator_one.setVisibility(View.VISIBLE);
            indicator_two.setVisibility(View.INVISIBLE);
            indicator_three.setVisibility(View.INVISIBLE);
        }
        else if (frequency == Settings.EventFrequency.MEDIUM)
        {
            Settings.EventRate = frequency;
            indicator_one.setVisibility(View.INVISIBLE);
            indicator_two.setVisibility(View.VISIBLE);
            indicator_three.setVisibility(View.INVISIBLE);
        }
        else if (frequency == Settings.EventFrequency.HIGH)
        {
            Settings.EventRate = frequency;
            indicator_one.setVisibility(View.INVISIBLE);
            indicator_two.setVisibility(View.INVISIBLE);
            indicator_three.setVisibility(View.VISIBLE);
        }
    }

    private void storeData()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("record", record);
        editor.putInt("bird_avatar", Settings.BirdAvatar);
        editor.putFloat("speed", Settings.Speed);
        editor.putLong("event_rate", Settings.EventRate);

        editor.apply();
    }

    private void loadData()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE);

        record = sharedPreferences.getInt("record", 0);
        Settings.BirdAvatar = sharedPreferences.getInt("bird_avatar", Settings.Avatar.BLUE);
        Settings.Speed = sharedPreferences.getFloat("speed", Settings.SceneSpeed.SLOW);
        Settings.EventRate = sharedPreferences.getLong("event_rate", Settings.EventFrequency.LOW);
    }
}

