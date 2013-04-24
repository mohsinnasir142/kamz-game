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
 * Der SpielerManager bestimmt, welcher Spieler am Zug ist und wählt den
 * nächsten Spieler aus. Er kennt alle Mitspieler.
 * 
 * @author Stefan Oltmann
 */
public class PlayerManager {

	/** List of all players. */
	private List<Player> playerList = new ArrayList<Player>();

	/** The player, who is currently on train **/
	private Player currentPlayer;

	public PlayerManager() {
	}

	public void addPlayers(Player player) {
		playerList.add(player);
	}

	public List<Player> getSpieler() {
		return Collections.unmodifiableList(playerList);
	}

	public Player getCurrentPlayer() {
		if (currentPlayer == null)
			throw new RuntimeException(
					"Before answering the player must be 'new train' have been called at least once!");
		return currentPlayer;
	}

	public void selectNextPlayer() {

		int indexAktSpieler = playerList.indexOf(currentPlayer);

		int indexNaechsterSpieler = indexAktSpieler + 1;
		if (indexNaechsterSpieler > playerList.size() - 1)
			indexNaechsterSpieler = 0;

		currentPlayer = playerList.get(indexNaechsterSpieler);
	}

}
