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
package com.game.kamz.dotsandbox.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The gameplay consists of, to put dashes boxes show to close. This class is a
 * representative of such a line.
 */
public class Line {

	/* ID of the line */
	private Box topBox;
	private Box bottomBox;
	private Box leftBox;
	private Box rightBox;

	/* Collection to iterate through- */
	private List<Box> boxList = new ArrayList<Box>();

	private Player owner;

	public Line(Box topBox, Box bottomBox, Box leftBox, Box rightBox) {

		this.topBox = topBox;
		this.bottomBox = bottomBox;
		this.leftBox = leftBox;
		this.rightBox = rightBox;

		if (topBox != null)
			boxList.add(topBox);

		if (bottomBox != null)
			boxList.add(bottomBox);

		if (leftBox != null)
			boxList.add(leftBox);

		if (rightBox != null)
			boxList.add(rightBox);
	}

	public Box getTopBox() {
		return topBox;
	}

	public Box getBottomBox() {
		return bottomBox;
	}

	public Box getLeftBox() {
		return leftBox;
	}

	public Box getRightBox() {
		return rightBox;
	}

	public List<Box> getListBox() {
		return Collections.unmodifiableList(boxList);
	}

	/**
	 * If one of the box to clear at this bar only has two owners, after setting
	 * this stroke only one order'd give you a boxes show the opponent.
	 */

	// It could Surrounding In box Close(if surrounding box is closed)
	// public boolean isSurroundingBoxClose() {
	//
	// for (Box box : boxList)
	// if (box.getUnselectedLinesList().size() <= 2)
	// return true;
	//
	// return false;
	// }

	// get owner
	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	@Override
	public String toString() {
		return "line [top Box=" + topBox + ", bottom box=" + bottomBox
				+ ", left box=" + leftBox + ", right box=" + rightBox
				+ ", owner=" + owner + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((leftBox == null) ? 0 : leftBox.hashCode());
		result = prime * result + ((topBox == null) ? 0 : topBox.hashCode());
		result = prime * result
				+ ((rightBox == null) ? 0 : rightBox.hashCode());
		result = prime * result
				+ ((bottomBox == null) ? 0 : bottomBox.hashCode());
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
		Line other = (Line) obj;
		if (leftBox == null) {
			if (other.leftBox != null)
				return false;
		} else if (!leftBox.equals(other.leftBox))
			return false;
		if (topBox == null) {
			if (other.topBox != null)
				return false;
		} else if (!topBox.equals(other.topBox))
			return false;
		if (rightBox == null) {
			if (other.rightBox != null)
				return false;
		} else if (!rightBox.equals(other.rightBox))
			return false;
		if (bottomBox == null) {
			if (other.bottomBox != null)
				return false;
		} else if (!bottomBox.equals(other.bottomBox))
			return false;
		return true;
	}

}
