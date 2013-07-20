package com.game.kamz.dotsandbox;

import com.game.kamz.dotsandbox.model.Box;
import com.game.kamz.dotsandbox.model.Line;
import com.game.kamz.dotsandbox.model.Player;
import com.game.kamz.dotsandbox.model.PlayerField;
import com.game.kamz.dotsandbox.model.PlayerManager;
import com.game.kamz.dotsandbox.model.PlayerType;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.game.kamz.dotsandbox.R;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

/**
 * The main activity that manages the game and controls the Gameloop.
 */
public class GameActivity extends Activity implements OnClickListener {

	private PlayerFieldView playingFieldView;
	private PlayerField playingField;
	private PlayerManager playingManager;
	int Player1Winner = 0;
	int Player2Winner = 0;
	public static final String GAME_SETTINGS_KEY = "game_settings";
	SharedPreferences settings;
	SharedPreferences playerPref;
	public final String filename1 = "player1File";

	TextView player1Score, player2Score;

	SharedPreferences player2Pref;
	public final String filename2 = "player2File";

	private final Handler mHandler = new Handler();

	/**
	 * This variable controls the game loop thread .
	 */
	private volatile boolean running = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.playingscreen);
		Button mybtn = (Button) findViewById(R.id.game_summary);
		mybtn.setOnClickListener(this);
		AdView ad = (AdView) findViewById(R.id.adViews);
		ad.loadAd(new AdRequest());
		Bundle intentExtras = getIntent().getExtras();

		PlayerType playerType1 = (PlayerType) intentExtras.get("playerType1");
		PlayerType playerType2 = (PlayerType) intentExtras.get("playerType2");

		int fieldSizeX = intentExtras.getInt("fieldSizeX");
		int fieldSizeY = intentExtras.getInt("fieldSizeY");
		playingField = PlayerField.generate(fieldSizeX, fieldSizeY);
		playingManager = new PlayerManager();

		playingFieldView = (PlayerFieldView) findViewById(R.id.playerfieldview);
		playingFieldView.init(playingField);

		settings = getSharedPreferences(GAME_SETTINGS_KEY, MODE_PRIVATE);

		playingManager.addPlayers(new Player(settings.getString("playerType1",
				"Player 1"), BitmapFactory.decodeResource(getResources(),
				R.drawable.player_symol_tick), getResources().getColor(
				R.color.player_1_color), playerType1));

		playingManager.addPlayers(new Player(settings.getString("playerType2",
				"Player 2"), BitmapFactory.decodeResource(getResources(),
				R.drawable.player_symol_cross), getResources().getColor(
				R.color.player_2_color), playerType2));

		// setting the player names on playing screen

		TextView player1Name = (TextView) findViewById(R.id.player_1_name);
		player1Name.setText(settings.getString("playerType1", "Player 1"));

		TextView player2Name = (TextView) findViewById(R.id.player_2_name);
		player2Name.setText(settings.getString("playerType2", "Player 2"));

		player1Score = (TextView) findViewById(R.id.player_1_score);
		player2Score = (TextView) findViewById(R.id.player_2_score);

		startGameLoop();
	}

	@Override
	protected void onStop() {
		running = true;
		super.onStop();
	}

	public void startGameLoop() {
		Thread thread = new Thread(new GameLoopRunnable());
		thread.start();
		running = true;
	}

	private class GameLoopRunnable implements Runnable {

		public void run() {

			/* The first player selection */
			playingManager.selectNextPlayer();

			while (!isGameOver()) {

				final Player player = playingManager.getCurrentPlayer();

				/*
				 * Display which player is it and how many points it has
				 * already.
				 */
				mHandler.post(new Runnable() {
					public void run() {

						ImageView imageView = (ImageView) findViewById(R.id.currentPlayrIcon);
						imageView.setImageBitmap(player.getSymbol());

						TextView currentPlayerName = (TextView) findViewById(R.id.currentplayername);
						currentPlayerName.setText(player.getName() + "'s Turn");

						// TextView textView = (TextView)
						// findViewById(R.id.displayScore);
						// textView.setText(String.valueOf(investigatingScore(player)));

						if (player.getName().equals(
								settings.getString("playerType1", "Player 1"))) {

							player1Score.setText(String
									.valueOf(investigatingScore(player)));
						}

						else if (player.getName().equals(
								settings.getString("playerType2", "Player 2"))) {

							player2Score.setText(String
									.valueOf(investigatingScore(player)));
						}

					}
				});

				Line line = null;

				if (!player.isComputerOpponent()) {

					playingFieldView.resetLastInput();

					/*
					 * The user now has its input term. Gameloop this thread is
					 * now waiting. The wait () / notify () from Java Technology
					 * Edition Note: is used here. As long as no new entry was
					 * get account , sleeps this thread now.
					 */
					while ((line = playingFieldView.getLastInput()) == null) {
						try {
							synchronized (playingFieldView) {
								playingFieldView.wait();
							}
						} catch (InterruptedException ignore) {
							/*
							 * This case can be ignored. If the thread pl wake
							 * up again , it is provided that no input is Geta
							 * account directly put to sleep again surrounded by
							 * the while loop.
							 */
						}
					}

				}

				// else {
				//
				// try { /* The user should see the input of the computer. */
				// Thread.sleep(500);
				// } catch (InterruptedException ignore) {
				// }
				//
				// // line = trainComputerOpponents(player.getPlayerType());
				// }

				selectLine(line);

				/*
				 * The activity was completed, then stop this thread. Without
				 * this line, the activity would never end and the thread
				 * continues to run until android kills it. But we of course do
				 * not want to stand out negatively.
				 */
				if (!running)
					return;
			}

			/*
			 * When all the boxex are filled, the game is over and the
			 * "Game Score" will be displayed.
			 */
			if (isGameOver()) {

				mHandler.post(new Runnable() {

					public void run() {

						final Player getwinner = investigatingWinner();

						/* FIXME DISPLAY TROPHY IMAGE */

						int pokalBildID = 0;

						// (getwinner.getName().equals(
						// settings.getString(
						// "playerType1",
						// "Player 1")))
						if (getwinner.getName().equals(
								settings.getString("playerType1", "Player 1"))) {

							pokalBildID = R.drawable.cup_tick;
						}

						else if (getwinner.getName().equals("draw")) {

							pokalBildID = R.drawable.draw;
						}

						else

							pokalBildID = R.drawable.cup_cross;

						AlertDialog alertDialog = new AlertDialog.Builder(
								GameActivity.this)
								.setTitle(
										getResources().getText(
												R.string.game_score))
								.setIcon(
										getResources().getDrawable(pokalBildID))
								.setMessage(getGameOverDialogMessage())
								.setCancelable(false)
								.setPositiveButton(
										getResources().getText(
												R.string.play_again),
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												// ______________________________start
												// Play again
												// event_________________________________//
												// settings.getString("playerType1","Player 1")
												if (getwinner.getName().equals(
														settings.getString(
																"playerType1",
																"Player 1"))) {
													// first get the preferences
													// and set the value in
													// player1Winner
													playerPref = getSharedPreferences(
															filename1, 0);

													Player1Winner = playerPref
															.getInt("player1",
																	0);
													// increment the value of
													// player1Winner and save
													// this value in shared
													// preferences
													Player1Winner++;

													SharedPreferences.Editor player1Editor = playerPref
															.edit();
													player1Editor.putInt(
															"player1",
															Player1Winner);
													player1Editor.commit();

													startActivity(getIntent());

												} else if (getwinner
														.getName()
														.equals(settings
																.getString(
																		"playerType2",
																		"Player 2"))) {

													// first get the preferences
													// and set the value in
													// player1Winner
													player2Pref = getSharedPreferences(
															filename2, 0);

													Player2Winner = player2Pref
															.getInt("player2",
																	0);
													// increment the value of
													// player1Winner and save
													// this value in shared
													// preferences
													Player2Winner++;

													SharedPreferences.Editor player2Editor = player2Pref
															.edit();
													player2Editor.putInt(
															"player2",
															Player2Winner);
													player2Editor.commit();
													startActivity(getIntent());

												}

												else {

													// game is draw do nothing

													startActivity(getIntent());

												}

												// ______________________________end
												// Play again
												// event_________________________________//

											}
										})
								.setNegativeButton(
										getResources().getText(
												R.string.main_menu),
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												dialog.dismiss();
												Intent intent = new Intent(
														getApplicationContext(),
														MainActivity.class);
												startActivity(intent);

												playerPref = getSharedPreferences(
														filename1, 0);
												SharedPreferences.Editor player1Editor = playerPref
														.edit();
												player1Editor.putInt("player1",
														0);
												player1Editor.commit();

												player2Pref = getSharedPreferences(
														filename2, 0);
												SharedPreferences.Editor player2Editor = player2Pref
														.edit();
												player2Editor.putInt("player2",
														0);
												player2Editor.commit();
											}
										}).create();
						MediaPlayer md = MediaPlayer.create(
								getApplicationContext(), R.raw.notify);
						md.start();
						alertDialog.show();
					}
				});
			}
		}

	}

	private String getGameOverDialogMessage() {

		Player getwinner = investigatingWinner();

		StringBuilder sb = new StringBuilder();

		sb.append(getResources().getString(R.string.winner) + ": "
				+ getwinner.getName() + "\n\n");

		for (Player player : playingManager.getPlayer())
			sb.append(player.getName() + ":\t\t" + investigatingScore(player)
					+ "\n");

		return sb.toString();
	}

	// private Line trainComputerOpponents(PlayerType playertype) {
	//
	// Line line = lastOpenLineForBox();
	//
	// if (line != null)
	// return line;
	//
	// Line randomLine = SelectRandomLine();
	//
	// /*
	// * The easy AI just any line, the average AI fits at least, that is no
	// * bar weight, the Complete boxes show a train at the opponent k
	// *and this might thus gives a point.
	// */
	// if (playertype == PlayerType.COMPUTER_MEDIUM) {
	//
	// int loopCounter = 0;
	//
	// while (randomLine.isSurroundingBoxClose()) {
	//
	// randomLine = SelectRandomLine();
	//
	// /*
	// * This will be attempted up to 30 times. Could then still not
	// * be found, there are either no more or the opponent may
	// * sometimes have luck.
	// */
	// if (++loopCounter >= 30)
	// break;
	// }
	// }
	//
	// return randomLine;
	// }

	// // Last hand select open line for box
	// private Line lastOpenLineForBox() {
	//
	// for (Box box : playingField.getOpenBoxList())
	// if (box.getUnselectedLinesList().size() == 1)
	// return box.getUnselectedLinesList().get(0);
	//
	// return null;
	// }

	// private Line SelectRandomLine() {
	//
	// List<Line> StrokesWithoutOwners = new ArrayList<Line>(
	// playingField.getStrokesWithoutOwners());
	// Line randomLine = StrokesWithoutOwners.get(new Random()
	// .nextInt(StrokesWithoutOwners.size()));
	//
	// return randomLine;
	// }

	private void selectLine(Line line) {

		if (line.getOwner() != null)
			return;

		Player currentPlayer = playingManager.getCurrentPlayer();

		boolean isBoxBecomeComplete = playingField.optForLine(line,
				currentPlayer);

		/*
		 * * If a cheese could be closed stchen, the player is to it again.
		 * Could he Close none of the other player's turn again:
		 */
		if (!isBoxBecomeComplete)
			playingManager.selectNextPlayer();

		playingFieldView.updateDisplay();
	}

	public boolean isGameOver() {
		return playingField.isAllOwnerHaveBoxes();
	}

	public Player investigatingWinner() {

		Player winner = null;
		int maxScore = 0;

		for (Player player : playingManager.getPlayer()) {

			int score = investigatingScore(player);

			if (score > maxScore) {
				winner = player;
				maxScore = score;
			}

			// if the game is draw
			else if (score == maxScore) {
				PlayerType playerType1 = PlayerType.parse("Human");
				// playingManager.addPlayers(();
				winner = new Player("draw", BitmapFactory.decodeResource(
						getResources(), R.drawable.player_symol_tick),
						getResources().getColor(R.color.player_1_color),
						playerType1);

				// winner = player;

			}
		}

		return winner;
	}

	public int investigatingScore(Player player) {

		int dots = 0;

		for (Box box : playingField.getListBox())
			if (box.getOwner() == player)
				dots++;

		return dots;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.game_summary:
			Intent openSummary = new Intent(getApplicationContext(),
					GameSummary.class);
			startActivity(openSummary);
			PlayerFieldView.check = true;
			break;
		}

	}

}