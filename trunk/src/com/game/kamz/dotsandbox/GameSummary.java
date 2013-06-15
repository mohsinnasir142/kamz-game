package com.game.kamz.dotsandbox;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Menu;
import android.widget.TextView;

public class GameSummary extends Activity {

	TextView player1W, player1L, player2W, player2L, player_1_Name,
			player_2_Name;
	public static final String GAME_SETTINGS_KEY = "game_settings";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_game_summary);

		SharedPreferences settings = getSharedPreferences(GAME_SETTINGS_KEY,
				MODE_PRIVATE);

		player1W = (TextView) findViewById(R.id.player1WinTv);
		player1L = (TextView) findViewById(R.id.player1LoseTv);
		player2W = (TextView) findViewById(R.id.player2WinTv);
		player2L = (TextView) findViewById(R.id.player2LoseTv);
		player_1_Name = (TextView) findViewById(R.id.player_1_name);
		player_2_Name = (TextView) findViewById(R.id.player_2_name);

		player_1_Name.setText(settings.getString("playerType1", "Player 1"));
		player_2_Name.setText(settings.getString("playerType2", "Player 2"));
		// settings.getString("playerType1", "Player 1")

		SharedPreferences player1Pref = getSharedPreferences("player1File", 0);

		player1W.setText("" + player1Pref.getInt("player1", 0));
		player2L.setText("" + player1Pref.getInt("player1", 0));

		SharedPreferences player2Pref = getSharedPreferences("player2File", 0);
		player2W.setText("" + player2Pref.getInt("player2", 0));
		player1L.setText("" + player2Pref.getInt("player2", 0));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_game_summary, menu);
		return true;
	}

}
