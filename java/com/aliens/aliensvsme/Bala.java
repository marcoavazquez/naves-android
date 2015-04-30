package com.aliens.aliensvsme;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Bala {

    GameView gameView;
    public boolean levantado = false;
    public Paint bala;
    public int posInicialY = 200;

    public int x = 100;

    public Bala(GameView gameView){
        this.gameView    = gameView;
    }

    public void onDraw(Canvas canvas,int i, int y){
        bala = new Paint();
        bala.setColor(Color.WHITE);
        bala.setStyle(Paint.Style.FILL);

        x += 25;

        if (x >= gameView.getWidth() && !levantado ) {

            x = 100;
            posInicialY = y;
        }
        if( levantado) {
            x = 100;
        }

        canvas.drawCircle(x + (i * 10), posInicialY, 10, bala);

    }

}
