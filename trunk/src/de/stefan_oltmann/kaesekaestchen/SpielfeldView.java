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

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import de.stefan_oltmann.kaesekaestchen.model.PlayerField;
import de.stefan_oltmann.kaesekaestchen.model.ShowBoxes;
import de.stefan_oltmann.kaesekaestchen.model.Strich;

/**
 * This class features the playing field and takes user interactions
  * Counter.
 * 
 * @author Stefan Oltmann
 */
public class SpielfeldView extends View implements OnTouchListener {

<<<<<<< .mine
    public static int       BOX_PAGE_LENGHT = 50;
    public static int       PADDING                = 5;
=======
	public static int KAESTCHEN_SEITENLAENGE = 50;
	public static int PADDING = 5;
>>>>>>> .r8

<<<<<<< .mine
    private PlayerField       PlayingField;
=======
	private Spielfeld spielfeld;
>>>>>>> .r8

<<<<<<< .mine
    /**
     * About the last entry is brought to experience what the user
       made. The retrieval of this value is blocking it were in the game-flow.
     */
    private volatile Strich lastInput;
=======
	/**
	 * Ãœber die letzte Eingabe wird in Erfahrung gebracht, was der Nutzer
	 * mÃ¶chte. Der Abruf dieses Wertes ist sozusagen im Spiel-Ablauf blocking.
	 */
	private volatile Strich letzteEingabe;
>>>>>>> .r8

	public SpielfeldView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

<<<<<<< .mine
    public void init(PlayerField PlayingField) {
        this.PlayingField = PlayingField;
        setOnTouchListener(this);
    }
=======
	public void init(Spielfeld spielfeld) {
		this.spielfeld = spielfeld;
		setOnTouchListener(this);
	}
>>>>>>> .r8

<<<<<<< .mine
    public Strich getLastInput() {
        return lastInput;
    }
=======
	public Strich getLetzteEingabe() {
		return letzteEingabe;
	}
>>>>>>> .r8

<<<<<<< .mine
    public void resetLastInput() {
    	lastInput = null;
    }
=======
	public void resetLetzteEingabe() {
		letzteEingabe = null;
	}
>>>>>>> .r8

<<<<<<< .mine
    /**
     * If the screen image to delete solution fully reflect the modified or known to initial,
      * This method is called. We use this to determine how big a
      * Cheese boxes show in dependency from the measurement resolutions of the display must be.

     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
=======
	/**
	 * Wird die BildschirmauflÃ¶sung verÃ¤ndert oder initial bekanntgegen, wird
	 * diese Methode aufgerufen. Wir benutzen das um zu ermitteln, wie groÃŸ ein
	 * KÃ¤stchen in AbhÃ¤ngigkeit von der AuflÃ¶sung des Displays sein muss.
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
>>>>>>> .r8

<<<<<<< .mine
        if (PlayingField == null)
            return;
=======
		if (spielfeld == null)
			return;
>>>>>>> .r8

<<<<<<< .mine
        int maxWidth = (w - PADDING * 2) / PlayingField.getBoxInWidth();
        int maxHeight = (h - PADDING * 2) / PlayingField.getBoxInHeight();
        BOX_PAGE_LENGHT = Math.min(maxWidth, maxHeight);
    }
=======
		int maxBreite = (w - PADDING * 2) / spielfeld.getBreiteInKaestchen();
		int maxHoehe = (h - PADDING * 2) / spielfeld.getHoeheInKaestchen();
		KAESTCHEN_SEITENLAENGE = Math.min(maxBreite, maxHoehe);
	}
>>>>>>> .r8

	@Override
	protected void onDraw(Canvas canvas) {

		canvas.drawColor(getResources().getColor(R.color.hintergrund_farbe));

<<<<<<< .mine
        /** Has not yet initialized the field, not draw this.
          * Otherwise, the'd lead to a null pointer exception for. This is
          * Required account also to be represented correctly in the GUI editor.
          */
        if (PlayingField == null) {
            canvas.drawRect(0, 0, getWidth(), getHeight(), new Paint());
            return;
        }
=======
		/*
		 * Wurde das Spielfeld noch nicht initalisiert, dieses nicht zeichnen.
		 * Ansonsten wÃ¼rde das zu einer NullPointer-Exception fÃ¼hren. Dies
		 * wird auch benÃ¶tigt, um korrekt im GUI-Editor dargestellt zu werden.
		 */
		if (spielfeld == null) {
			canvas.drawRect(0, 0, getWidth(), getHeight(), new Paint());
			return;
		}
>>>>>>> .r8

<<<<<<< .mine
        for (ShowBoxes box : PlayingField.getListBox())
            box.onDraw(canvas);
    }
