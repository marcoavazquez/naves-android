package com.aliens.aliensvsme;


import android.graphics.Canvas;
import android.graphics.Paint;


public class Fondo {

    public Paint estrellas;
    public Paint estrellas2;

    public GameView gameView;
    public int x;
    public int y = 1;


    public Fondo(GameView gameView,int x, int y) {
        this.gameView = gameView;
        this.x = x;

        this.y = y;
    }

    public void onDraw(Canvas canvas, int color, int velocidad, int tamano) {

        estrellas     = new Paint();
        estrellas2    = new Paint();

        x -= velocidad;

        if (x < 0) {
            x = gameView.getWidth();
        }

        estrellas.setColor(color);
        estrellas.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x, y, tamano, estrellas);

    }

}
