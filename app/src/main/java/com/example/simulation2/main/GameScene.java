package com.example.simulation2.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

import com.example.simulation2.R;
import com.example.simulation2.engine.GameConstants;
import com.example.simulation2.engine.GameEvent;
import com.example.simulation2.engine.GameView;
import com.example.simulation2.structs.Sprite;
import com.example.simulation2.structs.Vector2;
import com.example.simulation2.objects.BackGround;
import com.example.simulation2.objects.BaseGround;
import com.example.simulation2.objects.Bird;
import com.example.simulation2.objects.Coin;
import com.example.simulation2.objects.Heart;
import com.example.simulation2.objects.Pipe;
import com.example.simulation2.objects.Timer;

import java.util.ArrayList;
import java.util.Random;

public class GameScene extends GameView
{
    // States
    public int score;
    private Random random;
    private boolean isGameOver;
    private boolean canControl;
    private boolean isDebug;
    private long startTime;
    private long coinRainTime;
    private long lastFrameShowTime;
    private int coinCount;
    private int timeCount;

    // Scene Game Objects
    private Bird bird;
    private Coin coin;
    private Heart heart;
    private Timer timer;
    private Timer frameRateShower;
    private final ArrayList<BackGround> backGrounds = new ArrayList<BackGround>();
    private final ArrayList<BaseGround> baseGrounds = new ArrayList<BaseGround>();
    private final ArrayList<Pipe> pipes = new ArrayList<Pipe>();
    private final Coin[] coinRain = new Coin[10];

    // Scene Game Events
    private final ArrayList<GameEvent> gameEvents = new ArrayList<GameEvent>();

    public GameScene(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        random = new Random();
        isGameOver = false;
        canControl = false;
        isDebug = false;
        coinCount = 0;
        timeCount = 0;
    }

