package com.aliens.aliensvsme;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

public class Fondo {

    public Paint estrellas;
    public Paint estrellas2;

    public GameView gameView;
    public int x;
    public int y = 1;
    Random rnd = new Random();


    public Fondo(GameView gameView,int x, int y) {
        this.gameView = gameView;
        this.x = x;

        this.y = y;

    }

    public void onDraw(Canvas canvas) {


        estrellas     = new Paint();
        estrellas2    = new Paint();

        x -= 10;

        if (x < 0) {
            x = gameView.getWidth();
        }

        estrellas.setColor(Color.WHITE);
        estrellas.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x, y, 4, estrellas);



    }

}
