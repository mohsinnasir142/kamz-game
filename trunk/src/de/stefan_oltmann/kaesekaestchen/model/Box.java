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
package de.stefan_oltmann.kaesekaestchen.model;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import de.stefan_oltmann.kaesekaestchen.PlayerFieldView;

/**
 * Ein Kästchen auf dem Spielfeld.
 * 
 * @author Stefan Oltmann
 */
public class Box {

	/**
	 * Position of the cheese stand in the grid. This is about 
	 * identified,   so this is the ID.
	 */
	private int gridX;
	private int gridY;

	/**
	 * Could a player a cheese stick Close, he is the owner of the * cheese
	 * stchens. This counts at the end of the game as 1 victory point.
	 */
	private Player owner;

	/* Bars of cheese pricking*/
	private Line topLine;
	private Line bottomLine;
	private Line leftLine;
	private Line rightLine;

	private Paint framePaint = new Paint();

	/**
	 * Constructor to create the cheese stand. You must specify the position / *
	 * ID of the box to clear it.
	 */
	public Box(int gridX, int gridY) {
		this.gridX = gridX;
		this.gridY = gridY;

		framePaint.setStyle(Paint.Style.STROKE);
		framePaint.setStrokeWidth(5);
	}

	public int getGridX() {
		return gridX;
	}

	public int getGridY() {
		return gridY;
	}

	public int getPixelX() {
		return gridX * PlayerFieldView.BOX_PAGE_LENGHT
				+ PlayerFieldView.PADDING;
	}

