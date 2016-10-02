package com.nl.hackathon.hyperledger.riskybusiness.ruleengine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Created by Tyc on 01/10/2016.
 */
public class Player {
	public final String name, ip;
	public final int color;

	public Player(String name, String ip, int color) {
		this.name = name;
		this.ip = ip;
		this.color = color;
	}

	@Override
	public String toString() {
		return "Player{" + "name='" + name + '\'' + '}';
	}

	@Override
	public boolean equals(Object obj) {
		if (!obj.getClass().equals(Player.class)) {
			return false;
		} else {
			return ((Player) obj).ip.equals(ip);
		}
	}

	public static JSONArray generateJsonArray(List<Player> players) throws JSONException {
		JSONArray ja = new JSONArray();
		for (Player p : players) {
			JSONObject jo = new JSONObject();
			jo.put("name", p.name);
			jo.put("ip", p.ip);
			jo.put("color", p.color);
			ja.put(jo);
		}
		return ja;
	}
}
