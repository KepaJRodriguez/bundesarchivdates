package eu.ehri.bundesarchivdates;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;


/**
 * Application to normalize the date format used in the EAD provided by
 * Bundesarchiv. Standard used for the normalization is ISO 8601
 * 
 */

public class App {
	public static void main(String[] args) throws IOException,
			XMLStreamException {

		String eadfile = args[0];
		String outputfile = eadfile.replace(".xml", "_datenormalized.xml");

		FileInputStream fileInputStreamEAD = new FileInputStream(eadfile);
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		XMLEventWriter writer = factory.createXMLEventWriter(new FileWriter(
				outputfile));
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();

		XMLEventReader xmlEventReaderEAD = XMLInputFactory.newInstance()
				.createXMLEventReader(fileInputStreamEAD);

		// String normalized_date;
		HashMap<Integer, String> original_date = null;
		HashMap<Integer, String> normalized_date = null;

		//for provenance
		boolean hasrevdesc = LookForRevisiondesc.hasRevisiondesc(eadfile);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		
		boolean bestandslaufzeit = false;
		XMLEvent end = eventFactory.createDTD("\n");
		
		
		
		while (xmlEventReaderEAD.hasNext()) {
			XMLEvent event = xmlEventReaderEAD.nextEvent();

			
			if (hasrevdesc == true) {
				if (event.isStartElement()) {
					if (event.asStartElement().getName().getLocalPart()
							.equals("revisiondesc")) {
						writer.add(end);
						writer.add(eventFactory.createStartElement("", null,
								"change"));
						writer.add(end);
						writer.add(eventFactory.createStartElement("", null,
								"date"));
						writer.add(eventFactory.createCharacters(dateFormat.format(date)));
						writer.add(eventFactory.createEndElement("", null,
								"date"));
						writer.add(end);
						writer.add(eventFactory.createStartElement("", null,
								"item"));
						writer.add(eventFactory
								.createCharacters("EHRI added a unitid with label \"ehri_cleaned_date\" to include "
										+ "normalized version of chunks of the unitdate Bestandskatalog. "
										+ "Dates have been normalized following ISO-8601. Normalized date will be used for"
										+ " data selection purposes and for functions of the EHRI portal"));
						writer.add(eventFactory.createEndElement("", null,
								"item"));
						writer.add(end);
						writer.add(eventFactory.createEndElement("", null,
								"change"));
						writer.add(end);
					}
				}
			}

			if (hasrevdesc == false) {
				if (event.isEndElement()) {
					if (event.asEndElement().getName().getLocalPart()
							.equals("filedesc")) {
						writer.add(end);
						writer.add(eventFactory.createStartElement("", null,
								"revisiondesc"));
						writer.add(end);
						writer.add(eventFactory.createStartElement("", null,
								"change"));
						writer.add(end);
						writer.add(eventFactory.createStartElement("", null,
								"date"));
						writer.add(eventFactory.createCharacters(dateFormat.format(date)));
						writer.add(eventFactory.createEndElement("", null,
								"date"));
						writer.add(end);
						writer.add(eventFactory.createStartElement("", null,
								"item"));
						writer.add(eventFactory
								.createCharacters("EHRI added a unitid with label \"ehri_cleaned_date\" to include "
										+ "normalized version of chunks of the unitdate Bestandskatalog. "
										+ "Dates have been normalized following ISO-8601. Normalized date will be used for"
										+ " data selection purposes and for functions of the EHRI portal"));
						writer.add(eventFactory.createEndElement("", null,
								"item"));
						writer.add(end);
						writer.add(eventFactory.createEndElement("", null,
								"change"));
						writer.add(end);
						writer.add(eventFactory.createEndElement("", null,
								"revisiondesc"));
					}
				}
			}
			
			
			
			
			if (event.isStartElement()) {
				if (event.asStartElement().getName().getLocalPart()
						.equals("unitdate")) {
					@SuppressWarnings("unchecked")
					Iterator<Attribute> attributes = event.asStartElement()
							.getAttributes();
					while (attributes.hasNext()) {
						if (attributes.next().getValue().toString()
								.equals("Bestandslaufzeit")) {
							bestandslaufzeit = true;
							original_date = new HashMap<Integer, String>();
							normalized_date = new HashMap<Integer, String>();
						}
					}
					writer.add(event);
					event = xmlEventReaderEAD.nextEvent();
					if (event instanceof Characters) {
						if (bestandslaufzeit == true) {
							String date_field = event.asCharacters().toString();
							original_date = ProcessDates.splitDate(date_field);
							normalized_date = ProcessDates.normalizeDate(original_date);
							writer.add(event);						
							event = xmlEventReaderEAD.nextEvent();
							if(event.asEndElement().getName().getLocalPart().equals("unitdate")){
								writer.add(event);
								writer.add(end);
								for(int key : normalized_date.keySet()){
								writer.add(eventFactory.createStartElement("", null,
										"unitdate"));
								writer.add(eventFactory.createAttribute("encodinganalog",
										"Bestandslaufzeit"));
								writer.add(eventFactory.createAttribute("label",
										"ehri_cleaned_date"));
								writer.add(eventFactory.createAttribute("normal",
										normalized_date.get(key)));
								writer.add(eventFactory.createCharacters(original_date.get(key)));
								writer.add(eventFactory.createEndElement("", null, "unitdate"));
								writer.add(end);
								}
								event = xmlEventReaderEAD.nextEvent();
							}
						}
						writer.add(event);
					} else {
						writer.add(event);
					}
				} else {
					writer.add(event);
				}
			} else {
				writer.add(event);
			}

		}
		fileInputStreamEAD.close();
		xmlEventReaderEAD.close();
		writer.close();
	}
}
