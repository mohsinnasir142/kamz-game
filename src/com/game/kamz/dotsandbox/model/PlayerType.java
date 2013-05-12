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

/**
 * KA player can be either human or POINTS of AI bars.
 */
public enum PlayerType {

	MAN, COMPUTER_EASY, COMPUTER_MEDIUM, COMPUTER_HARD;

	/**
	 * If a Data of type was selected in the spinner, we can only the
	 * string Rep. presentation of query. Unfortunately, the spinner does not
	 * accept enums. Therefore, we must m√ º parse the correct type from the
	 * string.
	 */
	public static PlayerType parse(String string) {

		if (string == null)
			return null;

		if (string.equals("Human"))
			return MAN;
//
//		if (string.equals("AI Easy"))
//			return COMPUTER_EASY;
//
//		if (string.equals("AI Medium"))
//			return COMPUTER_MEDIUM;
//
//		if (string.equals("KI Hard"))
//			return COMPUTER_HARD;

		throw new IllegalArgumentException("Unknown Player Type: " + string);
	}

	public boolean iscomputerOpponent() {
		return this == PlayerType.COMPUTER_EASY
				|| this == PlayerType.COMPUTER_MEDIUM
				|| this == PlayerType.COMPUTER_HARD;
	}

}
