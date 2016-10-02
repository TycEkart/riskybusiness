package com.nl.hackathon.hyperledger.riskybusiness.ruleengine;

public class GameStateCaretaker {

	protected static GameStateCaretaker _instance;
    public GameState gameState;

	private GameStateCaretaker() {
	}

	public static GameStateCaretaker get_instance() {
		if (_instance == null) {
			_instance = new GameStateCaretaker();
		}
		return _instance;
	}
}
