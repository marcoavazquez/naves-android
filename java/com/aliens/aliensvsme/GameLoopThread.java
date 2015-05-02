package com.aliens.aliensvsme;
import android.graphics.Canvas;


public class GameLoopThread extends Thread {

    static final long FPS = 20;
    private GameView view;
    private boolean running = false;

    public GameLoopThread(GameView view){
        this.view = view;
    }

    public void setRunning(boolean run){
        this.running = run;
    }

    public boolean getRunning() {
        return this.running;
    }

    @Override
    public void run(){

        long ticksPS = 1000 / FPS;
        long startTime;
        long sleepTime;

        while(running){

            Canvas c = null;
            startTime = System.currentTimeMillis();

            try{
                c = view.getHolder().lockCanvas();
                synchronized (view.getHolder()){

                    /* synchronized evita que otros Threads dibujen mientras se llama a onDraw */

                    view.onDraw(c);
                }
            } finally {
                if (c != null) {
                    view.getHolder().unlockCanvasAndPost(c);

                    /* finally asegura que unlockCanvasAndPost se ejecute si es posible
                     * aunque haya habido un error */
                }
            }
            sleepTime = ticksPS - (System.currentTimeMillis() - startTime);

            try {
                if (sleepTime > 0)
                    sleep(sleepTime);
                else
                    sleep(10);
            }catch (Exception e) { }
        }
    }
}
