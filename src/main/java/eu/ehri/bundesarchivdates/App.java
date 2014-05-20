package eu.ehri.bundesarchivdates;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

/**
 * Application to normalize the date format used in the EAD provided by Bundesarchiv.
 * Standard used for the normalization is ISO 8601 
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

		String normalized_date;

		while (xmlEventReaderEAD.hasNext()) {

			XMLEvent event = xmlEventReaderEAD.nextEvent();

			if (event.isStartElement()) {
				if (event.asStartElement().getName().getLocalPart()
						.equals("unitdate")) {
					writer.add(event);
					event = xmlEventReaderEAD.nextEvent();
					if (event instanceof Characters) {
						String date_field = event.asCharacters().toString();
						normalized_date = NormalizeDate.processDate(date_field);
						writer.add(eventFactory.createCharacters(normalized_date));	
					} else {
						writer.add(event);
					}
				} else {
					writer.add(event);
				}
			}else{
				writer.add(event);
			}
			
		}
		fileInputStreamEAD.close();
		xmlEventReaderEAD.close();
		writer.close();
	}
}