=======
		for (Kaestchen kaestchen : spielfeld.getKaestchenListe())
			kaestchen.onDraw(canvas);
	}
>>>>>>> .r8

	public boolean onTouch(View view, MotionEvent event) {

<<<<<<< .mine
        /*
         * There are different motion events, but here we are interested only
          * The actual fact   1/4  on the screen.
         */
        if (event.getAction() != MotionEvent.ACTION_DOWN)
            return true;
=======
		/*
		 * Es gibt verschiedene MotionEvents, aber hier interessiert uns nur das
		 * tatsÃ¤chliche DrÃ¼cken auf den Bildschirm.
		 */
		if (event.getAction() != MotionEvent.ACTION_DOWN)
			return true;
>>>>>>> .r8

<<<<<<< .mine
        if (lastInput != null)
            return true;
=======
		if (letzteEingabe != null)
			return true;
>>>>>>> .r8

<<<<<<< .mine
        int calculatedgridX = (int) event.getX() / BOX_PAGE_LENGHT;
        int calculatedgridY = (int) event.getY() / BOX_PAGE_LENGHT;
=======
		int errechnetRasterX = (int) event.getX() / KAESTCHEN_SEITENLAENGE;
		int errechnetRasterY = (int) event.getY() / KAESTCHEN_SEITENLAENGE;
>>>>>>> .r8

<<<<<<< .mine
        ShowBoxes kaestchen = PlayingField.getBox(calculatedgridX, calculatedgridY);
=======
		Kaestchen kaestchen = spielfeld.getKaestchen(errechnetRasterX,
				errechnetRasterY);
>>>>>>> .r8

<<<<<<< .mine
        /*
         * If any of the celebrity hardships position is no cheese or boxes show
          * This already has an owner, ignore the entry.
         */
        if (kaestchen == null || kaestchen.getOwner() != null)
            return true;
=======
		/*
		 * Wenn sich an der berÃ¼hrten Position kein KÃ¤stchen befindet oder
		 * dieses schon einen Besitzer hat, die Eingabe ignorieren.
		 */
		if (kaestchen == null || kaestchen.getBesitzer() != null)
			return true;
>>>>>>> .r8

<<<<<<< .mine
        Strich strich = kaestchen.determineLine((int) event.getX(), (int) event.getY());
=======
		Strich strich = kaestchen.ermittleStrich((int) event.getX(),
				(int) event.getY());
>>>>>>> .r8

<<<<<<< .mine
        /*
         * Was no bar to be determined, the user has probably
          * The center of the cheese stchens taken. Anyway, it is not clear
          * What line he meant. Therefore, the command is aborted.
         */
        if (strich == null)
            return true;
=======
		/*
		 * Konnte kein Strich ermittelt werden, hat der Benutzer wahrscheinlich
		 * die Mitte des KÃ¤stchens getroffen. Es ist jedenfalls nicht klar,
		 * welchen Strich er gemeint hat. Deshalb wird die Eingabe abgebrochen.
		 */
		if (strich == null)
			return true;
>>>>>>> .r8

<<<<<<< .mine
        /*
         * At this point the user has entered his successful get a ¤ account.
          * We write its input into an intermediate variable for the
          * Communication is used with the thread and wake Gameloop
          * This again via "notifyAll". The Gameloop thread was previously
          * With "wait ()" and this class as a semaphore "paused".
         */
        lastInput = strich;
=======
		/*
		 * An dieser Stelle hat der Benutzer seine Eingabe erfolgreich
		 * getÃ¤tigt. Wir schreiben seine Eingabe in eine Zwischenvariable die
		 * zur Kommunikation mit dem Gameloop-Thread verwendet wird und wecken
		 * diesen via "notifyAll" wieder auf. Der Gameloop-Thread wurde zuvor
		 * mit "wait()" und dieser Klasse als Semaphor "pausiert".
		 */
		letzteEingabe = strich;
>>>>>>> .r8

		synchronized (this) {
			this.notifyAll();
		}

		return true;
	}

<<<<<<< .mine
    public void updateDisplay() {
        postInvalidate(); // View force to redraw
    }
=======
	public void anzeigeAktualisieren() {
		postInvalidate(); // View zwingen, neu zu zeichnen
	}
>>>>>>> .r8

}
