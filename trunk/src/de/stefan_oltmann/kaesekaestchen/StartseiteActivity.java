/*
 * Kaesekaestchen
 * A simple Dots'n'Boxes Game for Android
 *
 * Copyright (C) 2011 - 2012 Stefan Oltmann
 *
 * Contact : dotsandboxes@stefan-oltmann.de
 * Homepage: http://www.stefan-oltmann.de/
 *
 * This file is part of Kaesekaestchen.
 *
 * Kaesekaestchen is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Kaesekaestchen is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Kaesekaestchen. If not, see <http://www.gnu.org/licenses/>.
 */
package de.stefan_oltmann.kaesekaestchen;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;
import de.stefan_oltmann.kaesekaestchen.model.PlayerType;

/**
 *This activity appears when you start the app. Here is Selected √ § hlt, who
 * are the two players.

 * @author Stefan Oltmann
 */
public class StartseiteActivity extends Activity implements OnClickListener {

    /**
     * Wenn eine App Einstellungen speichert, dann m√ºssen die in einer
     * "shared preferences"-Map unter einem bestimmten Schl√ºssel abgelegt
     * werden. In diesem Fall speichern wir die Spiel-Einstellungen.
     */
    public static final String GAME_SETTINGS_KEY = "game_settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.startseite);

        Button playButton = (Button) findViewById(R.id.spielen);
        playButton.setOnClickListener(this);

        /* retrieve the settings to view. */
        SharedPreferences settings = getSharedPreferences(GAME_SETTINGS_KEY, MODE_PRIVATE);
        ((Spinner) findViewById(R.id.spieler_typ_1_spinner)).setSelection(settings.getInt("spielerTyp1", 0));
        ((Spinner) findViewById(R.id.spieler_typ_2_spinner)).setSelection(settings.getInt("spielerTyp2", 2));
        ((Spinner) findViewById(R.id.feld_groesse_x)).setSelection(settings.getInt("feldGroesseX", 3));
        ((Spinner) findViewById(R.id.feld_groesse_y)).setSelection(settings.getInt("feldGroesseY", 3));
    }

    /**
     * *
	 * This method must be overridden when a menu is displayed. The app uses
	 * this to offer a Quit menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.hauptmenue, menu);
        return true;
    }

    /**
     * Was in the menu bar Option § hlt additional charges, this method is called.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.app_beenden)
            finish();

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {

        PlayerType playerTyp1 = PlayerType.parse((String) ((Spinner) findViewById(R.id.spieler_typ_1_spinner)).getSelectedItem());
        PlayerType playerTyp2 = PlayerType.parse((String) ((Spinner) findViewById(R.id.spieler_typ_2_spinner)).getSelectedItem());

        int feldGroesseX = Integer.parseInt((String) ((Spinner) findViewById(R.id.feld_groesse_x)).getSelectedItem());
        int feldGroesseY = Integer.parseInt((String) ((Spinner) findViewById(R.id.feld_groesse_y)).getSelectedItem());

        /* store values in settings using shared preferences */
        SharedPreferences settings = getSharedPreferences(GAME_SETTINGS_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("spielerTyp1", ((Spinner) findViewById(R.id.spieler_typ_1_spinner)).getSelectedItemPosition());
        editor.putInt("spielerTyp2", ((Spinner) findViewById(R.id.spieler_typ_2_spinner)).getSelectedItemPosition());
        editor.putInt("feldGroesseX", ((Spinner) findViewById(R.id.feld_groesse_x)).getSelectedItemPosition());
        editor.putInt("feldGroesseY", ((Spinner) findViewById(R.id.feld_groesse_y)).getSelectedItemPosition());
        editor.commit();

        /* put data into intenet.Actually data is send by using intent and bundles */
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("spielerTyp1", playerTyp1);
        intent.putExtra("spielerTyp2", playerTyp2);
        intent.putExtra("feldGroesseX", feldGroesseX);
        intent.putExtra("feldGroesseY", feldGroesseY);

        startActivity(intent);
    }

}
