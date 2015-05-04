package com.aliens.aliensvsme;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.app.Activity;
import android.view.WindowManager;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /* Esta es la vista principal de la aplicación, aquí se carga las Views, osea lo que se ha
        programado para que se muestre en pantalla.

        Aqui lo que se hace es cargar el archivo llamado GameView.java.
         */

        super.onCreate(savedInstanceState);

        // Oculta la barra  de arriba para que sea fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // para que solo sea en modo horizontal
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // aqui se carga el arhichivo principal del juego
        setContentView(new GameView(this));

    }
}
