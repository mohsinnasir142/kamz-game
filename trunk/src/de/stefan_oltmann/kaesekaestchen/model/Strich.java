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
import java.util.Collections;
import java.util.List;

/**
 * Das Spielprinzip besteht daraus, Striche zu setzen um Kästchen zu schließen.
 * Diese Klasse repräsentiert einen solchen Strich.
 * 
 * @author Stefan Oltmann
 */
public class Strich {

	/* ID of the line */
	private ShowBoxes topBox;
	private ShowBoxes bottomBox;
	private ShowBoxes leftBox;
	private ShowBoxes rightBox;

	/* Collection to iterate through- */
	private List<ShowBoxes> boxList = new ArrayList<ShowBoxes>();

	private Player owner;

	public Strich(ShowBoxes topBox, ShowBoxes bottomBox, ShowBoxes leftBox, ShowBoxes rightBox) {

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

	public ShowBoxes getTopBox() {
		return topBox;
	}

	public ShowBoxes getBottomBox() {
		return bottomBox;
	}

	public ShowBoxes getLeftBox() {
		return leftBox;
	}

	public ShowBoxes getRightBox() {
		return rightBox;
	}

	public List<ShowBoxes> getListBox() {
		return Collections.unmodifiableList(boxList);
	}

	/**
	 * If one of the box to clear at this bar only has two owners, it dannh #
	 * tte after setting this stroke only one order'd give you a cheese boxes
	 * show the opponent.
	 */

	// It could Surrounding In box Close(if surrounding box is closed)
	public boolean isSurroundingBoxClose() {

		for (ShowBoxes box : boxList)
			if (box.getUnselectedLinesList().size() <= 2)
				return true;

		return false;
	}

	// get besitzer = get owner
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
		Strich other = (Strich) obj;
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
