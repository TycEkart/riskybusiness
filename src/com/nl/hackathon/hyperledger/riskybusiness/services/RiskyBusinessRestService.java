/**
 *
 */
package com.nl.hackathon.hyperledger.riskybusiness.services;

import com.nl.hackathon.hyperledger.riskybusiness.dto.RiskyBusinessDTO;
import com.nl.hackathon.hyperledger.riskybusiness.ruleengine.GameState;
import com.nl.hackathon.hyperledger.riskybusiness.ruleengine.GameStateCaretaker;
import com.nl.hackathon.hyperledger.riskybusiness.ruleengine.Player;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * @author C31931
 */
@Path("riskybusiness")
public class RiskyBusinessRestService {

	static Player self = new Player("Thijs", "192.168.0.102", 1);

	/**
	 * This method returns the current state of the game
	 *
	 * @return riskyBusinessDTO
	 */
	@GET
	@Path("createGame")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createGame() {

		GameState gameState;
		gameState = new GameState(null);
		gameState.addPlayer(self);
		gameState.addPlayer(new Player("Jan-Paul", "192.168.0.101", 2));
		gameState.addPlayer(new Player("Aschwin", "192.168.0.?", 3));
		gameState.createBoard();
		gameState.assignTerritories();
		GameStateCaretaker.get_instance().gameState = gameState;
		return Response.status(Status.OK).entity(ResponseFormat.createResponse(gameState.getJSON(), "game created")).build();
	}

	/**
	 * @return
	 */
	@GET
	@Path("test")
	@Produces(MediaType.APPLICATION_JSON)
	public String moveTroops() {

		return "real hellow world";
	}

	/**
	 * This method returns the current state of the game
	 *
	 * @return riskyBusinessDTO
	 */
	@GET
	@Path("getcurrentstate")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCurrentState() {
		GameState gameState = GameStateCaretaker.get_instance().gameState;
		return Response.status(Status.OK).entity(ResponseFormat.createResponse(gameState.getJSON(), "update")).build();
	}

	@GET
	@Path("startturn")
	@Produces(MediaType.APPLICATION_JSON)
	public Response startTurn(@Context final UriInfo ui) {
		/*
		 * RiskyBusinessDTO riskyBusinessDTO = new RiskyBusinessDTO();
		 * riskyBusinessDTO.setReturncode(getErrorCode(Status.OK));
		 * riskyBusinessDTO.setErrormessage(getErrorMesage(Status.OK)); return
		 * Response.status(Status.OK).entity(riskyBusinessDTO).build();
		 */

		return Response.status(Status.OK).entity("Hellowworldd").build();
	}

	@GET
	@Path("attackarea")
	@Produces(MediaType.APPLICATION_JSON)
	public Response attackArea(@Context final UriInfo ui) {
		Map map = new HashMap<>();

		map.put("returncode", getErrorCode(Status.OK));
		map.put("errormessage", getErrorMesage(Status.OK));
		map.put("resultmessage", "");
		map.put("board", gameState);
		map.put("anotherAttackPossible", true);
		return Response.status(Status.OK).entity(map).build();
	}

	@GET
	@Path("movetroops")
	@Produces(MediaType.APPLICATION_JSON)
	public Response moveTroops(@Context final UriInfo ui) {
		Map map = new HashMap<>();
		map.put("returncode", getErrorCode(Status.OK));
		map.put("errormessage", getErrorMesage(Status.OK));
		map.put("resultmessage", "");
		map.put("board", gameState);
		map.put("anotherMovePossible", "");
		return Response.status(Status.OK).entity(map).build();
	}

	@GET
	@Path("endturn")
	@Produces(MediaType.APPLICATION_JSON)
	public Response endTurn(@Context final UriInfo ui) {
		RiskyBusinessDTO riskyBusinessDTO = new RiskyBusinessDTO();
		riskyBusinessDTO.setReturncode(getErrorCode(Status.OK));
		riskyBusinessDTO.setErrormessage(getErrorMesage(Status.OK));
		return Response.status(Status.OK).entity(riskyBusinessDTO).build();
	}

	/**
	 * This method puts the request parameters in a HashMap.
	 *
	 * @param ui
	 *            UriInfo
	 * @return Map<String, String>
	 */
	protected Map<String, String> getAllReqParams(final UriInfo ui) {
		final MultivaluedMap<String, String> reqParams = ui.getQueryParameters();
		final Map<String, String> eventParams = new HashMap<String, String>();
		for (final String key : reqParams.keySet()) {
			eventParams.put(key, reqParams.getFirst(key));
		}
		return eventParams;
	}

	private String getErrorMesage(Status statusMsg) {
		Map<Status, String> msgMap = new HashMap<Status, String>();
		msgMap.put(Status.OK, "Success");
		msgMap.put(Status.INTERNAL_SERVER_ERROR, "Internal Error");
		msgMap.put(Status.BAD_REQUEST, "Bad Request");
		return msgMap.get(statusMsg);
	}

	private Integer getErrorCode(Status statusMsg) {
		Map<Status, Integer> msgMap = new HashMap<Status, Integer>();
		msgMap.put(Status.OK, 200);
		msgMap.put(Status.INTERNAL_SERVER_ERROR, 500);
		msgMap.put(Status.BAD_REQUEST, 400);
		return msgMap.get(statusMsg);
	}
}