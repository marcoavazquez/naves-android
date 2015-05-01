package com.aliens.aliensvsme;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Bala {

    GameView gameView;
    public boolean levantado = false;
    public Paint bala;
    public int posInicialY = 200;

    public int x = 200;

    public Bala(GameView gameView){
        this.gameView    = gameView;
    }

    public void onDraw(Canvas canvas,int i, int y){
        bala = new Paint();
        bala.setColor(Color.BLUE);
        bala.setStyle(Paint.Style.FILL);

        x += 25;

        if (x >= gameView.getWidth() && !levantado ) {

            x = 200;
            posInicialY = y;
        }
        if( levantado) {
            x = 200;
            posInicialY = y;
        }

        canvas.drawCircle(x-20, posInicialY, 6, bala);

    }

    public int get_x(){
        return this.x;
    }

    public int get_posInicialY() {
        return this.posInicialY;
    }

    public void set_levantado(Boolean bol) {
        this.levantado = bol;
    }

}
