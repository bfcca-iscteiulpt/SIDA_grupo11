package extra;


import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLReader {
	
	private String sybase_ip_server;
	private String sybase_port;
	private String sybase_database;
	private String sybase_user;
	private String sybase_password;
	
	private String mongo_ip_server;
	private String mongo_port;
	private String mongo_database;
	private String mongo_user;
	private String mongo_password;
	
	private String periodicity;
	private String diference;
	
	private String sensor_topic;
	private String sensor_qos;
	private String sensor_broker;
	
	public void readXML1() {
		
		try {
			File XmlFile = new File("conf1.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(XmlFile);
			
			doc.getDocumentElement().normalize();
		
			NodeList listOfSybase = doc.getElementsByTagName("sybase");
			for (int temp = 0; temp < listOfSybase.getLength(); temp++) {
				Node nNode = listOfSybase.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					sybase_ip_server = eElement.getElementsByTagName("ip_server").item(0).getTextContent();
					sybase_port = eElement.getElementsByTagName("port").item(0).getTextContent();
					sybase_database = eElement.getElementsByTagName("database").item(0).getTextContent();
					sybase_user = eElement.getElementsByTagName("user").item(0).getTextContent();
					sybase_password = eElement.getElementsByTagName("password").item(0).getTextContent();
				}
			}
			
			NodeList listOfMongoDB = doc.getElementsByTagName("mongodb");
			for (int temp = 0; temp < listOfMongoDB.getLength(); temp++) {
				Node nNode = listOfMongoDB.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					mongo_ip_server = eElement.getElementsByTagName("ip_server").item(0).getTextContent();
					mongo_port = eElement.getElementsByTagName("port").item(0).getTextContent();
					mongo_database = eElement.getElementsByTagName("database").item(0).getTextContent();
					mongo_user = eElement.getElementsByTagName("user").item(0).getTextContent();
					mongo_password = eElement.getElementsByTagName("password").item(0).getTextContent();
				}
			}
			
			NodeList listOfP = doc.getElementsByTagName("periodicity");
			for (int temp = 0; temp < listOfP.getLength(); temp++) {
				Node nNode = listOfP.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					periodicity = doc.getElementsByTagName("periodicity").item(0).getTextContent();
				}
			}
			
			
			diference = doc.getElementsByTagName("diference").item(0).getTextContent();
//			NodeList listOfD = doc.getElementsByTagName("periodicity");
//			for (int temp = 0; temp < listOfP.getLength(); temp++) {
//				Node nNode = listOfP.item(temp);
//				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//					Element eElement = (Element) nNode;
//					periodicity = doc.getElementsByTagName("periodicity").item(0).getTextContent();
//				}
//			}
			
			
			
			} catch (IOException  e) {
			} catch (ParserConfigurationException e) {
			} catch (SAXException e) {}
	}

	public void readXML2() {
		
		try {
			File XmlFile = new File("conf2.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(XmlFile);
			
			doc.getDocumentElement().normalize();
		
			NodeList listOfSensor = doc.getElementsByTagName("sensor");
			for (int temp = 0; temp < listOfSensor.getLength(); temp++) {
				Node nNode = listOfSensor.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					sensor_topic = eElement.getElementsByTagName("topic").item(0).getTextContent();
					sensor_qos = eElement.getElementsByTagName("qos").item(0).getTextContent();
					sensor_broker = eElement.getElementsByTagName("broker").item(0).getTextContent();
				}
			}
			
			NodeList listOfMongoDB = doc.getElementsByTagName("mongodb");
			for (int temp = 0; temp < listOfMongoDB.getLength(); temp++) {
				Node nNode = listOfMongoDB.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					mongo_ip_server = eElement.getElementsByTagName("ip_server").item(0).getTextContent();
					mongo_port = eElement.getElementsByTagName("port").item(0).getTextContent();
					mongo_database = eElement.getElementsByTagName("database").item(0).getTextContent();
					mongo_user = eElement.getElementsByTagName("user").item(0).getTextContent();
					mongo_password = eElement.getElementsByTagName("password").item(0).getTextContent();
				}
			}
			
			
			} catch (IOException  e) {
			} catch (ParserConfigurationException e) {
			} catch (SAXException e) {}
	}
	
	public  String getSybase_ip_server() {
		return sybase_ip_server;
	}

	public String getSybase_port() {
		return sybase_port;
	}

	public String getSybase_database() {
		return sybase_database;
	}

	public String getSybase_user() {
		return sybase_user;
	}

	public String getSybase_password() {
		return sybase_password;
	}

	public String getMongo_ip_server() {
		return mongo_ip_server;
	}

	public String getMongo_port() {
		return mongo_port;
	}

	public String getMongo_database() {
		return mongo_database;
	}

	public String getMongo_user() {
		return mongo_user;
	}

	public String getMongo_password() {
		return mongo_password;
	}

	public String getPeriodicity() {
		return periodicity;
	}
	
	public String getSensor_topic() {
		return sensor_topic;
	}
	
	public String getSensor_qos() {
		return sensor_qos;
	}
		
	public String getSensor_broker() {
		return sensor_broker;
	}
	
	public double getDiference() {
		return Double.parseDouble(diference);
	}
	
}
