package com.nl.hackathon.hyperledger.riskybusiness.ruleengine;

import java.util.*;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.nl.hackathon.hyperledger.riskybusiness.ruleengine.TurnRuler.TurnState;

/**
 * Created by Tyc on 01/10/2016.
 */
public class GameState {
	private Player previousPlayer, currentPlayer;
	public Map<String, TerritoryNode> gameboard = new HashMap<>();
	public List<Player> players;
	public TurnState state;


	public GameState(Player previousTurnFromPlayer) {
		this.previousPlayer = previousTurnFromPlayer;
		players = new ArrayList<>();
	}

	public void setPlayerTurn() {
		previousPlayer = currentPlayer;
		if (previousPlayer == null) {
			currentPlayer = players.get(0);
			return;
		}
		boolean next = false;
		for (Player p : players) {
			if (!next) {
				if (p.equals(previousPlayer)) {
					next = true;
				}
			} else {
				currentPlayer = p;
				return;
			}
		}
		currentPlayer = players.get(0);
		return;
	}

	public void addPlayer(Player player) {
		if (players.contains(player)) {
			System.out.println("Player Already added");
		} else {
			players.add(player);
		}
	}

	public void createBoard() {
		int size = 10;
		for (int i = 0; i < size; i++) {
			String id = Integer.toString(i);
			TerritoryNode tn = new TerritoryNode(id, i % 4 * 50, i / 4 * 50);
			gameboard.put(id, tn);
		}
		connectNodes();

		// bind last to first

	}

	private void connectNodes() {
		for (TerritoryNode a : gameboard.values()) {
			List<TerritoryNode> nodes = new ArrayList<>(gameboard.values());
			nodes.sort(new Comparator<TerritoryNode>() {
				@Override
				public int compare(TerritoryNode o1, TerritoryNode o2) {
					return a.distanceToNode(o1) - a.distanceToNode(o2);
				}
			});
			for (int i = 0; i < 3; i++) {
				a.addNeighbourNode(nodes.get(i));
			}

		}
	}

	@Deprecated
	public void printBoard() {
		for (TerritoryNode tn : gameboard.values()) {
			System.out.println(tn.toString());
		}
	}

	public void printGui() {
		int line = 0;
		for (TerritoryNode tn : gameboard.values()) {
			if (tn.coordinateY != line) {
				line = tn.coordinateY;
				System.out.print("\n");
			}
			System.out.print("[" + tn.getOwner().name.toCharArray()[0] + tn.id + "{" + tn.getArmiesAmount() + "}]");
		}
		System.out.println("");
	}

	public void assignTerritories() {
		ArrayList<TerritoryNode> notAssignedNodes = new ArrayList<>(gameboard.values());
		while (notAssignedNodes.size() > 0) {
			for (Player p : players) {
				if (!notAssignedNodes.isEmpty()) {
					TerritoryNode tn = notAssignedNodes.get(0);
					tn.changeOwner(p);
					notAssignedNodes.remove(0);
				} else {
					System.out.println("DONE");
					break;
				}
			}
		}
		System.out.println("All assigned");
		// printBoard();
	}

	public Collection<TerritoryNode> getTeritoryNodes() {
		return gameboard.values();
	}

	public Player getCurrentTurnPlayer() {
		return currentPlayer;
	}

	public JSONObject getJSON() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("GameName", "Test");
			JSONArray arrayPlayers = new JSONArray();
			jo.put("players", Player.generateJsonArray(players));
			jo.put("terrytories", TerritoryNode.generateJsonArray(gameboard));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jo;
	}
}
