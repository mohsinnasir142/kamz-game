package com.game.kamz.dotsandbox.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * The game field.
 */
public class PlayerField {

	private int boxInWidth;
	private int boxInHeight;

	private Box[][] boxArray;

	/**
	 * A second list is vascular diseases deriving from performance-Green leads
	 * to the 'Box List' not so must be frequently iterates through-. For this
	 * leads to poor performance in large fields.
	 */
	private List<Box> openBoxesList = new ArrayList<Box>();
	// strokes without owner
	private Set<Line> strokesWithoutOwners = new HashSet<Line>();

	/**
	 * The constructor is private because to create playing fields generate the
	 * Factory Method () should be used.
	 */
	private PlayerField(int widthOfBox, int HeightOfBox) {
		this.boxInWidth = widthOfBox;
		this.boxInHeight = HeightOfBox;

		this.boxArray = new Box[widthOfBox][HeightOfBox];
	}

	public List<Box> getListBox() {

		List<Box> list = new ArrayList<Box>();

		for (int gridX = 0; gridX < boxInWidth; gridX++) {
			for (int gridY = 0; gridY < boxInHeight; gridY++) {
				list.add(boxArray[gridX][gridY]);
			}
		}

		return Collections.unmodifiableList(list);
	}

	// public List<Box> getOpenBoxList() {
	// return Collections.unmodifiableList(openBoxesList);
	// }

	public Set<Line> getStrokesWithoutOwners() {
		return Collections.unmodifiableSet(strokesWithoutOwners);
	}

	private void addBox(Box box) {
		boxArray[box.getGridX()][box.getGridY()] = box;
		openBoxesList.add(box);
	}

	private void addLine(Line line) {
		strokesWithoutOwners.add(line);
	}

	public Box getBox(int gridX, int gridY) {

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
	 * Closes all box to clear it, the can be closed.    @ param Boxes show the
	 * owner of this cheese to be assigned    Could @ return a box to clear it
	 * be closed? (Important for gameplay)
	 * 
	 */

	// assign and owner
	private boolean concludeAllPossibleBox(Player assignAndOwner) {
		// Closed box Could Become
		boolean isBoxClosed = false;

		Iterator<Box> openBoxes = openBoxesList.iterator();

		while (openBoxes.hasNext()) {

			Box box = openBoxes.next();

			if (box.isEveryLineHasOwner() && box.getOwner() == null) {
				box.setOwner(assignAndOwner);
				openBoxes.remove();
				isBoxClosed = true;
			}
		}

		return isBoxClosed;
	}

	public boolean isAllOwnerHaveBoxes() {
		return openBoxesList.isEmpty();
	}

	public boolean optForLine(Line line, Player player) {
		line.setOwner(player);
		strokesWithoutOwners.remove(line);
		return concludeAllPossibleBox(player);
	}

	/**
	 * Factory method for creating a playing field
	 */

	// TODO all the magic is being done here

	public static PlayerField generate(int numberH, int numberW) {

		PlayerField playingField = new PlayerField(numberH, numberW);

		for (int gridX = 0; gridX < numberH; gridX++) {
			for (int gridY = 0; gridY < numberW; gridY++) {


					playingField.addBox(new Box(gridX, gridY));

			}
		}
		/*
		 * RUNNING LIKE
		 * *******************************************************************
		 * 00 01 02 . . . . . 0w /////////////////////////////////////////////
		 * 10 11 12 . . . . . 1w /////////////////////////////////////////////
		 * 20 21 22 . . . . . 2w /////////////////////////////////////////////
		 * .. .. .. . . . . . .. /////////////////////////////////////////////
		 * .. .. . . . . . .. .. /////////////////////////////////////////////
		 * h0 .. .. . . . . . hw /////////////////////////////////////////////
		 * *******************************************************************
		 */
		for (int gridY = 0; gridY < numberH; gridY++) {
			for (int gridX = 0; gridX < numberW; gridX++) {

				Box topBox = playingField.getBox(gridY, gridX);

				Box bottomBox = null;
				Box rightBox = null;
				Box leftBox = null;

				// it means if width is 3 then grid is 0-2 and will run one less
				// than numberH
				if (gridX < numberW - 1)
					// getbox(width,height)
					bottomBox = playingField.getBox(gridY, gridX + 1);

				if (gridY < numberH - 1)
					rightBox = playingField.getBox(gridY + 1, gridX);

				if (gridY > 0)
					leftBox = playingField.getBox(gridY - 1, gridX);
				// FIXME

				Line belowLine = new Line(topBox, bottomBox, null, rightBox);
				Line rightLine = new Line(null, null, topBox, rightBox);
				Line leftLine = new Line(null, null, null, rightBox);

				if (rightBox != null) {
					topBox.setRightLine(rightLine);
					rightBox.setLeftLine(rightLine);
					
					
					playingField.addLine(rightLine);
				}

				if (bottomBox != null) {
					topBox.setBottomLine(belowLine);
					bottomBox.setTopLine(belowLine);
					playingField.addLine(belowLine);
				}

				// TEST1
				// if (leftBox != null) {
				// //topBox.setLeftLine(leftLine);
				// leftBox.setTopLine(leftLine);
				// //playingField.addLine(leftLine);
				// }

				// TEST1 left line will be reemoved

				// if (topBox != null) {
				// topBox.setRightLine(leftLine);
				// rightBox.setLeftLine(leftLine);
				// playingField.addLine(leftLine);
				// }
			}
		}

		return playingField;
	}

}
