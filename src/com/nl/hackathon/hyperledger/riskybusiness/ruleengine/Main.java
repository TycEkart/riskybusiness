package com.nl.hackathon.hyperledger.riskybusiness.ruleengine;

import java.awt.*;

public class Main {
    static Player self = new Player("Thijs", "192.168.0.102", Color.BLUE);

    public static void main(String[] args) {
        // write your code here


        GameState game = new GameState(null);
        game.addPlayer(self);
        game.addPlayer(new Player("Jan-Paul", "192.168.0.101", Color.RED));
        game.addPlayer(new Player("Aschwin", "192.168.0.?", Color.GREEN));
        game.createBoard();
        game.assignTerritories();

        game.gameboard.get("0").incrementArmiesAmount(3);
        System.out.println("INIT!!!");
        game.printGui();
        System.out.println("\n\n");


        doSpecifiedTurn(game);

        //Caretaker.getInstance().gameState = game;
        //new MyFrame();
    }

    private static void doSpecifiedTurn(GameState game) {
        game.setPlayerTurn();

        //start turn
        TurnRuler ruler = new TurnRuler(self, game);

        if (ruler.passTurn()) {
            System.out.println("\n\n");
            game.printGui();
        }
        if (ruler.attack(game.gameboard.get("0"), game.gameboard.get("1"))) {
            System.out.println("\n\n");
            game.printGui();
        }


        if (ruler.move(game.gameboard.get("1"), game.gameboard.get("0"), 1)) {
            System.out.println("\n\n");
            game.printGui();
        }

        if (ruler.attack(game.gameboard.get("1"), game.gameboard.get("2"))) {
            System.out.println("\n\n");
            game.printGui();
        }
    }
}
