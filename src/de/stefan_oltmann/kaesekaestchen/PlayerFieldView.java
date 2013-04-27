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
† * Counter.
 * 
 * @author Stefan Oltmann
 */
public class PlayerFieldView extends View implements OnTouchListener {


    public static int       BOX_PAGE_LENGHT = 50;
    public static int       PADDING                = 5;

	



    private PlayerField       PlayingField;

	


    /**
     * About the last entry is brought to experience what the user
†††††† made. The retrieval of this value is blocking it were in the game-flow.
     */
    private volatile Strich lastInput;

	/**
	 * √úber die letzte Eingabe wird in Erfahrung gebracht, was der Nutzer
	 * m√∂chte. Der Abruf dieses Wertes ist sozusagen im Spiel-Ablauf blocking.
	 */

    
    
    

	public PlayerFieldView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


    public void init(PlayerField PlayingField) {
        this.PlayingField = PlayingField;
        setOnTouchListener(this);
    }



    public Strich getLastInput() {
 
    	return lastInput;
    }




    public void resetLastInput() {
    	lastInput = null;
    }




    /**
     * If the screen image to delete solution fully reflect the modified or known to initial,
††††† * This method is called. We use this to determine how big a
††††† * Cheese boxes show in dependency from the measurement resolutions of the display must be.

     */
   

    	
    	/**
	 * Wird die Bildschirmaufl√∂sung ver√§ndert oder initial bekanntgegen, wird
	 * diese Methode aufgerufen. Wir benutzen das um zu ermitteln, wie gro√ü ein
	 * K√§stchen in Abh√§ngigkeit von der Aufl√∂sung des Displays sein muss.
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (PlayingField == null)
            return;




        int maxWidth = (w - PADDING * 2) / PlayingField.getBoxInWidth();
        int maxHeight = (h - PADDING * 2) / PlayingField.getBoxInHeight();
        BOX_PAGE_LENGHT = Math.min(maxWidth, maxHeight);
    }

	


	@Override
	protected void onDraw(Canvas canvas) {

		canvas.drawColor(getResources().getColor(R.color.hintergrund_farbe));


        /** Has not yet initialized the field, not draw this.
††††††††† * Otherwise, the'd lead to a null pointer exception for. This is
††††††††† * Required account also to be represented correctly in the GUI editor.
††††††††† */
        if (PlayingField == null) {
            canvas.drawRect(0, 0, getWidth(), getHeight(), new Paint());
            return;
        }

		/*
		 * Wurde das Spielfeld noch nicht initalisiert, dieses nicht zeichnen.
		 * Ansonsten w√ºrde das zu einer NullPointer-Exception f√ºhren. Dies
		 * wird auch ben√∂tigt, um korrekt im GUI-Editor dargestellt zu werden.
		 */

        for (ShowBoxes box : PlayingField.getListBox()){
            box.onDraw(canvas);
    }
        }
		

	public boolean onTouch(View view, MotionEvent event) {

        /*
         * There are different motion events, but here we are interested only
††††††††† * The actual fact   1/4  on the screen.
         */
        if (event.getAction() != MotionEvent.ACTION_DOWN)
            return true;
		/*
		 * Es gibt verschiedene MotionEvents, aber hier interessiert uns nur das
		 * tats√§chliche Dr√ºcken auf den Bildschirm.
		 */

        if (lastInput != null)
            return true;

        int calculatedgridX = (int) event.getX() / BOX_PAGE_LENGHT;
        int calculatedgridY = (int) event.getY() / BOX_PAGE_LENGHT;

        ShowBoxes kaestchen = PlayingField.getBox(calculatedgridX, calculatedgridY);
		/*
         * If any of the celebrity hardships position is no cheese or boxes show
††††††††† * This already has an owner, ignore the entry.
         */
        if (kaestchen == null || kaestchen.getOwner() != null)
            return true;
		/*
		 * Wenn sich an der ber√ºhrten Position kein K√§stchen befindet oder
		 * dieses schon einen Besitzer hat, die Eingabe ignorieren.
		 */
		Strich strich = kaestchen.determineLine((int) event.getX(), (int) event.getY());
		
        /*
         * Was no bar to be determined, the user has probably
††††††††† * The center of the cheese stchens taken. Anyway, it is not clear
††††††††† * What line he meant. Therefore, the command is aborted.
         */
        if (strich == null)
            return true;
		/*
		 * Konnte kein Strich ermittelt werden, hat der Benutzer wahrscheinlich
		 * die Mitte des K√§stchens getroffen. Es ist jedenfalls nicht klar,
		 * welchen Strich er gemeint hat. Deshalb wird die Eingabe abgebrochen.
		 */
		/*
         * At this point the user has entered his successful get a § account.
††††††††† * We write its input into an intermediate variable for the
††††††††† * Communication is used with the thread and wake Gameloop
††††††††† * This again via "notifyAll". The Gameloop thread was previously
††††††††† * With "wait ()" and this class as a semaphore "paused".
         */
        lastInput = strich;
		/*
		 * An dieser Stelle hat der Benutzer seine Eingabe erfolgreich
		 * get√§tigt. Wir schreiben seine Eingabe in eine Zwischenvariable die
		 * zur Kommunikation mit dem Gameloop-Thread verwendet wird und wecken
		 * diesen via "notifyAll" wieder auf. Der Gameloop-Thread wurde zuvor
		 * mit "wait()" und dieser Klasse als Semaphor "pausiert".
		 */
		synchronized (this) {
			this.notifyAll();
		}

		return true;
	}

    public void updateDisplay() {
        postInvalidate(); // View force to redraw
    }


}
