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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;
import de.stefan_oltmann.kaesekaestchen.model.ShowBoxes;
import de.stefan_oltmann.kaesekaestchen.model.Player;
import de.stefan_oltmann.kaesekaestchen.model.PlayerManager;
import de.stefan_oltmann.kaesekaestchen.model.PlayerType;
import de.stefan_oltmann.kaesekaestchen.model.PlayerField;
import de.stefan_oltmann.kaesekaestchen.model.Strich;

/**
 * The main activity that manages the game and controls the Gameloop.
 */
public class GameActivity extends Activity {

    private PlayerFieldView    playingFieldView;
    private PlayerField        playingField;
    private PlayerManager   playingManager;

    private final Handler    mHandler = new Handler();

    /**  This variable controls the game loop thread. */
    private volatile boolean running  = true;

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

        playingManager.addPlayers(
                new Player(getResources().getString(R.string.spieler_1_name),
                        BitmapFactory.decodeResource(getResources(), R.drawable.spieler_symbol_kaese),
                        getResources().getColor(R.color.spieler_1_farbe), spielerTyp1));
        playingManager.addPlayers(
                new Player(getResources().getString(R.string.spieler_2_name),
                        BitmapFactory.decodeResource(getResources(), R.drawable.spieler_symbol_maus),
                        getResources().getColor(R.color.spieler_2_farbe), spielerTyp2));

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
                 *  Display which player is it and how many points it has already.
                 */
                mHandler.post(new Runnable() {
                    public void run() {

                        ImageView imageView = (ImageView) findViewById(R.id.aktuellerSpielerSymbol);
                        imageView.setImageBitmap(spieler.getSymbol());

                        TextView textView = (TextView) findViewById(R.id.punkteAnzeige);
                        textView.setText(String.valueOf(investigatingScore(spieler)));
                    }
                });

                Strich line = null;

                if (!spieler.isComputerOpponent()) {

                    playingFieldView.resetLastInput();

                    /*
                     * The user now has its input ¤ tÃ term. Gameloop this
					 * thread is now waiting. The wait () / notify () from Java
					 * Technology Edition Note: is used here. As long as no new
					 * entry was get account Ã ¤, sleeps this thread now.
                     */
                    while ((line = playingFieldView.getLastInput()) == null) {
                        try {
                            synchronized (playingFieldView) {
                                playingFieldView.wait();
                            }
                        } catch (InterruptedException ignore) {
                            /*
                             *This case can be ignored. If the thread pl wake
							 * up again tzlich Ã ¶, it is provided that no input
							 * is Geta ¤ account directly put to sleep again
							 * surrounded by the while loop.
                             */
                        }
                    }

                } 
                
                else {

                    try { /*The user should see the input of the computer.*/
                        Thread.sleep(500);
                    } catch (InterruptedException ignore) {
                    }

                    line = computerGegnerZug(spieler.getPlayerType());
                }

                selectLine(line);

                /*
                 * Wurde die Activity beendet, dann auch diesen Thread stoppen.
				 * Ohne diese Zeile wÃ¼rde die Activity niemals enden und der
				 * Thread immer weiter laufen, bis Android diesen killt. Wir
				 * wollen aber natÃ¼rlich nicht negativ auffallen.
                 */
                if (!running)
                    return;
            }

            /*
             * Wenn alle KÃ¤stchen besetzt sind, ist das Spiel vorbei und der
             * "Game Score" kann angezeigt werden.
             */
            if (isGameOver()) {

                mHandler.post(new Runnable() {

                    public void run() {

                        Player gewinner = investigatingWinner();

                        /* FIXME Hartkodierte Pokalbilder */
                        int pokalBildID = 0;
                        if (gewinner.getName().equals(getResources().getString(R.string.spieler_1_name)))
                            pokalBildID = R.drawable.pokal_kaese;
                        else
                            pokalBildID = R.drawable.pokal_maus;

                        AlertDialog alertDialog = new AlertDialog.Builder(GameActivity.this)
                                .setTitle(getResources().getText(R.string.game_score))
                                .setIcon(getResources().getDrawable(pokalBildID))
                                .setMessage(getGameOverDialogMessage())
                                .setCancelable(false)
                                .setPositiveButton(getResources().getText(R.string.play_again),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                startActivity(getIntent());
                                            }
                                        })
                                .setNegativeButton(getResources().getText(R.string.zurueck_zum_hauptmenue),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                                GameActivity.this.finish();
                                            }
                                        })
                                .create();

                        alertDialog.show();
                    }
                });
            }
        }

    }

    private String getGameOverDialogMessage() {

        Player gewinner = investigatingWinner();

        StringBuilder sb = new StringBuilder();

        sb.append(getResources().getString(R.string.gewinner) + ": " + gewinner.getName() + "\n\n");

        for (Player spieler : playingManager.getSpieler())
            sb.append(spieler.getName() + ":\t\t" + investigatingScore(spieler) + "\n");

        return sb.toString();
    }

    private Strich computerGegnerZug(PlayerType spielerTyp) {

        Strich strich = lastOpenLineForBox();

        if (strich != null)
            return strich;

        Strich randomLine = SelectRandomLine();

        /*
         * Die einfache KI wÃ¤hlt einfach irgendeinen Strich, die mittlere KI
         * passt wenigstens auf, dass kein Strich gewÃ¤hlt wird, der beim Zug des
         * Gegners ein KÃ¤stchen schlieÃŸen kÃ¶nnte und diesem damit einen Punkt
         * schenkt.
         */
        if (spielerTyp == PlayerType.COMPUTER_MEDIUM) {

            int loopCounter = 0;

            while (randomLine.isSurroundingBoxClose()) {

                randomLine = SelectRandomLine();

                /*
                 * Dies wird maximal 30 Mal versucht. Konnte dann immer noch
                 * keine gefunden werden, gibt es entweder keine mehr oder der
                 * Gegner darf auch mal GlÃ¼ck haben.
                 */
                if (++loopCounter >= 30)
                    break;
            }
        }

        return randomLine;
    }
// Last hand select open line for box
    private Strich lastOpenLineForBox() {

        for (ShowBoxes kaestchen : playingField.getOpenBoxList())
            if (kaestchen.getUnselectedLinesList().size() == 1)
                return kaestchen.getUnselectedLinesList().get(0);

        return null;
    }

    private Strich SelectRandomLine() {

        List<Strich> StrokesWithoutOwners = new ArrayList<Strich>(playingField.getStricheOhneBesitzer());
        Strich randomLine = StrokesWithoutOwners.get(new Random().nextInt(StrokesWithoutOwners.size()));

        return randomLine;
    }

    private void selectLine(Strich strich) {

        if (strich.getOwner() != null)
            return;

        Player currentPlayer = playingManager.getCurrentPlayer();

        boolean isBoxBecomeComplete = playingField.optForLine(strich, currentPlayer);

        /*
         ** If a cheese could be closed stchen, the player is to it again. Could
		 * he Close none of the other player's turn again:
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

    public int investigatingScore(Player spieler) {

        int dots = 0;

        for (ShowBoxes kaestchen : playingField.getListBox())
            if (kaestchen.getOwner() == spieler)
                dots++;

        return dots;
    }

}