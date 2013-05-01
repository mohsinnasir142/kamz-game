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

		setContentView(R.layout.mainmenu);

		Button playButton = (Button) findViewById(R.id.play);
		playButton.setOnClickListener(this);

		/* retrieve the settings to view. */
		SharedPreferences settings = getSharedPreferences(GAME_SETTINGS_KEY,
				MODE_PRIVATE);
		((Spinner) findViewById(R.id.player_type_1_spinner))
				.setSelection(settings.getInt("playerType1", 0));
		((Spinner) findViewById(R.id.player_type_2_spinner))
				.setSelection(settings.getInt("playerType2", 2));
		((Spinner) findViewById(R.id.field_size_x)).setSelection(settings
				.getInt("fieldSizeX", 3));
		((Spinner) findViewById(R.id.field_size_y)).setSelection(settings
				.getInt("fieldSizeY", 3));
	}

	/**
	 * * This method must be overridden when a menu is displayed. The app uses
	 * this to offer a Quit menu.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.mainmenu, menu);
		return true;
	}

	/**
	 * What in the menu bar option-elected additional charges, this method is
	 * called.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.app_quit)
			finish();

		return super.onOptionsItemSelected(item);
	}

	public void onClick(View v) {

		PlayerType playerType1 = PlayerType
				.parse((String) ((Spinner) findViewById(R.id.player_type_1_spinner))
						.getSelectedItem());
		PlayerType playerTyp2 = PlayerType
				.parse((String) ((Spinner) findViewById(R.id.player_type_2_spinner))
						.getSelectedItem());

		int fieldSizeX = Integer
				.parseInt((String) ((Spinner) findViewById(R.id.field_size_x))
						.getSelectedItem());
		int fieldSizeY = Integer
				.parseInt((String) ((Spinner) findViewById(R.id.field_size_y))
						.getSelectedItem());

		/* store values in settings using shared preferences */
		SharedPreferences settings = getSharedPreferences(GAME_SETTINGS_KEY,
				MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("playerType1",
				((Spinner) findViewById(R.id.player_type_1_spinner))
						.getSelectedItemPosition());
		editor.putInt("playerType2",
				((Spinner) findViewById(R.id.player_type_2_spinner))
						.getSelectedItemPosition());
		editor.putInt("fieldSizeX", ((Spinner) findViewById(R.id.field_size_x))
				.getSelectedItemPosition());
		editor.putInt("fieldSizeY", ((Spinner) findViewById(R.id.field_size_y))
				.getSelectedItemPosition());
		editor.commit();

		/*
		 * put data into intenet.Actually data is send by using intent and
		 * bundles
		 */
		Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra("playerType1", playerType1);
		intent.putExtra("playerType2", playerTyp2);
		intent.putExtra("fieldSizeX", fieldSizeX);
		intent.putExtra("fieldSizeY", fieldSizeY);

		startActivity(intent);
	}

}
