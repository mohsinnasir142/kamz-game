package com.game.kamz.dotsandbox.model;

import android.graphics.Bitmap;

/**
 * This class is a representative of a player. This has a name, a color and
 * symbol. It can either be human or computer
 * 
 * 
 */
public class Player {

	private String name;
	private Bitmap symbol;
	private int color;
	private PlayerType playerType;

	public Player(String name, Bitmap symbol, int color, PlayerType playerType) {
		this.name = name;
		this.symbol = symbol;
		this.color = color;
		this.playerType = playerType;
	}

	public String getName() {
		return name;
	}

	public Bitmap getSymbol() {
		return symbol;
	}

	public int getColor() {
		return color;
	}

	public PlayerType getPlayerType() {
		return playerType;
	}

	public boolean isComputerOpponent() {
		return playerType.iscomputerOpponent();
	}

	@Override
	public String toString() {
		return "Player [name=" + name + ", color=" + color + "]";
	}

}