    @Override
    protected void onInitialization()
    {
        ///////////////////////// Objects /////////////////////////
        // Bird
        bird = new Bird(Vector2.Zero, Vector2.multiply(Vector2.One, 1.3f));
        bird.setPosition(new Vector2(-bird.getRawRect().width(), GameConstants.SCREEN_HEIGHT * 0.3f));

        // BackGround
        BackGround backGround1 = new BackGround(Vector2.Zero, Vector2.One);
        backGround1.setSize(new Vector2((float) GameConstants.SCREEN_WIDTH / backGround1.getRect().width(), (float) GameConstants.SCREEN_HEIGHT / backGround1.getRect().height()));

        BackGround backGround2 = new BackGround(Vector2.Zero, Vector2.One);
        backGround2.setSize(new Vector2((float) GameConstants.SCREEN_WIDTH / backGround2.getRect().width(), (float) GameConstants.SCREEN_HEIGHT / backGround2.getRect().height()));
        backGround2.setPosition(new Vector2(GameConstants.SCREEN_WIDTH, 0));

        backGrounds.add(backGround1);
        backGrounds.add(backGround2);

        // BaseGround
        BaseGround baseGround1 = new BaseGround(Vector2.Zero, Vector2.One);
        baseGround1.setSize(new Vector2((float) GameConstants.SCREEN_WIDTH / baseGround1.getRect().width(), 0.9f));
        baseGround1.setPosition(new Vector2(0, GameConstants.SCREEN_HEIGHT - baseGround1.getRect().height()));

        BaseGround baseGround2 = new BaseGround(Vector2.Zero, Vector2.One);
        baseGround2.setSize(new Vector2((float) GameConstants.SCREEN_WIDTH / baseGround2.getRect().width(), 0.9f));
        baseGround2.setPosition(new Vector2(baseGround2.getRect().width(), GameConstants.SCREEN_HEIGHT - baseGround2.getRect().height()));

        baseGrounds.add(baseGround1);
        baseGrounds.add(baseGround2);

        // Pipes
        Pipe pipe1U = new Pipe(Vector2.Zero, Vector2.multiply(Vector2.One, 1.3f));
        pipe1U.setPosition(new Vector2(GameConstants.SCREEN_WIDTH + 1 * Pipe.INTERVAL, 0));
        pipe1U.boundYRange(Math.max(GameConstants.SCREEN_HEIGHT - baseGround1.getRect().height() - 2 * pipe1U.getRect().height() - Pipe.GAP, (float) GameConstants.SCREEN_HEIGHT / 10 - pipe1U.getRect().height()), Math.min(0, (float) GameConstants.SCREEN_HEIGHT * 9 / 10 - baseGround1.getRect().height() - Pipe.GAP - pipe1U.getRect().height()));
        pipe1U.randomizeY();
        Pipe pipe1D = new Pipe(Vector2.multiply(Vector2.Up, pipe1U.getRect().height() + Pipe.GAP), Vector2.One);
        pipe1D.flip();
        pipe1D.setParent(pipe1U);

        Pipe pipe2U = new Pipe(Vector2.Zero, Vector2.multiply(Vector2.One, 1.3f));
        pipe2U.setPosition(new Vector2(GameConstants.SCREEN_WIDTH + 2 * Pipe.INTERVAL, 0));
        pipe2U.boundYRange(Math.max(GameConstants.SCREEN_HEIGHT - baseGround1.getRect().height() - 2 * pipe1U.getRect().height() - Pipe.GAP, (float) GameConstants.SCREEN_HEIGHT / 10 - pipe1U.getRect().height()), Math.min(0, (float) GameConstants.SCREEN_HEIGHT * 9 / 10 - baseGround1.getRect().height() - Pipe.GAP - pipe1U.getRect().height()));
        pipe2U.randomizeY();
        Pipe pipe2D = new Pipe(Vector2.multiply(Vector2.Up, pipe2U.getRect().height() + Pipe.GAP), Vector2.One);
        pipe2D.flip();
        pipe2D.setParent(pipe2U);

        Pipe pipe3U = new Pipe(Vector2.Zero, Vector2.multiply(Vector2.One, 1.3f));
        pipe3U.setPosition(new Vector2(GameConstants.SCREEN_WIDTH + 3 * Pipe.INTERVAL, 0));
        pipe3U.boundYRange(Math.max(GameConstants.SCREEN_HEIGHT - baseGround1.getRect().height() - 2 * pipe1U.getRect().height() - Pipe.GAP, (float) GameConstants.SCREEN_HEIGHT / 10 - pipe1U.getRect().height()), Math.min(0, (float) GameConstants.SCREEN_HEIGHT * 9 / 10 - baseGround1.getRect().height() - Pipe.GAP - pipe1U.getRect().height()));
        pipe3U.randomizeY();
        Pipe pipe3D = new Pipe(Vector2.multiply(Vector2.Up, pipe3U.getRect().height() + Pipe.GAP), Vector2.One);
        pipe3D.flip();
        pipe3D.setParent(pipe3U);

        pipes.add(pipe1U);
        pipes.add(pipe1D);
        pipes.add(pipe2U);
        pipes.add(pipe2D);
        pipes.add(pipe3U);
        pipes.add(pipe3D);

        // Coin
        int ranIndex = random.nextInt(3) * 2;
        coin = new Coin(Vector2.Zero, Vector2.multiply(Vector2.One, 1.1f));
        coin.setPosition(new Vector2(pipes.get(ranIndex).getPosition().x + (float) pipe1U.getRect().width() / 2 + Pipe.INTERVAL / 2 - (float) coin.getRect().width() / 2, 0));
        coin.boundYRange((float) GameConstants.SCREEN_HEIGHT / 10, GameConstants.SCREEN_HEIGHT - baseGround1.getRect().height() - (float) GameConstants.SCREEN_HEIGHT / 10);
        coin.randomizeY();

        // Coin Rain
        for (int i = 0; i < coinRain.length; i ++)
        {
            Coin coinElement = new Coin(Vector2.Zero, Vector2.multiply(Vector2.One, 1.1f));
            coinElement.setPosition(new Vector2((float) (i + 1) * GameConstants.SCREEN_WIDTH / (coinRain.length + 1), (float) -GameConstants.SCREEN_HEIGHT / 20));
            coinRain[i] = coinElement;
        }

        // Heart
        heart = new Heart(Vector2.Zero, Vector2.multiply(Vector2.One, 1.1f));
        heart.setPosition(new Vector2((float) (GameConstants.SCREEN_WIDTH - heart.getRect().width()) / 2, GameConstants.SCREEN_HEIGHT - (float) (baseGround1.getRect().height() + heart.getRect().height()) / 2 + 25));
        heart.setSpriteColor(Color.TRANSPARENT);

        // Timer and FrameRateShower
        timer = new Timer(Vector2.Zero, Vector2.multiply(Vector2.One, 1.5f));
        timer.CanGlow = true;
        timer.setAlignTopCenter(new Vector2((float) GameConstants.SCREEN_WIDTH / 2, GameConstants.SCREEN_HEIGHT * 0.1f));

        frameRateShower = new Timer(Vector2.Zero, Vector2.multiply(Vector2.One, 0.8f));
        frameRateShower.setDebugGreen();
        frameRateShower.setAlignTopRight(new Vector2(GameConstants.SCREEN_WIDTH, 0));

        // Load Object Speed
        reloadSpeed();

        ///////////////////////// Events /////////////////////////
        GameEvent crazyPipes = new GameEvent()
        {
            @Override
            public void onInit()
            {
                setEventSlogan(Sprite.loadSprite(R.drawable.event_crazy_pipes), Vector2.multiply(Vector2.One, 0.3f));
            }

            @Override
            public void onCalled()
            {

            }

            @Override
            public void onHappen()
            {
                for (int i = 0; i < pipes.size() / 2; i++)
                {
                    if (pipes.get(2 * i).spriteIndex != 1)
                    {
                        pipes.get(2 * i).toggleMovePipe(true);
                    }
                }
            }

            @Override
            public void onAfter()
            {
                for (int i = 0; i < pipes.size() / 2; i++)
                {
                    pipes.get(2 * i).toggleMovePipe(false);
                }

            }
        };

        GameEvent moreGravity = new GameEvent()
        {
            @Override
            public void onInit()
            {
                setEventSlogan(Sprite.loadSprite(R.drawable.event_more_gravity), Vector2.multiply(Vector2.One, 0.3f));
            }

            @Override
            public void onCalled()
            {
                temporaryStorage = GameConstants.GRAVITY_SCALE;
                GameConstants.GRAVITY_SCALE *= 1.6f;
            }

            @Override
            public void onHappen()
            {

            }

            @Override
            public void onAfter()
            {
                GameConstants.GRAVITY_SCALE = (float) temporaryStorage;
            }
        };

        GameEvent goldRush = new GameEvent()
        {
            @Override
            public void onInit()
            {
                setEventSlogan(Sprite.loadSprite(R.drawable.event_gold_rush), Vector2.multiply(Vector2.One, 0.3f));
            }

            @Override
            public void onCalled()
            {

            }

            @Override
            public void onHappen()
            {
                for (int i = 0; i < pipes.size() / 2; i++)
                {
                    if (pipes.get(2 * i).spriteIndex != 2)
                    {
                        pipes.get(2 * i).toggleGoldPipe(true);
                    }
                }
            }

            @Override
            public void onAfter()
            {
                for (int i = 0; i < pipes.size() / 2; i++)
                {
                    pipes.get(2 * i).toggleGoldPipe(false);
                    pipes.get(2 * i).toggleIsGoldenGet(false);
                }
            }
        };

        gameEvents.add(crazyPipes);
        gameEvents.add(moreGravity);
        gameEvents.add(goldRush);
    }

