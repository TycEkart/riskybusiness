package com.nl.hackathon.hyperledger.riskybusiness.ruleengine;

/**
 * Created by Tyc on 01/10/2016.
 */
public class TurnRuler {

	public Player player;
	public GameState currentState;

	public TurnRuler(Player self, GameState currentState) {
		this.player = self;
		this.currentState = currentState;
		if (self.equals(currentState.getCurrentTurnPlayer())) {
			currentState.state = TurnState.START;
		} else {
			currentState.state = TurnState.NOT_YOUR_TURN;
		}
	}

	public String attack(String attack, String def) {
		if (currentState.gameboard.containsKey(attack) && currentState.gameboard.containsKey(def)) {
			return attack(currentState.gameboard.get(attack), currentState.gameboard.get(def));
		} else {
			System.out.println("Error: can't find attack/deff for attack");
			return "Error: can't find attack/deff for attack";
		}
	}

	public String attack(TerritoryNode attack, TerritoryNode def) {
		switch (currentState.state) {
		case START:
		case ATTACK:
			currentState.state = TurnState.ATTACK;
			System.out.println("Attack: " + attack + ", def: " + def);
			if (attack.getOwner().equals(player)) {
				if (!def.getOwner().equals(player)) {
					if (attack.getArmiesAmount() > 0) {
						if (attack.isNextToTerritory(def)) {
							if (attack.getArmiesAmount() == def.getArmiesAmount()) {
								attack.killAllArmies();
								def.killAllArmies();
							} else if (attack.getArmiesAmount() > def.getArmiesAmount()) {
								attack.incrementArmiesAmount(-def.getArmiesAmount());
								def.killAllArmies();
								if (attack.getArmiesAmount() > 0) {
									def.changeOwner(attack.getOwner());
									def.incrementArmiesAmount(attack.getArmiesAmount());
									attack.killAllArmies();
								}
							} else {
								def.incrementArmiesAmount(attack.getArmiesAmount());
								attack.killAllArmies();
							}
							return null;
						} else {
							System.out.println("Error: Not a neighbour");
							return "Error: Not a neighbour";
						}
					}else{
						return "Error: What are you doing, you don't have any armies!";
					}
				} else {
					System.out.println("Error: def player == turn player");
					return "Error: def player == turn player";

				}
			} else {
				System.out.println("Error: attack player != turn player");
				return "Error: attack player != turn player";
			}
		default:
			System.out.println("Error: attack wrong state, == " + currentState.state);
			return "Error: attack wrong state, == " + currentState.state;
		}
	}

	public boolean move(TerritoryNode from, TerritoryNode to, int n) {
		if (currentState.state != TurnState.END && currentState.state != TurnState.NOT_YOUR_TURN) {
			currentState.state = TurnState.MOVE;
			System.out.println("Move From: " + from + ", to: " + to);
			if (from.getOwner().equals(player)) {
				if (to.getOwner().equals(player)) {
					if (from.isNextToTerritory(to)) {
						if (from.getArmiesAmount() - n >= 0) {
							from.incrementArmiesAmount(-n);
							to.incrementArmiesAmount(n);
							return true;
						} else {
							System.out.println("Error: Not enough armies {" + n + "}");
						}
					} else {
						System.out.println("Error: Not a neighbour");
					}
				} else {
					System.out.println("Error: to player != turn player");
				}
			} else {
				System.out.println("Error: from player != turn player");
			}
		} else {
			System.out.println("Error: Can't move, turn ended");
		}
		return false;
	}

	public boolean passTurn() {
		if (currentState.state == TurnState.START) {
			currentState.state = TurnState.PASS;
			for (TerritoryNode tn : currentState.gameboard.values()) {
				if (tn.getOwner().equals(player)) {
					tn.incrementArmiesAmount(1);
				}
			}
			return true;
		} else {
			System.out.println("Error: Already made an action");
		}
		return false;
	}

	public enum TurnState {
		NOT_YOUR_TURN, START, ATTACK, PASS, MOVE, END;
	}
}
