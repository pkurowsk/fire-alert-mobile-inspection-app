package com.nomenipsum.famobileinspection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.os.Environment;

public class UserAcountModel {
	
	private static UserAcountModel instance;
	
	Document document;
	Node currentNode;
	Element rootNode;
	
	private String reportPath = Environment.getExternalStorageDirectory().toString() + "/savedReports/UserAccounts.xml";
	
	public UserAcountModel()	{
		File f = new File(reportPath);
		
		try {
	    	if (!f.exists()){
	    		f.createNewFile();
	    		
	    	}else{
	    	InputStream is= new FileInputStream(f.getPath());
	    	
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder docBuilder;

			docBuilder = docBuilderFactory.newDocumentBuilder();
			document = docBuilder.parse(is);
			rootNode = document.getDocumentElement();
	    	}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		currentNode = document;
	    
	}
	
	/**
	 * Searches through the inspection report for a node.
	 * 
	 * @param tag : The tag / name of the target node
	 * @param key : The value of some attribute fo the target node 
	 * @param setAsCurrent : If true the found node will be set as the report current node
	 * @return The matching node or null if not found
	 */
	
	public Element giveRoot(){
		return rootNode;
	}
	public Node Find(String tag, String key, boolean setAsCurrent)	{
		if (document == null)
			return null;
		
		NodeList results = document.getElementsByTagName(tag);
		
		for (int i = 0; i < results.getLength(); i++)	{
			Node nodeInResults = results.item(i);
			
			if (nodeInResults.getNodeType() == Node.ELEMENT_NODE)	{
				NamedNodeMap attrs = nodeInResults.getAttributes();
				
				for (int j = 0; j < attrs.getLength(); j++)	{
					if (attrs.item(j).getTextContent().equals(key))	{
						if (setAsCurrent)
							currentNode = nodeInResults;
						return nodeInResults;
					}
				}
			}
		}
		
		return null;
	}
	
	public boolean verifyLogin(String username, String pass)	{
		Node userAccount = Find("user", username, false);

		if (userAccount == null || 
				username.equals("") ||
				pass.equals("") ||
				username == null ||
				pass == null)	{
			
			return false;

		}
		
		if (userAccount.getAttributes().getNamedItem("username").getTextContent().equals(username) &&
			userAccount.getAttributes().getNamedItem("pass").getTextContent().equals(pass))	{
			
			Account.getInstance().init(userAccount);
			return true;
		}
		return false;
	}
	
	
	/**
	 * Returns the current node in the report traversal
	 * @return the current node
	 */
	public Node getCurrentNode()	{
		if (currentNode == null)
			currentNode = document;
		
		return currentNode;
	}
	
	public Document getDocument()	{
		
		return document;
	}
	
	
	/**
	 * Returns an instance of the loaded Inspection Report file
	 * 
	 * @return Instance of the InspectionReportController
	 */
	public static UserAcountModel getInstance()	{
		if (instance == null)
			instance = new UserAcountModel();
			
		return instance;
	}
	
	/**
	 * Overwrites the XML file with its modified attributes and elements
	 */
	public boolean SaveReport()	{
		try	{
			// Save XML File to device
		    Transformer transformer = TransformerFactory.newInstance().newTransformer();
		    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	
		    // Initialise StreamResult with File object to save to file
		    StreamResult result = new StreamResult(new StringWriter());
		    DOMSource source = new DOMSource(document);
		    transformer.transform(source, result);
		    
		    // Write transformed xml file
		    String xmlReport = result.getWriter().toString();
		    FileWriter fileIO = new FileWriter(new File(reportPath));
		    fileIO.write(xmlReport);
            fileIO.close();
            
		}
		catch (TransformerException e)	{
			e.printStackTrace();
			
			return false;
		} 
		catch (IOException e) {
			e.printStackTrace();
			
			return false;
		}
		
		// Report saved successfully
		return true;
	}

}
