package com.nl.hackathon.hyperledger.riskybusiness.ruleengine;

/**
 * Created by Tyc on 01/10/2016.
 */
public class TurnRuler {

	public Player selfPlayer;
	public GameState currentState;

	public TurnRuler(Player selfPlayer, GameState currentState) {
		this.selfPlayer = selfPlayer;
		this.currentState = currentState;
		if (!selfPlayer.equals(currentState.getCurrentTurnPlayer())) {
			currentState.setState(TurnState.NOT_YOUR_TURN);
		} else {
			if (currentState.getState() == TurnState.START || currentState.getState() == null) {
				currentState.setState(TurnState.START);
			}
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
		switch (currentState.getState()) {
		case START:
		case ATTACK:
			currentState.setState(TurnState.ATTACK);
			System.out.println("Attack: " + attack + ", def: " + def);
			if (attack.getOwner().equals(selfPlayer)) {
				if (!def.getOwner().equals(selfPlayer)) {
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
					} else {
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
			System.out.println("Error: attack wrong state, == " + currentState.getState());
			return "Error: attack wrong state, == " + currentState.getState();
		}
	}

	public String move(String from, String to, int number) {
		if (currentState.gameboard.containsKey(from) && currentState.gameboard.containsKey(to)) {
			return move(currentState.gameboard.get(from), currentState.gameboard.get(to), number);
		} else {
			System.out.println("Error: can't find from/to for move");
			return "Error: can't find attack/deff for attack";
		}
	}

	public String move(TerritoryNode from, TerritoryNode to, int n) {
		if (currentState.getState() != TurnState.NOT_YOUR_TURN) {
			if (currentState.getState() != TurnState.END) {
				currentState.setState(TurnState.MOVE);
				System.out.println("Move From: " + from + ", to: " + to);
				if (from.getOwner().equals(selfPlayer)) {
					if (to.getOwner().equals(selfPlayer)) {
						if (!from.equals(to)) {
							if (from.isNextToTerritory(to)) {
								if (from.getArmiesAmount() - n >= 0) {
									from.incrementArmiesAmount(-n);
									to.incrementArmiesAmount(n);
									return null;
								} else {
									System.out.println("Error: Not enough armies {" + n + "}");
									return "Error: Not enough armies {" + n + "}";
								}
							} else {
								System.out.println("Error: Not a neighbour");
								return "Error: Not a neighbour";
							}
						} else {
							System.out.println("Error: Your troops are already there!");
							return "Error: Your troops are already there!";
						}
					} else {
						System.out.println("Error: not your land yet!");
						return "Error: not your land yet!";
					}
				} else {
					System.out.println("Error: from player != turn player");
					return "Error: from player != turn player";

				}
			} else {
				System.out.println("Error: Can't move, your turn ended");
				return "Error: Can't move, turn ended";
			}
		} else {
			System.out.println("Error: Can't move, not your turn");
			return "Error: Can't move, not your turn";
		}
	}

	public boolean passTurn() {
		if (currentState.getState() == TurnState.START) {
			currentState.setState(TurnState.PASS);
			for (TerritoryNode tn : currentState.gameboard.values()) {
				if (tn.getOwner().equals(selfPlayer)) {
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

	public String setTurnTypeToAttack(boolean b) {
		if (currentState.getState() == TurnState.START) {
			if (b) {
				currentState.setState(TurnState.ATTACK);
			} else {
				currentState.setState(TurnState.PASS);

			}
			return null;
		} else {
			if (b && currentState.getState() == TurnState.ATTACK) {
				return "Error: Attack? ATTACK, YOU ARE ATTACKING!!!";
			} else if (!b && currentState.getState() == TurnState.PASS) {
				return "Error: You already passed your turn, you are a coward by doing it again!";
			} else {
				return "Error: current state is not start! {" + currentState.getState() + "}";
			}
		}
	}
}