	public int getPixelY() {
		return gridY * PlayerFieldView.BOX_PAGE_LENGHT
				+ PlayerFieldView.PADDING;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public Line getTopLine() {
		return topLine;
	}

	public void setTopLine(Line topLine) {
		this.topLine = topLine;
	}

	public Line getBottomLine() {
		return bottomLine;
	}

	public void setBottomLine(Line bottomLine) {
		this.bottomLine = bottomLine;
	}

	public Line getLeftLine() {
		return leftLine;
	}

	public void setLeftLine(Line leftLine) {
		this.leftLine = leftLine;
	}

	public Line getrightLine() {
		return rightLine;
	}

	public void setRightLine(Line rightLine) {
		this.rightLine = rightLine;
	}

	public List<Line> getLines() {

		List<Line> line = new ArrayList<Line>();
		if (topLine != null)
			line.add(topLine);
		if (bottomLine != null)
			line.add(bottomLine);
		if (leftLine != null)
			line.add(leftLine);
		if (rightLine != null)
			line.add(rightLine);
		return line;
	}

	public List<Line> getUnselectedLinesList() {

		List<Line> lineList = new ArrayList<Line>();
		if (topLine != null && topLine.getOwner() == null)
			lineList.add(topLine);
		if (bottomLine != null && bottomLine.getOwner() == null)
			lineList.add(bottomLine);
		if (leftLine != null && leftLine.getOwner() == null)
			lineList.add(leftLine);
		if (rightLine != null && rightLine.getOwner() == null)
			lineList.add(rightLine);
		return lineList;
	}

	public boolean isEveryLineHasOwner() {
		return getUnselectedLinesList().size() == 0;
	}

	public Rect getRectTopLine() {

		if (topLine == null)
			return null;

		return new Rect(getPixelX() + PlayerFieldView.BOX_PAGE_LENGHT/ 4,
				getPixelY() - PlayerFieldView.BOX_PAGE_LENGHT / 4, getPixelX()
						+ (int) (PlayerFieldView.BOX_PAGE_LENGHT * 0.75),
				getPixelY() + PlayerFieldView.BOX_PAGE_LENGHT / 4);
	}

	public Rect getRectBottomLine() {

		if (bottomLine == null)
			return null;

		return new Rect(getPixelX() + PlayerFieldView.BOX_PAGE_LENGHT / 4,
				getPixelY() + (int) (PlayerFieldView.BOX_PAGE_LENGHT * 0.75),
				getPixelX() + (int) (PlayerFieldView.BOX_PAGE_LENGHT * 0.75),
				getPixelY() + PlayerFieldView.BOX_PAGE_LENGHT
						+ PlayerFieldView.BOX_PAGE_LENGHT / 4);
	}

	public Rect getRectLeftLine() {

		if (leftLine == null)
			return null;

		return new Rect(getPixelX() - PlayerFieldView.BOX_PAGE_LENGHT / 4,
				getPixelY() + PlayerFieldView.BOX_PAGE_LENGHT / 4, getPixelX()
						+ PlayerFieldView.BOX_PAGE_LENGHT / 4, getPixelY()
						+ (int) (PlayerFieldView.BOX_PAGE_LENGHT * 0.75));
	}

	public Rect getRectRightLine() {

		if (rightLine == null)
			return null;

		return new Rect(getPixelX()
				+ (int) (PlayerFieldView.BOX_PAGE_LENGHT * 0.75), getPixelY()
				+ PlayerFieldView.BOX_PAGE_LENGHT / 4, getPixelX()
				+ PlayerFieldView.BOX_PAGE_LENGHT
				+ PlayerFieldView.BOX_PAGE_LENGHT / 4, getPixelY()
				+ (int) (PlayerFieldView.BOX_PAGE_LENGHT * 0.75));
	}

	/**
	 * This method determines which line of cheese bites in sequence
	 */
	public Line determineLine(int pixelX, int pixelY) {

		if (getRectTopLine() != null
				&& getRectTopLine().contains(pixelX, pixelY))
			return topLine;

		if (getRectBottomLine() != null
				&& getRectBottomLine().contains(pixelX, pixelY))
			return bottomLine;

		if (getRectLeftLine() != null
				&& getRectLeftLine().contains(pixelX, pixelY))
			return leftLine;

		if (getRectRightLine() != null
				&& getRectRightLine().contains(pixelX, pixelY))
			return rightLine;

		return null;
	}

	public void onDraw(Canvas canvas) {

		if (owner != null) {

			Paint fillingPaint = new Paint();
			fillingPaint.setColor(owner.getColor());

			Rect destRect = new Rect(getPixelX(), getPixelY(), getPixelX()
					+ PlayerFieldView.BOX_PAGE_LENGHT, getPixelY()
					+ PlayerFieldView.BOX_PAGE_LENGHT);
			canvas.drawBitmap(owner.getSymbol(), null, destRect, framePaint);
		}

		if (topLine == null) {
			framePaint.setColor(Color.BLACK);
			canvas.drawLine(getPixelX(), getPixelY(), getPixelX()
					+ PlayerFieldView.BOX_PAGE_LENGHT, getPixelY(), framePaint);
		}

		if (bottomLine != null && bottomLine.getOwner() != null)
			framePaint.setColor(bottomLine.getOwner().getColor());
		else if (bottomLine != null)
			framePaint.setColor(Color.LTGRAY);
		else
			framePaint.setColor(Color.BLACK);

		canvas.drawLine(getPixelX(), getPixelY()
				+ PlayerFieldView.BOX_PAGE_LENGHT, getPixelX()
				+ PlayerFieldView.BOX_PAGE_LENGHT, getPixelY()
				+ PlayerFieldView.BOX_PAGE_LENGHT, framePaint);

		if (leftLine == null) {
			framePaint.setColor(Color.BLACK);
			canvas.drawLine(getPixelX(), getPixelY(), getPixelX(), getPixelY()
					+ PlayerFieldView.BOX_PAGE_LENGHT, framePaint);
		}

		if (rightLine != null && rightLine.getOwner() != null)
			framePaint.setColor(rightLine.getOwner().getColor());
		else if (rightLine != null)
			framePaint.setColor(Color.LTGRAY);
		else
			framePaint.setColor(Color.BLACK);

		canvas.drawLine(getPixelX() + PlayerFieldView.BOX_PAGE_LENGHT,
				getPixelY(), getPixelX() + PlayerFieldView.BOX_PAGE_LENGHT,
				getPixelY() + PlayerFieldView.BOX_PAGE_LENGHT, framePaint);

		/* vertices draw */
		framePaint.setColor(Color.BLACK);
		canvas.drawRect(getPixelX() - 1, getPixelY() - 1, getPixelX() + 1,
				getPixelY() + 1, framePaint);
		canvas.drawRect(getPixelX() + PlayerFieldView.BOX_PAGE_LENGHT - 1,
				getPixelY() - 1, getPixelX() + PlayerFieldView.BOX_PAGE_LENGHT
						+ 1, getPixelY() + 1, framePaint);
		canvas.drawRect(getPixelX() - 1, getPixelY()
				+ PlayerFieldView.BOX_PAGE_LENGHT - 1, getPixelX() + 1,
				getPixelY() + PlayerFieldView.BOX_PAGE_LENGHT + 1, framePaint);
		canvas.drawRect(getPixelX() + PlayerFieldView.BOX_PAGE_LENGHT - 1,
				getPixelY() + PlayerFieldView.BOX_PAGE_LENGHT - 1, getPixelX()
						+ PlayerFieldView.BOX_PAGE_LENGHT + 1, getPixelY()
						+ PlayerFieldView.BOX_PAGE_LENGHT + 1, framePaint);
	}

	@Override
	public String toString() {
		return "Kaestchen [grid X=" + gridX + ", grid Y=" + gridY
				+ ", Owner=" + owner + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + gridX;
		result = prime * result + gridY;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Box other = (Box) obj;
		if (gridX != other.gridX)
			return false;
		if (gridY != other.gridY)
			return false;
		return true;
	}

}
