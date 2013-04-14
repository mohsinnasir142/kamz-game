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

/**
 * Spieler können entweder Menschen sein oder eine von 3 KI-Stärken.
 * 
 * @author Stefan Oltmann
 */
public enum PlayerType {

	MAN, COMPUTER_EASY, COMPUTER_MEDIUM, COMPUTER_HARD;

	/**
	 * Wenn im Spinner ein Typ ausgewählt wurde, können wir nur die
	 * String-Repräsentation davon abfragen. Der Spinner nimmt leider keine
	 * Enums entgegen. Deshalb müssen wir den richtigen Typ aus dem String
	 * parsen.
	 */
	public static PlayerType parse(String string) {

		if (string == null)
			return null;

		if (string.equals("Mensch") || string.equals("Human"))
			return MAN;

		if (string.equals("KI Leicht") || string.equals("KI Easy"))
			return COMPUTER_EASY;

		if (string.equals("KI Mittel") || string.equals("KI Medium"))
			return COMPUTER_MEDIUM;

		if (string.equals("KI Schwer") || string.equals("KI Hard"))
			return COMPUTER_HARD;

		throw new IllegalArgumentException("Unbekannter SpielerTyp: " + string);
	}

	public boolean isComputerGegner() {
		return this == PlayerType.COMPUTER_EASY
				|| this == PlayerType.COMPUTER_MEDIUM
				|| this == PlayerType.COMPUTER_HARD;
	}

}
