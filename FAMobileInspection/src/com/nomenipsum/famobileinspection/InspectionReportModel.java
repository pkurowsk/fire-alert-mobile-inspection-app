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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.os.Environment;

/**
 * Singleton class that manages InspectionData.xml and deals with saving,
 * finding nodes, and reading the report.
 * 
 * @author Philip
 *
 */
public class InspectionReportModel {
	
	private static InspectionReportModel instance;
	
	Document document;
	Node currentNode;
	
	private String reportDir = Environment.getExternalStorageDirectory().toString() + "/savedReports";
	private String reportName = "InspectionData.xml";
	
	File report;
	public InspectionReportModel()	{
		report = new File(reportDir, reportName);
		
		try {
	    	if (!report.exists())
	    		report.createNewFile();
	    	
	    	InputStream is= new FileInputStream(report.getPath());
	    	
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder docBuilder;

			docBuilder = docBuilderFactory.newDocumentBuilder();
			document = docBuilder.parse(is);
			
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
	
	/**
	 * Searches through the inspection report for a node that is a child of Room.
	 * 
	 * @param id
	 * @param setAsCurrent
	 * @return
	 */
	public Node FindEquipment(String id, boolean setAsCurrent)	{
		if (document == null)
			return null;
		
		NodeList results = document.getElementsByTagName("Room");
		
		for (int i = 0; i < results.getLength(); i++)	{
			Node child = results.item(i);
			
			if (child.getNodeType() == Node.ELEMENT_NODE)	{
				NodeList children = child.getChildNodes();
				
				for (int j = 0; j < children.getLength(); j++)	{
					Node equipment = children.item(j);
					
					if (equipment.getNodeType() == Node.ELEMENT_NODE)	{
						NamedNodeMap attrs = equipment.getAttributes();
						
						if (attrs.getNamedItem("id").getTextContent().equals(id))	{
							if (setAsCurrent)
								currentNode = equipment;
							return equipment;
							
						}
					}
				}
				
			}
		}
		
		return null;
		
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
	public static InspectionReportModel getInstance()	{
		if (instance == null)
			instance = new InspectionReportModel();
			
		return instance;
	}
	
	public String getReportAsString()	{
		try	{
		    Transformer transformer = TransformerFactory.newInstance().newTransformer();
		    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	
		    // Initialise StreamResult with File object to save to file
		    StreamResult result = new StreamResult(new StringWriter());
		    DOMSource source = new DOMSource(document);
		    transformer.transform(source, result);
		    
		    // Write transformed xml file
		    String xmlReport = result.getWriter().toString();
		    
		    return xmlReport;
		}
		catch (TransformerException e)	{
			e.printStackTrace();
		}
		
		return null;
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
		    FileWriter fileIO = new FileWriter(report);
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
	
	/**
	 * Sets the reports current node up one level
	 */
	public void TraverseUp()	{
		currentNode = currentNode.getParentNode();
	}

}
