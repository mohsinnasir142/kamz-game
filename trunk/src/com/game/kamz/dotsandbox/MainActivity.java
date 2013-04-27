package com.game.kamz.dotsandbox;

import com.game.kamz.dotsandbox.model.PlayerType;

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
import com.game.kamz.dotsandbox.R;

/**
 * This activity appears when you start the app. Here is Selected who are the
 * two players.
 */
public class MainActivity extends Activity implements OnClickListener {

	/**
	 * If an application stores settings, then you must store in a
	 * "shared preferences" map under a certain hits. In this case, we save the
	 * game settings.
	 */
	public static final String GAME_SETTINGS_KEY = "game_settings";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.startseite);

		Button playButton = (Button) findViewById(R.id.spielen);
		playButton.setOnClickListener(this);

		/* retrieve the settings to view. */
		SharedPreferences settings = getSharedPreferences(GAME_SETTINGS_KEY,
				MODE_PRIVATE);
		((Spinner) findViewById(R.id.spieler_typ_1_spinner))
				.setSelection(settings.getInt("spielerTyp1", 0));
		((Spinner) findViewById(R.id.spieler_typ_2_spinner))
				.setSelection(settings.getInt("spielerTyp2", 2));
		((Spinner) findViewById(R.id.feld_groesse_x)).setSelection(settings
				.getInt("feldGroesseX", 3));
		((Spinner) findViewById(R.id.feld_groesse_y)).setSelection(settings
				.getInt("feldGroesseY", 3));
	}

	/**
	 * * This method must be overridden when a menu is displayed. The app uses
	 * this to offer a Quit menu.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.hauptmenue, menu);
		return true;
	}

	/**
	 * What in the menu bar option-elected additional charges, this method is
	 * called.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.app_beenden)
			finish();

		return super.onOptionsItemSelected(item);
	}

	public void onClick(View v) {

		PlayerType playerTyp1 = PlayerType
				.parse((String) ((Spinner) findViewById(R.id.spieler_typ_1_spinner))
						.getSelectedItem());
		PlayerType playerTyp2 = PlayerType
				.parse((String) ((Spinner) findViewById(R.id.spieler_typ_2_spinner))
						.getSelectedItem());

		int feldGroesseX = Integer
				.parseInt((String) ((Spinner) findViewById(R.id.feld_groesse_x))
						.getSelectedItem());
		int feldGroesseY = Integer
				.parseInt((String) ((Spinner) findViewById(R.id.feld_groesse_y))
						.getSelectedItem());

		/* store values in settings using shared preferences */
		SharedPreferences settings = getSharedPreferences(GAME_SETTINGS_KEY,
				MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("spielerTyp1",
				((Spinner) findViewById(R.id.spieler_typ_1_spinner))
						.getSelectedItemPosition());
		editor.putInt("spielerTyp2",
				((Spinner) findViewById(R.id.spieler_typ_2_spinner))
						.getSelectedItemPosition());
		editor.putInt("feldGroesseX",
				((Spinner) findViewById(R.id.feld_groesse_x))
						.getSelectedItemPosition());
		editor.putInt("feldGroesseY",
				((Spinner) findViewById(R.id.feld_groesse_y))
						.getSelectedItemPosition());
		editor.commit();

		/*
		 * put data into intenet.Actually data is send by using intent and
		 * bundles
		 */
		Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra("spielerTyp1", playerTyp1);
		intent.putExtra("spielerTyp2", playerTyp2);
		intent.putExtra("feldGroesseX", feldGroesseX);
		intent.putExtra("feldGroesseY", feldGroesseY);

		startActivity(intent);
	}

}
