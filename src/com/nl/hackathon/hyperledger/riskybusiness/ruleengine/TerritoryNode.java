package com.nl.hackathon.hyperledger.riskybusiness.ruleengine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Created by Tyc on 01/10/2016.
 */
public class TerritoryNode {
	public final String id;
	public final int coordinateX, coordinateY; // gui

	private Player owner;
	private int armiesAmount = 4;

	private List<TerritoryNode> neighbours = new ArrayList<>();

	/**
	 * @param id
	 * @param coordinateX
	 * @param coordinateY
	 */
	public TerritoryNode(String id, int coordinateX, int coordinateY) {
		this.id = id;
		this.coordinateX = coordinateX;
		this.coordinateY = coordinateY;
	}

	/**
	 * Add neighbour if not already neighbour
	 *
	 * @param territoryNode
	 */
	public void addNeighbourNode(TerritoryNode territoryNode) {
		for (TerritoryNode tn : neighbours) {
			if (tn.id.equals(territoryNode.id)) {
				//System.out.println("NeighbourNode already added");
				return;
			}
		}
		neighbours.add(territoryNode);
		territoryNode.addNeighbourNode(this);
	}

	@Override
	public String toString() {

		String nodes = "Neighbours: ";
		for (TerritoryNode tn : neighbours) {
			nodes += ", " + tn.id;
		}

		return "TerritoryNode{" + "id='" + id + '\'' + ", coordinateX=" + coordinateX + ", coordinateY=" + coordinateY
				+ ", owner=" + owner + ", armiesAmount=" + armiesAmount + ", neighbours: " + nodes + '}';
	}

	/**
	 * True if node is neighbour
	 *
	 * @param node
	 *            potential neighbour
	 * @return
	 */
	public boolean isNextToTerritory(TerritoryNode node) {
		for (TerritoryNode tn : neighbours) {
			if (tn.equals(node)) {
				return true;
			}
		}
		return false;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public int getArmiesAmount() {
		return armiesAmount;
	}

	public void incrementArmiesAmount(int n) {
		armiesAmount += n;
	}

	public void killAllArmies() {
		armiesAmount = 0;
	}

	public void changeOwner(Player owner) {
		this.owner = owner;
	}

	public List<TerritoryNode> getNeighbours() {
		return neighbours;
	}

	public int distanceToNode(TerritoryNode a) {
		return (int) Math.sqrt(Math.pow(a.coordinateX - coordinateX, 2) + Math.pow(a.coordinateY - coordinateY, 2));
	}

	public static JSONArray generateJsonArray(Map<String, TerritoryNode> gameboard) throws JSONException {
		// TODO Auto-generated method stub
		JSONArray ja = new JSONArray();
		for (TerritoryNode tn : gameboard.values()) {
			JSONObject jo = new JSONObject();
			jo.put("id", tn.id);
			jo.put("x", tn.coordinateX);
			jo.put("y", tn.coordinateY);
			if (tn.getOwner() != null) {
				jo.put("owner", tn.getOwner().color);
				jo.put("ownerN", tn.getOwner().name);
			} else {
				jo.put("owner", "NULL");
			}
			jo.put("numberOfTroops", tn.armiesAmount);

			JSONArray nja = new JSONArray();
			for (TerritoryNode ntn : tn.neighbours) {
				nja.put(ntn.id);
			}
			jo.put("adjacentFields", nja);
			ja.put(jo);
		}
		return ja;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj.getClass().equals(TerritoryNode.class)) {
			return id.equals(((TerritoryNode) obj).id);
		} else {
			return false;
		}
	}
}
