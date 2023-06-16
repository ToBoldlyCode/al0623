package com.example.rentalagreement;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.math.BigDecimal;

import javax.xml.parsers.*;
import javax.xml.xpath.*;
import java.util.logging.*;

public class XMLParser {
	
	private static final Logger logger = Logger.getLogger(XMLParser.class.getName());
	
	public static Tool findToolRecordByToolCode(Tool tool, String inputToolCode) {
		try {
            File inputFile = new File("tool.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputFile);

            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();
            String expression = "/dataset/record[translate(Code, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz') = translate('" + inputToolCode + "', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')]";
            NodeList nodeList = (NodeList) xpath.evaluate(expression, document, XPathConstants.NODESET);

            if (nodeList.getLength() > 0) {
                Element recordElement = (Element) nodeList.item(0);
                String toolType = recordElement.getElementsByTagName("Type").item(0).getTextContent();
                String toolBrand = recordElement.getElementsByTagName("Brand").item(0).getTextContent();
                tool.setCode(inputToolCode);
                tool.setType(toolType);
                tool.setBrand(toolBrand);
            } else {
                System.out.println("No match found for code: " + inputToolCode);
            }
        } catch (Exception e) {
			logger.log(Level.WARNING, "An exception occurred: " + e.getMessage());
        }
		
		return tool;
	}


	public static ToolRentalCharge findToolRentalChargeByToolType(ToolRentalCharge toolRentalCharge, String inputToolType) {
		try {
            File inputFile = new File("tool_rental_charge.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputFile);

            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();
            String expression = "/dataset/record[translate(type, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz') = translate('" + inputToolType + "', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')]";
            NodeList nodeList = (NodeList) xpath.evaluate(expression, document, XPathConstants.NODESET);

            if (nodeList.getLength() > 0) {
                Element recordElement = (Element) nodeList.item(0);
                BigDecimal dailyCharge = new BigDecimal(recordElement.getElementsByTagName("daily_charge").item(0).getTextContent());
                Boolean weekdayCharge = Boolean.parseBoolean(recordElement.getElementsByTagName("weekday_charge").item(0).getTextContent());
                Boolean weekendCharge = Boolean.parseBoolean(recordElement.getElementsByTagName("weekend_charge").item(0).getTextContent());
                Boolean holidayCharge = Boolean.parseBoolean(recordElement.getElementsByTagName("holiday_charge").item(0).getTextContent());
                toolRentalCharge.setToolType(inputToolType);
                toolRentalCharge.setDailyCharge(dailyCharge);
                toolRentalCharge.setWeekdayCharge(weekdayCharge);
                toolRentalCharge.setWeekendCharge(weekendCharge);
                toolRentalCharge.setHolidayCharge(holidayCharge);
            } else {
                System.out.println("No match found for tool type: " + inputToolType);
            }
        } catch (Exception e) {
			logger.log(Level.WARNING, "An exception occurred: " + e.getMessage());
        }
		
		return toolRentalCharge;
	}
}






