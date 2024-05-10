package com.example.simulation2.engine;

public class GameRenderThread extends Thread
{
    private GameView gameView;
    private boolean paused;

    public GameRenderThread(GameView gameView)
    {
        this.gameView = gameView;
        this.paused = false;
    }

    public void pause()
    {
        paused = true;
    }

    public synchronized void unpause()
    {
        paused = false;
        notify();
    }

    @Override
    public void run()
    {
        long startTime;
        long elapsedTime;

        while (true)
        {
            synchronized (this)
            {
                while (paused)
                {
                    try
                    {
                        wait();
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            startTime = System.currentTimeMillis();
            ///////////////////////////////////////////////////////
            //////////////////// Render Here //////////////////////

            gameView.renderUpdate();
            gameView.postInvalidate();

            ///////////////////////////////////////////////////////
            long currentTime = System.currentTimeMillis();
            elapsedTime = currentTime - startTime;

            long sleepTime = GameConstants.DELTA_TIME - elapsedTime;
            if (sleepTime > 0)
            {
                try
                {
                    Thread.sleep(sleepTime);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
