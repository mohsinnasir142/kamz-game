package com.game.kamz.dotsandbox;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Menu;
import android.widget.TextView;

public class GameSummary extends Activity {

	TextView player1W,player1L,player2W,player2L;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_summary);
		player1W=(TextView) findViewById(R.id.player1WinTv);
		player1L=(TextView) findViewById(R.id.player1LoseTv);
		player2W=(TextView) findViewById(R.id.player2WinTv);
		player2L=(TextView) findViewById(R.id.player2LoseTv);
		
	 SharedPreferences	playerPref=getSharedPreferences("player1File", 0);

		
		player1W.setText(""+playerPref.getInt("player1", 0));
		player2L.setText(""+playerPref.getInt("player1", 0));
		
		
		SharedPreferences player2Pref=getSharedPreferences("player2File", 0);
		player2W.setText(""+player2Pref.getInt("player2", 0));
		player1L.setText(""+player2Pref.getInt("player2", 0));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_game_summary, menu);
		return true;
	}
	
	

}
