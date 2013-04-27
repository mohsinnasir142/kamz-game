package de.stefan_oltmann.kaesekaestchen.model;

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
	 * A second list is  vascular diseases deriving from performance-Green
	 * leads to the 'Box List' not so   must be frequently iterates
	 * through-. For this leads to poor performance in large fields.
	 */
	private List<Box> openBoxesList = new ArrayList<Box>();
	//   strokes without owner
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

		for (int rasterX = 0; rasterX < boxInWidth; rasterX++) {
			for (int rasterY = 0; rasterY < boxInHeight; rasterY++) {
				list.add(boxArray[rasterX][rasterY]);
			}
		}

		return Collections.unmodifiableList(list);
	}

	public List<Box> getOpenBoxList() {
		return Collections.unmodifiableList(openBoxesList);
	}

	public Set<Line> getStricheOhneBesitzer() {
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
	 * Closes all box to clear it, the   can be closed.    @ param
	 Boxes show the owner of this cheese to be
	 * assigned    Could @ return a box to clear it be closed? (Important for
	 * gameplay)
	 * 
	 */

	//   assign and owner
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
	public static PlayerField generate(int numberH, int numberV) {

		PlayerField playingField = new PlayerField(numberH, numberV);

		for (int gridX = 0; gridX < numberH; gridX++) {
			for (int gridY = 0; gridY < numberV; gridY++) {

				playingField.addBox(new Box(gridX, gridY));
			}
		}

		for (int gridX = 0; gridX < numberH; gridX++) {
			for (int gridY = 0; gridY < numberV; gridY++) {

				Box box = playingField.getBox(gridX, gridY);

				Box bottomBox = null;
				Box rightBox = null;

				if (gridY < numberV - 1)
					bottomBox = playingField.getBox(gridX, gridY + 1);

				if (gridX < numberH - 1)
					rightBox = playingField.getBox(gridX + 1, gridY);

				Line strichUnten = new Line(box, bottomBox, null, null);
				Line strichRechts = new Line(null, null, box, rightBox);

				if (rightBox != null) {
					box.setRightLine(strichRechts);
					rightBox.setLeftLine(strichRechts);
					playingField.addLine(strichRechts);
				}

				if (bottomBox != null) {
					box.setBottomLine(strichUnten);
					bottomBox.setTopLine(strichUnten);
					playingField.addLine(strichUnten);
				}
			}
		}

		return playingField;
	}

}
