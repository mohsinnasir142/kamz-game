package de.stefan_oltmann.kaesekaestchen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import de.stefan_oltmann.kaesekaestchen.model.PlayerField;
import de.stefan_oltmann.kaesekaestchen.model.Box;
import de.stefan_oltmann.kaesekaestchen.model.Line;

/**
 * This class features the playing field and takes user interactions   *
 * Counter. 
 */
public class PlayerFieldView extends View implements OnTouchListener {

	public static int BOX_PAGE_LENGHT = 50;
	public static int PADDING = 5;

	private PlayerField PlayingField;

	/**
	 * About the last entry is brought to experience what the user        made.
	 * The retrieval of this value is blocking it were in the game-flow.
	 */
	private volatile Line lastInput;

	/**
	 * About the last entry is brought to experience what the users. The
	 * retrieval of this value is blocking as it were in the game-flow.
	 */

	public PlayerFieldView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void init(PlayerField PlayingField) {
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
	 * a Cheese boxes show in dependency from the measurement resolutions of the
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

		/**
		 * Has not yet initialized the field, not draw this. Otherwise, the'd
		 * lead to a null pointer exception for. This is Required account also
		 * to be represented correctly in the GUI editor.          
		 */
		if (PlayingField == null) {
			canvas.drawRect(0, 0, getWidth(), getHeight(), new Paint());
			return;
		}

		/**
		 * If the field is not initialized, do not draw this. Otherwise this
		 * lead to a null pointer exception for. This is account to be
		 * represented correctly in the GUI editor.
		 */

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
		 * If any of the celebrity hardships position is no cheese or boxes show
		 * This already has an owner, ignore the entry.
		 */
		if (box == null || box.getOwner() != null)
			return true;

		Line strich = box.determineLine((int) event.getX(),
				(int) event.getY());

		/*
		 * Was no bar to be determined, the user has probably The center of the
		 * cheese stchens taken. Anyway, it is not clear           What line he
		 * meant. Therefore, the command is aborted.
		 */
		if (strich == null)
			return true;

		/*
		 * At this point the user has entered his successful get a ¤ account. We
		 * write its input into an intermediate variable for the Communication
		 * is used with the thread and wake Gameloop This again via "notifyAll".
		 * The Gameloop thread was previously With "wait ()" and this class as a
		 * semaphore "paused".
		 */
		lastInput = strich;
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
