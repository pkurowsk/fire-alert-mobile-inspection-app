package com.nomenipsum.famobileinspection;

import org.w3c.dom.Node;

public class Account {
	private static Account _instance;
	String fName, lName, id;
	boolean isAdmin;
	
	public static Account getInstance()	{
		if (_instance == null)
			_instance = new Account();
		
		return _instance;
	}
	
	public Account()	{
		
	}
	
	public Account(String fName, String lName, String id)	{
		this.fName = fName;
		this.lName = lName;
		this.id = id;
	}
	
	/**
	 * Initializes the accountholder's first name, last name, and id with a Node
	 * @param n : A "user" node read from UserAccounts.xml
	 */
	public void init(Node n)	{
		fName = n.getAttributes().getNamedItem("firstname").getTextContent();
		lName = n.getAttributes().getNamedItem("lastname").getTextContent();
		id = n.getAttributes().getNamedItem("id").getTextContent();
		
		if (n.getAttributes().getNamedItem("adminFlag").getTextContent().equals("1"))
			isAdmin = true;
		else
			isAdmin = false;
		
	}
	
	/**
	 * 
	 * @return the current user's ID
	 */
	public String getID()	{
		return id;
	}
	
	/**
	 * 
	 * @return : the current user's first name
	 */
	public String getFName()	{
		return fName;
	}

}
