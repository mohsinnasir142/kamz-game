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
import android.widget.EditText;
import android.widget.Spinner;
import com.game.kamz.dotsandbox.R;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

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
	public final String filename="player1File";
	public final String filename2="player2File";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.mainmenu);
		AdView ad=(AdView)findViewById(R.id.adView);
		ad.loadAd(new AdRequest());
		
		//empty the sharepreferences of Summary
		
		SharedPreferences playerPref = getSharedPreferences(filename, 0);
		SharedPreferences.Editor player1Editor=playerPref.edit();
		player1Editor.putInt("player1", 0);
		player1Editor.commit();
		
		SharedPreferences player2Pref = getSharedPreferences(filename2, 0);
		SharedPreferences.Editor player2Editor=player2Pref.edit();
		player2Editor.putInt("player2", 0);
		player2Editor.commit();
		
		
		

		Button playButton = (Button) findViewById(R.id.play);
		Button aboutButton = (Button) findViewById(R.id.about);
		
		playButton.setOnClickListener(this);
		aboutButton.setOnClickListener(this);

		/* retrieve the settings to view. */
		SharedPreferences settings = getSharedPreferences(GAME_SETTINGS_KEY,
				MODE_PRIVATE);
		((EditText) findViewById(R.id.player_1_editText)).setText(settings
				.getString("playerType1", "Player 1"));

		((EditText) findViewById(R.id.player_2_editText)).setText(settings
				.getString("playerType2", "Player 2"));
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

		switch (v.getId()) {

		case R.id.play:

			PlayerType playerType1 = PlayerType.parse("Human");
			PlayerType playerType2 = PlayerType.parse("Human");

			int fieldSizeX = Integer
					.parseInt((String) ((Spinner) findViewById(R.id.field_size_x))
							.getSelectedItem());
			int fieldSizeY = Integer
					.parseInt((String) ((Spinner) findViewById(R.id.field_size_y))
							.getSelectedItem());

			/* store values in settings using shared preferences */
			SharedPreferences settings = getSharedPreferences(
					GAME_SETTINGS_KEY, MODE_PRIVATE);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("playerType1",
					((EditText) findViewById(R.id.player_1_editText)).getText()
							.toString());
			editor.putString("playerType2",
					((EditText) findViewById(R.id.player_2_editText)).getText()
							.toString());
			editor.putInt("fieldSizeX",
					((Spinner) findViewById(R.id.field_size_x))
							.getSelectedItemPosition());
			editor.putInt("fieldSizeY",
					((Spinner) findViewById(R.id.field_size_y))
							.getSelectedItemPosition());
			editor.commit();

			/*
			 * put data into intenet.Actually data is send by using intent and
			 * bundles
			 */
			Intent intent = new Intent(this, GameActivity.class);
			intent.putExtra("playerType1", playerType1);
			intent.putExtra("playerType2", playerType2);
			intent.putExtra("fieldSizeX", fieldSizeX);
			intent.putExtra("fieldSizeY", fieldSizeY);

			startActivity(intent);
			
			
			
			//empty the sharepreferences of Summary
			
			SharedPreferences playerPref = getSharedPreferences(filename, 0);
			SharedPreferences.Editor player1Editor=playerPref.edit();
			player1Editor.putInt("player1", 0);
			player1Editor.commit();
			
			SharedPreferences player2Pref = getSharedPreferences(filename2, 0);
			SharedPreferences.Editor player2Editor=player2Pref.edit();
			player2Editor.putInt("player2", 0);
			player2Editor.commit();
			

			break;

		case R.id.about:
			Intent opentAbout = new Intent(this, About.class);
			startActivity(opentAbout);
		}
	}
}
