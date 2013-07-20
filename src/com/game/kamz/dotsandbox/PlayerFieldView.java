package com.game.kamz.dotsandbox;

import com.game.kamz.dotsandbox.model.Box;
import com.game.kamz.dotsandbox.model.Line;
import com.game.kamz.dotsandbox.model.PlayerField;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.game.kamz.dotsandbox.R;

/**
 * This class features the playing field and takes user interactions   *
 * Counter.
 */
public class PlayerFieldView extends View implements OnTouchListener {

	public static int BOX_PAGE_LENGHT = 50;
	public static int PADDING = 5;

	private PlayerField PlayingField;

	private volatile Line lastInput;
	// for sound
	public static Boolean check = true;
	MediaPlayer mp;

	/**
	 * About the last entry is brought to experience what the users. The
	 * retrieval of this value is blocking as it were in the game-flow.
	 */

	public PlayerFieldView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void init(PlayerField PlayingField) {
		// for Sound
		check = true;
		this.PlayingField = PlayingField;
		setOnTouchListener(this);
	}

	public Line getLastInput() {

		return lastInput;
	}

	public void resetLastInput() {
		lastInput = null;
	}

	/**
	 * If the screen image to delete solution fully reflect the modified or
	 * known to initial, This method is called. We use this to determine how big
	 * a boxes show in dependency from the measurement resolutions of the
	 * display must be.
	 */

	/**
	 * how big boxes must be drawn on the basis of screen resolution
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
		canvas.drawColor(getResources().getColor(R.color.background_color));
		// for sound

		if (check == true) {
			check = false;
		}

		else if (check == false) {

			// action for sound onclick

			mp = MediaPlayer.create(getContext(), R.raw.popup);
			mp.start();

		}
		// Dialog d=new Dialog(getContext());
		// d.setTitle("::");
		// d.show();

		/**
		 * Has not yet initialized the field, not draw this. Otherwise, the'd
		 * lead to a null pointer exception for. This is Required account also
		 * to be represented correctly in the GUI editor.          
		 */
		if (PlayingField == null) {
			canvas.drawRect(0, 0, getWidth(), getHeight(), new Paint());
			return;
		}

		for (Box box : PlayingField.getListBox()) {
			box.onDraw(canvas);
		}
	}

	public boolean onTouch(View view, MotionEvent event) {

		/**
		 * There are different motion events, but here we are interested only
		 * The actual fact 1/4 on the screen.
		 */
		if (event.getAction() != MotionEvent.ACTION_DOWN)
			return true;
		/*
		 * There are different motion events, but we are interested here only
		 * the facts The actual on the screen.
		 */

		if (lastInput != null)
			return true;

		int calculatedgridX = (int) event.getX() / BOX_PAGE_LENGHT;
		int calculatedgridY = (int) event.getY() / BOX_PAGE_LENGHT;

		Box box = PlayingField.getBox(calculatedgridX, calculatedgridY);
		/*
		 * If any of the celebrity hardships position is no boxes show This
		 * already has an owner, ignore the entry.
		 */
		if (box == null || box.getOwner() != null)
			return true;

		Line line = box.determineLine((int) event.getX(), (int) event.getY());

		/*
		 * Was no bar to be determined, the user has probably The center of the
		 * cheese taken. Anyway, it is not clear What line he meant. Therefore,
		 * the command is aborted.
		 */
		if (line == null)
			return true;


		lastInput = line;
		/*
		 * At this point the user has input his account successfully get. We
		 * write its input into an intermediate variable that is used to
		 * communicate with the Gameloop thread and wake it on via "notifyAll"
		 * again. The Gameloop thread was previously "paused" with "wait ()" and
		 * this class as a semaphore.
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
