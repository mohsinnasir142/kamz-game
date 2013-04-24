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
import de.stefan_oltmann.kaesekaestchen.SpielfeldView;

/**
 * Ein KÃ¤stchen auf dem Spielfeld.
 * 
 * @author Stefan Oltmann
 */
public class ShowBoxes {

	/*
	 * Position of the cheese stand in the grid. This is about ¼ hiëra
	 * identified, * so this is the ID.
	 */
	private int gridX;
	private int gridY;

	/**
	 * Could a player a cheese stick Close, he is the owner of the * cheese
	 * stchens. This counts at the end of the game as 1 victory point.
	 */
	private Player owner;

	/* Striche des KÃ¤stchens */
	private Strich topLine;
	private Strich bottomLine;
	private Strich leftLine;
	private Strich rightLine;

	private Paint framePaint = new Paint();

	/**
	 * Constructor to create the cheese stand. You must specify the position / *
	 * ID of the box to clear it.
	 */
	public ShowBoxes(int gridX, int gridY) {
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
		return gridX * SpielfeldView.BOX_PAGE_LENGHT
				+ SpielfeldView.PADDING;
	}

	public int getPixelY() {
		return gridY * SpielfeldView.BOX_PAGE_LENGHT
				+ SpielfeldView.PADDING;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public Strich getTopLine() {
		return topLine;
	}

	public void setTopLine(Strich topLine) {
		this.topLine = topLine;
	}

	public Strich getBottomLine() {
		return bottomLine;
	}

	public void setBottomLine(Strich bottomLine) {
		this.bottomLine = bottomLine;
	}

	public Strich getLeftLine() {
		return leftLine;
	}

	public void setLeftLine(Strich leftLine) {
		this.leftLine = leftLine;
	}

	public Strich getrightLine() {
		return rightLine;
	}

	public void setRightLine(Strich rightLine) {
		this.rightLine = rightLine;
	}

	public List<Strich> getLines() {

		List<Strich> line = new ArrayList<Strich>();
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

	public List<Strich> getUnselectedLinesList() {

		List<Strich> lineList = new ArrayList<Strich>();
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

	public boolean isAllLineHaveOwner() {
		return getUnselectedLinesList().size() == 0;
	}

	public Rect getRectTopLine() {

		if (topLine == null)
			return null;

		return new Rect(getPixelX() + SpielfeldView.BOX_PAGE_LENGHT/ 4,
				getPixelY() - SpielfeldView.BOX_PAGE_LENGHT / 4, getPixelX()
						+ (int) (SpielfeldView.BOX_PAGE_LENGHT * 0.75),
				getPixelY() + SpielfeldView.BOX_PAGE_LENGHT / 4);
	}

	public Rect getRectBottomLine() {

		if (bottomLine == null)
			return null;

		return new Rect(getPixelX() + SpielfeldView.BOX_PAGE_LENGHT / 4,
				getPixelY() + (int) (SpielfeldView.BOX_PAGE_LENGHT * 0.75),
				getPixelX() + (int) (SpielfeldView.BOX_PAGE_LENGHT * 0.75),
				getPixelY() + SpielfeldView.BOX_PAGE_LENGHT
						+ SpielfeldView.BOX_PAGE_LENGHT / 4);
	}

	public Rect getRectLeftLine() {

		if (leftLine == null)
			return null;

		return new Rect(getPixelX() - SpielfeldView.BOX_PAGE_LENGHT / 4,
				getPixelY() + SpielfeldView.BOX_PAGE_LENGHT / 4, getPixelX()
						+ SpielfeldView.BOX_PAGE_LENGHT / 4, getPixelY()
						+ (int) (SpielfeldView.BOX_PAGE_LENGHT * 0.75));
	}

	public Rect getRectRightLine() {

		if (rightLine == null)
			return null;

		return new Rect(getPixelX()
				+ (int) (SpielfeldView.BOX_PAGE_LENGHT * 0.75), getPixelY()
				+ SpielfeldView.BOX_PAGE_LENGHT / 4, getPixelX()
				+ SpielfeldView.BOX_PAGE_LENGHT
				+ SpielfeldView.BOX_PAGE_LENGHT / 4, getPixelY()
				+ (int) (SpielfeldView.BOX_PAGE_LENGHT * 0.75));
	}

	/**
	 * This method determines which line of cheese bites in sequence
	 */
	public Strich determineLine(int pixelX, int pixelY) {

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
					+ SpielfeldView.BOX_PAGE_LENGHT, getPixelY()
					+ SpielfeldView.BOX_PAGE_LENGHT);
			canvas.drawBitmap(owner.getSymbol(), null, destRect, framePaint);
		}

		if (topLine == null) {
			framePaint.setColor(Color.BLACK);
			canvas.drawLine(getPixelX(), getPixelY(), getPixelX()
					+ SpielfeldView.BOX_PAGE_LENGHT, getPixelY(), framePaint);
		}

		if (bottomLine != null && bottomLine.getOwner() != null)
			framePaint.setColor(bottomLine.getOwner().getColor());
		else if (bottomLine != null)
			framePaint.setColor(Color.LTGRAY);
		else
			framePaint.setColor(Color.BLACK);

		canvas.drawLine(getPixelX(), getPixelY()
				+ SpielfeldView.BOX_PAGE_LENGHT, getPixelX()
				+ SpielfeldView.BOX_PAGE_LENGHT, getPixelY()
				+ SpielfeldView.BOX_PAGE_LENGHT, framePaint);

		if (leftLine == null) {
			framePaint.setColor(Color.BLACK);
			canvas.drawLine(getPixelX(), getPixelY(), getPixelX(), getPixelY()
					+ SpielfeldView.BOX_PAGE_LENGHT, framePaint);
		}

		if (rightLine != null && rightLine.getOwner() != null)
			framePaint.setColor(rightLine.getOwner().getColor());
		else if (rightLine != null)
			framePaint.setColor(Color.LTGRAY);
		else
			framePaint.setColor(Color.BLACK);

		canvas.drawLine(getPixelX() + SpielfeldView.BOX_PAGE_LENGHT,
				getPixelY(), getPixelX() + SpielfeldView.BOX_PAGE_LENGHT,
				getPixelY() + SpielfeldView.BOX_PAGE_LENGHT, framePaint);

		/* vertices draw */
		framePaint.setColor(Color.BLACK);
		canvas.drawRect(getPixelX() - 1, getPixelY() - 1, getPixelX() + 1,
				getPixelY() + 1, framePaint);
		canvas.drawRect(getPixelX() + SpielfeldView.BOX_PAGE_LENGHT - 1,
				getPixelY() - 1, getPixelX() + SpielfeldView.BOX_PAGE_LENGHT
						+ 1, getPixelY() + 1, framePaint);
		canvas.drawRect(getPixelX() - 1, getPixelY()
				+ SpielfeldView.BOX_PAGE_LENGHT - 1, getPixelX() + 1,
				getPixelY() + SpielfeldView.BOX_PAGE_LENGHT + 1, framePaint);
		canvas.drawRect(getPixelX() + SpielfeldView.BOX_PAGE_LENGHT - 1,
				getPixelY() + SpielfeldView.BOX_PAGE_LENGHT - 1, getPixelX()
						+ SpielfeldView.BOX_PAGE_LENGHT + 1, getPixelY()
						+ SpielfeldView.BOX_PAGE_LENGHT + 1, framePaint);
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
		ShowBoxes other = (ShowBoxes) obj;
		if (gridX != other.gridX)
			return false;
		if (gridY != other.gridY)
			return false;
		return true;
	}

}