    @Override
    protected void onRegistration()
    {
        ///////////////////////// Objects /////////////////////////
        // Bird
        registerObject(bird, 1);

        // BackGrounds
        for (BackGround backGround : backGrounds)
        {
            registerObject(backGround, 5);
        }

        // BaseGrounds
        for (BaseGround baseGround : baseGrounds)
        {
            registerObject(baseGround, 3);
        }

        // Pipes
        for (Pipe pipe : pipes)
        {
            registerObject(pipe, 4);
        }

        // Coin
        registerObject(coin, 2);

        // Coin Rain
        for (Coin coinElement : coinRain)
        {
            registerObject(coinElement, 2);
        }

        // Heart
        registerObject(heart, 0);

        // Timer and FrameRateShower
        registerObject(timer, 0);
        registerObject(frameRateShower, 0);

        ///////////////////////// Events /////////////////////////
        for (GameEvent gameEvent : gameEvents)
        {
            registerEvent(gameEvent);
        }
    }

    @Override
    public void renderUpdate()
    {
        bird.applyAnimation(16);
        coin.applyAnimation(24);

        // Coin Rain
        for (Coin coinElement : coinRain)
        {
            if (!coinElement.isKinematic())
            {
                coinElement.applyAnimation(24);
            }
        }
    }

