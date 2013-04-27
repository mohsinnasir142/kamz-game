package com.game.kamz.dotsandbox.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The player manager determines which player is on the train and would-elected
 * the next few players. He knows all the players. 
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
