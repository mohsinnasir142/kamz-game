package com.game.kamz.dotsandbox;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.game.kamz.dotsandbox.model.Box;
import com.game.kamz.dotsandbox.model.Line;
import com.game.kamz.dotsandbox.model.Player;
import com.game.kamz.dotsandbox.model.PlayerField;
import com.game.kamz.dotsandbox.model.PlayerManager;
import com.game.kamz.dotsandbox.model.PlayerType;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;
import com.game.kamz.dotsandbox.R;

/**
 * The main activity that manages the game and controls the Gameloop.
 */
public class GameActivity extends Activity {

	private PlayerFieldView playingFieldView;
	private PlayerField playingField;
	private PlayerManager playingManager;

	private final Handler mHandler = new Handler();

	/** This variable controls the game loop thread. */
	private volatile boolean running = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		Bundle intentExtras = getIntent().getExtras();

		PlayerType spielerTyp1 = (PlayerType) intentExtras.get("spielerTyp1");
		PlayerType spielerTyp2 = (PlayerType) intentExtras.get("spielerTyp2");

		int feldGroesseX = intentExtras.getInt("feldGroesseX");
		int feldGroesseY = intentExtras.getInt("feldGroesseY");

		playingField = PlayerField.generate(feldGroesseX, feldGroesseY);
		playingManager = new PlayerManager();

		playingFieldView = (PlayerFieldView) findViewById(R.id.spielfeldView);
		playingFieldView.init(playingField);

		playingManager.addPlayers(new Player(getResources().getString(
				R.string.spieler_1_name), BitmapFactory.decodeResource(
				getResources(), R.drawable.spieler_symbol_kaese),
				getResources().getColor(R.color.player_1_color), spielerTyp1));
		playingManager.addPlayers(new Player(getResources().getString(
				R.string.spieler_2_name), BitmapFactory.decodeResource(
				getResources(), R.drawable.spieler_symbol_maus), getResources()
				.getColor(R.color.player_2_color), spielerTyp2));

		startGameLoop();
	}

	@Override
	protected void onStop() {
		running = false;
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

				final Player spieler = playingManager.getCurrentPlayer();

				/*
				 * Display which player is it and how many points it has
				 * already.
				 */
				mHandler.post(new Runnable() {
					public void run() {

						ImageView imageView = (ImageView) findViewById(R.id.aktuellerSpielerSymbol);
						imageView.setImageBitmap(spieler.getSymbol());

						TextView textView = (TextView) findViewById(R.id.punkteAnzeige);
						textView.setText(String
								.valueOf(investigatingScore(spieler)));
					}
				});

				Line line = null;

				if (!spieler.isComputerOpponent()) {

					playingFieldView.resetLastInput();

					/*
					 * The user now has its input term. Gameloop this
					 * thread is now waiting. The wait () / notify () from Java
					 * Technology Edition Note: is used here. As long as no new
					 * entry was get account  , sleeps this thread now.
					 */
					while ((line = playingFieldView.getLastInput()) == null) {
						try {
							synchronized (playingFieldView) {
								playingFieldView.wait();
							}
						} catch (InterruptedException ignore) {
							/*
							 * This case can be ignored. If the thread pl wake
							 * up again , it is provided that no input
							 * is Geta ¤ account directly put to sleep again
							 * surrounded by the while loop.
							 */
						}
					}

				}

				else {

					try { /* The user should see the input of the computer. */
						Thread.sleep(500);
					} catch (InterruptedException ignore) {
					}

					line = computerGegnerZug(spieler.getPlayerType());
				}

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

						Player gewinner = investigatingWinner();

						/* FIXME DISPLAY TROPHY IMAGE */

						int pokalBildID = 0;
						if (gewinner.getName().equals(
								getResources().getString(
										R.string.spieler_1_name)))
							pokalBildID = R.drawable.pokal_kaese;
						else
							pokalBildID = R.drawable.pokal_maus;

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
												startActivity(getIntent());
											}
										})
								.setNegativeButton(
										getResources()
												.getText(
														R.string.zurueck_zum_hauptmenue),
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												dialog.dismiss();
												GameActivity.this.finish();
											}
										}).create();

						alertDialog.show();
					}
				});
			}
		}

	}

	private String getGameOverDialogMessage() {

		Player gewinner = investigatingWinner();

		StringBuilder sb = new StringBuilder();

		sb.append(getResources().getString(R.string.gewinner) + ": "
				+ gewinner.getName() + "\n\n");

		for (Player spieler : playingManager.getSpieler())
			sb.append(spieler.getName() + ":\t\t" + investigatingScore(spieler)
					+ "\n");

		return sb.toString();
	}

	private Line computerGegnerZug(PlayerType spielerTyp) {

		Line strich = lastOpenLineForBox();

		if (strich != null)
			return strich;

		Line randomLine = SelectRandomLine();

		/*
		 * The easy AI just any line, the average AI fits at least, that is no
		 * bar weight, the Complete boxes show a cheese train at the opponent k
		 * Ã  and this might thus gives a point.
		 */
		if (spielerTyp == PlayerType.COMPUTER_MEDIUM) {

			int loopCounter = 0;

			while (randomLine.isSurroundingBoxClose()) {

				randomLine = SelectRandomLine();

				/*
				 * This will be attempted up to 30 times. Could then still not
				 * be found, there are either no more or the opponent may
				 * sometimes have luck.
				 */
				if (++loopCounter >= 30)
					break;
			}
		}

		return randomLine;
	}

	// Last hand select open line for box
	private Line lastOpenLineForBox() {

		for (Box kaestchen : playingField.getOpenBoxList())
			if (kaestchen.getUnselectedLinesList().size() == 1)
				return kaestchen.getUnselectedLinesList().get(0);

		return null;
	}

	private Line SelectRandomLine() {

		List<Line> StrokesWithoutOwners = new ArrayList<Line>(
				playingField.getStricheOhneBesitzer());
		Line randomLine = StrokesWithoutOwners.get(new Random()
				.nextInt(StrokesWithoutOwners.size()));

		return randomLine;
	}

	private void selectLine(Line strich) {

		if (strich.getOwner() != null)
			return;

		Player currentPlayer = playingManager.getCurrentPlayer();

		boolean isBoxBecomeComplete = playingField.optForLine(strich,
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

		for (Player spieler : playingManager.getSpieler()) {

			int score = investigatingScore(spieler);

			if (score > maxScore) {
				winner = spieler;
				maxScore = score;
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

}