    @Override
    public void logicUpdate()
    {
        // Bird
        if (!canControl && bird.getPosition().x >= (float) (100 * GameConstants.SCREEN_WIDTH) / 1080)
        {
            bird.setVelocity(new Vector2(0, bird.getVelocity().y));
            bird.setPosition(new Vector2((float) (100 * GameConstants.SCREEN_WIDTH) / 1080, bird.getPosition().y));
            heart.setHeartHealth(bird.Health);
            timer.timerUpdate(0);
            startTime = System.currentTimeMillis();

            canControl = true;
        }
        else if (!canControl && bird.getPosition().y > GameConstants.SCREEN_HEIGHT * 0.45f)
        {
            playSound(R.raw.flap_sound);
            bird.setVelocity(new Vector2(bird.getVelocity().x, 1.75f));
        }

        // BackGrounds
        for (BackGround backGround : backGrounds)
        {
            backGround.themeCheck();
        }

        // Pipes
        for (Pipe pipe : pipes)
        {
            pipe.logicUpdate();
        }

        // Timer
        int elapsedTime = (int)(System.currentTimeMillis() - startTime) / 1000;
        float elapsedFrameTime = (float)(System.currentTimeMillis() - lastFrameShowTime) / 1000.0f;
        float elapsedCoinRainTime = (float) (System.currentTimeMillis() - coinRainTime) / 1000.0f;

        if (canControl && elapsedTime >= 1)
        {
            timeCount += (int) ((System.currentTimeMillis() - startTime) / 1000);
            startTime = System.currentTimeMillis();
            lastFrameShowTime = System.currentTimeMillis();
            timer.timerUpdate(timeCount);
        }

        //Frame Rate Shower
        if (isDebug && GameConstants.DELTA_TIME != 0 && elapsedFrameTime >= 0.5f)
        {
            frameRateShower.timerUpdate((int) (1000 / GameConstants.DELTA_TIME));
            lastFrameShowTime = System.currentTimeMillis();
        }

        // Coin Rain
        if (pipes.get(0).spriteIndex == 2 && GameConstants.DELTA_TIME != 0 && elapsedCoinRainTime >= 0.25f)
        {
            int randomCoinIndex = random.nextInt(coinRain.length);
            Coin coinGet = coinRain[randomCoinIndex];

            if (coinGet.isKinematic())
            {
                coinGet.setKinematic(false);
            }

            coinRainTime = System.currentTimeMillis();
        }

        for (Coin coinElement : coinRain)
        {
            if (!coinElement.isKinematic() && coinElement.getPosition().y > GameConstants.SCREEN_HEIGHT + coinElement.getRect().height())
            {
                coinElement.setKinematic(true);
                coinElement.setVelocity(Vector2.Zero);
                coinElement.setPosition(new Vector2(coinElement.getPosition().x, (float) -GameConstants.SCREEN_HEIGHT / 20));
            }
        }

        // Events
        if (!GameEvent.isEventHappening && timeCount != 0 && timeCount % Settings.EventRate == 0)
        {
            int randEventIndex = random.nextInt(gameEvents.size());
            playSound(R.raw.swoosh_sound);
            gameEvents.get(randEventIndex).start();
        }

        // State Checks
        cycleCheck();

        if (!bird.IsDead)
        {
            checkCollision();
        }
        else
        {
            // GameOver Event
            if (!isGameOver)
            {
                onGameOver();
            }

            // Stop GameScene Threads
            if (bird.getPosition().y > GameConstants.SCREEN_HEIGHT)
            {
                freeze();
                MainActivity mainActivity = (MainActivity) getContext();
                mainActivity.onGameOverHandler();
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // If dead, cannot control, is game over, get rid of this touch action
        if (!canControl || isGameOver || bird == null || bird.IsDead)
        {
            return true;
        }

        // Handle Touch Screen
        switch (event.getActionMasked())
        {
            case MotionEvent.ACTION_DOWN:
                playSound(R.raw.flap_sound);
                bird.flap();
                break;

            default:
                break;
        }

        return true;
    }

    @Override
    public void start()
    {
        reloadSpeed();
        super.start();
    }

    public void reset()
    {
        ///////////////////////// Objects /////////////////////////
        // Bird
        bird.setPosition(new Vector2(-bird.getRawRect().width(), GameConstants.SCREEN_HEIGHT * 0.3f));
        bird.Health = Bird.FULL_HEALTH;
        bird.IsDead = false;

        // BackGround
        backGrounds.get(0).setPosition(Vector2.Zero);
        backGrounds.get(1).setPosition(new Vector2(GameConstants.SCREEN_WIDTH, 0));

        // BaseGround
        baseGrounds.get(0).setPosition(new Vector2(0, GameConstants.SCREEN_HEIGHT - baseGrounds.get(0).getRect().height()));
        baseGrounds.get(1).setPosition(new Vector2(baseGrounds.get(1).getRect().width(), GameConstants.SCREEN_HEIGHT - baseGrounds.get(1).getRect().height()));

        // Pipes
        for (int i = 0; i < pipes.size() / 2; i++)
        {
            pipes.get(2 * i).setPosition(new Vector2(GameConstants.SCREEN_WIDTH + (i + 1) * Pipe.INTERVAL, 0));
            pipes.get(2 * i).randomizeY();
            pipes.get(2 * i).toggleMovePipe(false);
            pipes.get(2 * i).toggleIsGoldenGet(false);
            pipes.get(2 * i + 1).logicUpdate();
        }

        // Coin
        int ranIndex = random.nextInt(3) * 2;
        coin.setPosition(new Vector2(pipes.get(ranIndex).getPosition().x + (float) pipes.get(0).getRect().width() / 2 + Pipe.INTERVAL / 2 - (float) coin.getRect().width() / 2, 0));
        coin.randomizeY();

        // Coin Rain
        for (Coin coinElement : coinRain)
        {
            coinElement.setKinematic(true);
            coinElement.setVelocity(Vector2.Zero);
            coinElement.setPosition(new Vector2(coinElement.getPosition().x, (float) -GameConstants.SCREEN_HEIGHT / 20));
        }

        // Heart
        heart.setHeartHealth(Bird.FULL_HEALTH);
        heart.setSpriteColor(Color.TRANSPARENT);

        // Timer and FrameRateShower
        timer.setSpriteColor(Color.TRANSPARENT);
        frameRateShower.setSpriteColor(Color.TRANSPARENT);

        ///////////////////////// Events /////////////////////////
        for (GameEvent gameEvent : gameEvents)
        {
            gameEvent.stop();
        }

        ///////////////////////// States /////////////////////////
        isGameOver = false;
        canControl = false;
        coinCount = 0;
        timeCount = 0;
        reloadSpeed();
        invalidate();
    }

    public void restart()
    {
        if (isGameOver)
        {
            reset();
        }

        score = 0;
        reloadSpeed();
        unfreeze();
    }

    public void toggleDebugFrame()
    {
        isDebug = !isDebug;
    }

    private void cycleCheck()
    {
        // BackGround
        for (BackGround backGround : backGrounds)
        {
            if (backGround.getPosition().x <= -GameConstants.SCREEN_WIDTH)
            {
                float offset = backGround.getPosition().x + GameConstants.SCREEN_WIDTH;
                backGround.setPosition(new Vector2(GameConstants.SCREEN_WIDTH + offset, backGround.getPosition().y));
            }
        }

        // BaseGround
        for (BaseGround baseGround : baseGrounds)
        {
            if (baseGround.getPosition().x <= -GameConstants.SCREEN_WIDTH)
            {
                float offset = baseGround.getPosition().x + GameConstants.SCREEN_WIDTH;
                baseGround.setPosition(new Vector2(GameConstants.SCREEN_WIDTH + offset, baseGround.getPosition().y));
            }
        }


        // Pipes
        float pipeX = 0;
        for (int i = 0; i < pipes.size() / 2; i++)
        {
            float offset = pipes.get(2 * i).getPosition().x + pipes.get(2 * i).getRect().width();

            if (offset < 0)
            {
                pipes.get(2 * i).setPosition(new Vector2(3 * Pipe.INTERVAL - pipes.get(2 * i).getRect().width() + offset, 0f));
                pipes.get(2 * i).randomizeY();
                pipes.get(2 * i).randomizeToggleMove();
                pipes.get(2 * i).toggleIsGoldenGet(false);
            }

            pipeX = Math.max(pipes.get(2 * i).getPosition().x, pipeX);
        }

        // Coin
        if (coin.getPosition().x < -coin.getRect().width())
        {
            float ranRange = random.nextInt(2);
            coin.setPosition(new Vector2(pipeX + ranRange * Pipe.INTERVAL - (Pipe.INTERVAL - pipes.get(0).getRect().width()) / 2 - (float) coin.getRect().width() / 2, 0));
            coin.randomizeY();
        }
    }

    private void checkCollision()
    {
        // Check Bird and Ground
        for (BaseGround baseGround : baseGrounds)
        {
            if (bird.collisionCheck(baseGround))
            {
                if (!bird.IsDamaging)
                {
                    playSound(R.raw.damage_sound);
                    bird.onDamage();
                    heart.setHeartHealth(bird.Health);
                }

                bird.setVelocity(new Vector2(bird.getVelocity().x, 3.8f));
            }
        }

        // Check Bird and Pipes
        for (Pipe pipe : pipes)
        {
            if (bird.collisionCheck(pipe))
            {
                // Golden Pipe Case
                if (pipe.spriteIndex == 2)
                {
                    if (!pipe.checkIsGoldenGet())
                    {
                        pipe.toggleIsGoldenGet(true);
                        playSound(R.raw.gold_pipe_sound);
                        coinCount += 5;

                        bird.Health ++;
                        if (bird.Health > Bird.FULL_HEALTH)
                        {
                            bird.Health = Bird.FULL_HEALTH;
                        }

                        heart.setHeartHealth(bird.Health);
                    }
                }
                else if (!bird.IsDamaging)
                {
                    playSound(R.raw.damage_sound);
                    bird.onDamage();
                    heart.setHeartHealth(bird.Health);
                }
            }
        }

        // Check Bird and Coin
        if (bird.collisionCheck(coin))
        {
            playSound(R.raw.coin_sound);
            coin.setPosition(new Vector2(-coin.getRect().width(), 0));
            coinCount++;

            // Two Coins Increase Health Once
            if (coinCount % 2 == 0)
            {
                bird.Health++;
                if (bird.Health > Bird.FULL_HEALTH)
                {
                    bird.Health = Bird.FULL_HEALTH;
                }

                heart.setHeartHealth(bird.Health);
            }
        }

        // Check Bird and Coin Rain
        for (Coin coinElement : coinRain)
        {
            if (!coinElement.isKinematic() && bird.collisionCheck(coinElement))
            {
                playSound(R.raw.coin_sound);
                coinElement.setPosition(new Vector2(coinElement.getPosition().x, GameConstants.SCREEN_HEIGHT + coinElement.getRect().height()));
                coinCount++;

                // Two Coins Increase Health Once
                if (coinCount % 2 == 0)
                {
                    bird.Health++;
                    if (bird.Health > Bird.FULL_HEALTH)
                    {
                        bird.Health = Bird.FULL_HEALTH;
                    }

                    heart.setHeartHealth(bird.Health);
                }
            }
        }
    }

    private void reloadSpeed()
    {
        // Bird
        bird.setVelocity(Vector2.multiply(Vector2.Right, Settings.Speed / 5));

        // BackGround
        backGrounds.get(0).setVelocity(Vector2.multiply(Vector2.Left, Settings.Speed / 12));
        backGrounds.get(1).setVelocity(Vector2.multiply(Vector2.Left, Settings.Speed / 12));

        // BaseGround
        baseGrounds.get(0).setVelocity(Vector2.multiply(Vector2.Left, Settings.Speed));
        baseGrounds.get(1).setVelocity(Vector2.multiply(Vector2.Left, Settings.Speed));

        // Pipes
        for (int i = 0; i < pipes.size() / 2; i++)
        {
            pipes.get(2 * i).setVelocity(Vector2.multiply(Vector2.Left, Settings.Speed));
            pipes.get(2 * i + 1).logicUpdate();
        }

        // Coin
        coin.setVelocity(Vector2.multiply(Vector2.Left, Settings.Speed));
    }

    private void onGameOver()
    {
        playSound(R.raw.die_sound);
        score = 5 * coinCount + 10 * timeCount;
        isGameOver = true;

        // Stop Scene Move
        for (BackGround backGround : backGrounds)
        {
            backGround.setVelocity(Vector2.Zero);
        }

        for (BaseGround baseGround : baseGrounds)
        {
            baseGround.setVelocity(Vector2.Zero);
        }

        for (Pipe pipe : pipes)
        {
            pipe.setVelocity(Vector2.Zero);
        }

        coin.setVelocity(Vector2.Zero);

        // Start Bird Horizontal Move
        bird.setVelocity(new Vector2(Settings.Speed, bird.getVelocity().y));
    }
}
