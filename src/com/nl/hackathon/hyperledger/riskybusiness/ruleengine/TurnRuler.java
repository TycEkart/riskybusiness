package com.nl.hackathon.hyperledger.riskybusiness.ruleengine;

/**
 * Created by Tyc on 01/10/2016.
 */
public class TurnRuler {

    public Player player;
    public GameState currentState;
    private TurnState state;

    public TurnRuler(Player self, GameState currentState) {
        this.player = self;
        this.currentState = currentState;
        if (self.equals(currentState.getCurrentTurnPlayer())) {
            state = TurnState.START;
        } else {
            state = TurnState.NOT_YOUR_TURN;
        }
    }

    public boolean attack(TerritoryNode attack, TerritoryNode def) {
        switch (state) {
            case START:
            case ATTACK:
                state = TurnState.ATTACK;
                System.out.println("Attack: " + attack + ", def: " + def);
                if (attack.getOwner().equals(player)) {
                    if (!def.getOwner().equals(player)) {
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
                            return true;
                        } else {
                            System.out.println("Error: Not a neighbour");
                        }
                    } else {
                        System.out.println("Error: def player == turn player");
                    }
                } else {
                    System.out.println("Error: attack player != turn player");
                }
                break;
            default:
                System.out.println("Error: attack wrong state, == " + state);
        }
        return false;
    }

    public boolean move(TerritoryNode from, TerritoryNode to, int n) {
        if (state != TurnState.END && state != TurnState.NOT_YOUR_TURN) {
            state = TurnState.MOVE;
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
        if (state == TurnState.START) {
            state = TurnState.PASS;
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
