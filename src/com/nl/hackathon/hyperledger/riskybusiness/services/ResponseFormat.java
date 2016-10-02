package com.nl.hackathon.hyperledger.riskybusiness.services;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class ResponseFormat {
	public static String createResponse(JSONObject data, int returncode, String errormessage, String resultmessage) {
		JSONObject res = new JSONObject();
		try {
			res.put("returncode", returncode);
			res.put("errormessage", errormessage);
			res.put("resultmessage", resultmessage);
			res.put("data", data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return res.toString();
	}

	/**
	 * Positive respone
	 * 
	 * @param data
	 * @param resultmessage
	 * @return
	 */
	public static String createResponse(JSONObject data, String resultmessage) {
		return createResponse(data, 200, "", resultmessage);
	}

	/**
	 * negative respone
	 * 
	 * @param data
	 * @param resultmessage
	 * @return
	 */
	public static String createResponse(int returncode, String errormessage) {
		return createResponse(null, returncode, errormessage, "");
	}
}
