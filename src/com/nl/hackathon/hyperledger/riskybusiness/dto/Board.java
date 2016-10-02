/**
 * 
 */
package com.nl.hackathon.hyperledger.riskybusiness.dto;

/**
 * @author C31931
 *
 */
public class Board {

	private String id;
	private String x;
	private String y;
	private String owner;
	private int numberOfTroops;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the x
	 */
	public String getX() {
		return x;
	}
	/**
	 * @param x the x to set
	 */
	public void setX(String x) {
		this.x = x;
	}
	/**
	 * @return the y
	 */
	public String getY() {
		return y;
	}
	/**
	 * @param y the y to set
	 */
	public void setY(String y) {
		this.y = y;
	}
	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}
	/**
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}
	/**
	 * @return the numberOfTroops
	 */
	public int getNumberOfTroops() {
		return numberOfTroops;
	}
	/**
	 * @param numberOfTroops the numberOfTroops to set
	 */
	public void setNumberOfTroops(int numberOfTroops) {
		this.numberOfTroops = numberOfTroops;
	}
}
