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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * Das Spielfeld.
 * 
 * @author Stefan Oltmann
 */
public class PlayerField {

	private int boxInWidth;
	private int boxInHeight;

	private ShowBoxes[][] boxArray;

	/**
	 * A second list is º vascular diseases deriving from performance-Green
	 * leads to the 'kaestchenListe' not so h√ § must be frequently iterates
	 * through-. For this leads to poor performance in large fields.
	 */
	private List<ShowBoxes> openBoxesList = new ArrayList<ShowBoxes>();
	// stricheOhneBesitzer= strokes without owner
	private Set<Strich> strokesWithoutOwners = new HashSet<Strich>();

	/**
	 * The constructor is private because to create playing fields generate the
	 * Factory Method () should be used.
	 */
	private PlayerField(int widthOfBox, int HeightOfBox) {
		this.boxInWidth = widthOfBox;
		this.boxInHeight = HeightOfBox;

		this.boxArray = new ShowBoxes[widthOfBox][HeightOfBox];
	}

	public List<ShowBoxes> getListBox() {

		List<ShowBoxes> liste = new ArrayList<ShowBoxes>();

		for (int rasterX = 0; rasterX < boxInWidth; rasterX++) {
			for (int rasterY = 0; rasterY < boxInHeight; rasterY++) {
				liste.add(boxArray[rasterX][rasterY]);
			}
		}

		return Collections.unmodifiableList(liste);
	}

	public List<ShowBoxes> getOpenBoxList() {
		return Collections.unmodifiableList(openBoxesList);
	}

	public Set<Strich> getStricheOhneBesitzer() {
		return Collections.unmodifiableSet(strokesWithoutOwners);
	}

	private void addBox(ShowBoxes box) {
		boxArray[box.getGridX()][box.getGridY()] = box;
		openBoxesList.add(box);
	}

	private void addStrich(Strich line) {
		strokesWithoutOwners.add(line);
	}

	public ShowBoxes getBox(int gridX, int gridY) {

		if (gridX >= boxInWidth || gridY >= boxInHeight)
			return null;

		return boxArray[gridX][gridY];
	}

	public int getBoxInWidth() {
		return boxInWidth;
	}

	public int getBoxInHeight() {
		return boxInHeight;
	}

	/**
	 Closes all box to clear it, the KA ∂ can be closed.
†† @ param zuzuweisenderBesitzer
††† Boxes show the owner of this cheese to be assigned
†† Could @ return a box to clear it be closed? (Important for gameplay)       
	 *         
	 */
	
	//zuzuweisenderBesitzer==assign and owner
	private boolean concludeAllPossibleBox(
			Player zuzuweisenderBesitzer) {
//Closed box Could Become
		boolean kaestchenKonnteGeschlossenWerden = false;

		Iterator<ShowBoxes> openBoxes = openBoxesList.iterator();

		while (openBoxes.hasNext()) {

			ShowBoxes box = openBoxes.next();

			if (box.isAllLineHaveOwner()
					&& box.getOwner() == null) {
				box.setOwner(zuzuweisenderBesitzer);
				openBoxes.remove();
				kaestchenKonnteGeschlossenWerden = true;
			}
		}

		return kaestchenKonnteGeschlossenWerden;
	}

	public boolean isAllOwnerHaveBoxes() {
		return openBoxesList.isEmpty();
	}

	public boolean optForLine(Strich line, Player player) {
		line.setOwner(player);
		strokesWithoutOwners.remove(line);
		return concludeAllPossibleBox(player);
	}

	/**
	 * Factory method for creating a playing field
	 */
	public static PlayerField generate(int numberH, int numberV) {

		PlayerField playingField = new PlayerField(numberH, numberV);

		for (int gridX = 0; gridX < numberH; gridX++) {
			for (int gridY = 0; gridY < numberV; gridY++) {

				playingField.addBox(new ShowBoxes(gridX, gridY));
			}
		}

		for (int gridX = 0; gridX < numberH; gridX++) {
			for (int gridY = 0; gridY < numberV; gridY++) {

				ShowBoxes box = playingField.getBox(gridX, gridY);

				ShowBoxes bottomBox = null;
				ShowBoxes rightBox = null;

				if (gridY < numberV - 1)
					bottomBox = playingField.getBox(gridX,
							gridY + 1);

				if (gridX < numberH - 1)
					rightBox = playingField.getBox(gridX + 1,
							gridY);

				Strich strichUnten = new Strich(box, bottomBox, null,
						null);
				Strich strichRechts = new Strich(null, null, box,
						rightBox);

				if (rightBox != null) {
					box.setRightLine(strichRechts);
					rightBox.setLeftLine(strichRechts);
					playingField.addStrich(strichRechts);
				}

				if (bottomBox != null) {
					box.setBottomLine(strichUnten);
					bottomBox.setTopLine(strichUnten);
					playingField.addStrich(strichUnten);
				}
			}
		}

		return playingField;
	}

}
