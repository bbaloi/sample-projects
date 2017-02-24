package com.tibco.stax.parser;

import java.io.*;
import javax.xml.namespace.QName;
import javax.xml.stream.events.*;
import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;

import com.tibco.stax.action.jms.MessageProducer;
import com.tibco.stax.utils.XMLContextBuilder;

import java.util.Vector;

import javanet.staxutils.helpers.*;
import javanet.staxutils.*;


/**
 * <p>Title: XMLFileStaxParser</p>
 * <p>Description: This class uses a Stax API
     * to process large XML documents in chunks. The key assumption with this parser
 * is that the order in which the target elements are declared must match
 * the order in which they are expected to appear in the input
 * XML document.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: TIBCO Software Inc.</p>
 * @author Jorge A. Flores
 * @version 1.0
 */

public class XMLFileStaxParser {
  /* Do Not Modify Below Here */
  private static final int FILE_OUTPUT_MODE = 1;
  private static final int JMS_OUTPUT_MODE = 2;
  /* Do Not Modify Above Here */

  private static java.util.Vector TARGET_ELEMENTS = null;
  private static java.util.Vector HEADER_ELEMENTS = new java.util.Vector();

  private static XMLEventReader READER = null;
  private static XMLContextBuilder CONTEXT_BUILDER = null;
  private static QName CURRENT_TARGET = null;
  private static ElementContext CURRENT_TARGET_CONTEXT = null;
  private static StringBuffer XML_OUTPUT = null;

  public static int OUTPUT_MODE = 1;

  private static final String FILE_WRITE_PATH =
      "C:\\Data\\TIBCO\\Projects\\SRP\\Integration Systems\\AMR\\OutputXML\\";

  public void initialize(int outputMode) {

    // Use  the reference implementation for the  XML input factory
    System.setProperty(
        "javax.xml.stream.XMLInputFactory",
        "com.bea.xml.stream.MXParserFactory");

    setContextBuilder();

    OUTPUT_MODE = outputMode;

  }

  public void processXMLFile(String inputFile) {
    try {
      // Create the XML input factory
      XMLInputFactory factory = XMLInputFactory.newInstance();

      // Create XML event reader
      READER =
          factory.createXMLEventReader(new FileReader(inputFile));

      setXMLDeclaration();

      while (!getTargetElements().isEmpty()) {
        //Now call start the iterative handling of file
        QName targetElement = (QName) getTargetElements().firstElement();
        setCurrentTarget(targetElement);
        this.processXMLElement();

      }
    }

    catch (FileNotFoundException ex) {
      ex.printStackTrace();
    }
    catch (java.io.IOException ex) {
      ex.printStackTrace();
    }
    catch (XMLStreamException ex) {
      ex.printStackTrace();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

  }

  private void processXMLElement() throws FileNotFoundException,
      XMLStreamException {

    int instanceCount = 0;

    while (READER.hasNext()) {

      /* Peek
       * Break out of this loop if we encounter the next target element
       * which means were are finished with the current one
       */
      if (isTargetFinished()) {
        return;
      }

      // Read event to be written to result document
      XMLEvent event = (XMLEvent) READER.next();
      QName thisElement = null;

      switch (event.getEventType()) {
        case XMLEvent.START_ELEMENT:

          //Add to Context tree
          thisElement = event.asStartElement().getName();
          getContextBuilder().addContext(thisElement);

          //Process all instances of target element
          if (isCurrentTarget(thisElement) && isValidContext(thisElement)) {
            instanceCount++;
            CURRENT_TARGET_CONTEXT = getContextBuilder().getContext();
            outputXMLElement(instanceCount, event);
          }
          break;
        case XMLEvent.END_ELEMENT:

          //Remove From Context tree
          thisElement = event.asEndElement().getName();
          getContextBuilder().removeContext(thisElement);
          break;

      }
    }
  }

  /*
   * Output to either File Disk or JMS Queue
   */

  private void outputXMLElement(int instanceCount, XMLEvent event) throws
      FileNotFoundException,
      XMLStreamException {

    //Output defaulted to File Writes
    switch (OUTPUT_MODE) {
      case FILE_OUTPUT_MODE:
        xmlElementToFile(instanceCount, event);
        break;

      case JMS_OUTPUT_MODE:
        xmlElementToJms(event);
        break;
    }

  }

  private void xmlElementToFile(int count, XMLEvent event) throws
      FileNotFoundException, XMLStreamException {

    String outputFile = FILE_WRITE_PATH + count + "_" +
        CURRENT_TARGET.toString() + ".xml";

    // Create XML event writer
    XMLEventWriter writer = this.getXMLWriter(outputFile);

    //Write the first instance event for current target
    writer.add(event);

    //Process all contents of current target element
    while (READER.hasNext()) {

      XMLEvent thisEvent = (XMLEvent) READER.next();

      if (thisEvent.isEndElement() &&
          isCurrentTarget(thisEvent.asEndElement().getName())) {

        getContextBuilder().removeContext(thisEvent.asEndElement().getName());

        writer.add(thisEvent);
        writer.close();
        writer.flush();
        return;
      }
      else {
        writer.add(thisEvent);
      }
    }
  }

  private void xmlElementToJms(XMLEvent event) {

    //Write the first instance event for current target
    appendXMLOutput(event);

    //Process all contents of current target element
    while (READER.hasNext()) {

      XMLEvent thisEvent = (XMLEvent) READER.next();

      if (thisEvent.isEndElement() &&
          isCurrentTarget(thisEvent.asEndElement().getName())) {

        getContextBuilder().removeContext(thisEvent.asEndElement().getName());

        appendXMLOutput(thisEvent);
        sendXMLtoJmsMessage();
        return;
      }
      else {
        appendXMLOutput(thisEvent);
      }
    }
  }

  private void setXMLDeclaration() {
    // Read the first event to be written to each result document
    XMLEvent event = (XMLEvent) READER.next();
    if (event.getEventType() == XMLEvent.START_DOCUMENT) {
      HEADER_ELEMENTS.add(0, event);
    }
  }

  private void setContextBuilder() {
    CONTEXT_BUILDER = new XMLContextBuilder();
  }

  public XMLContextBuilder getContextBuilder() {
    return CONTEXT_BUILDER;
  }

  public void addElementToTarget(String elementName) {
    if (TARGET_ELEMENTS == null) {
      TARGET_ELEMENTS = new java.util.Vector();
    }
    QName target = new QName(elementName);
    TARGET_ELEMENTS.add(target);
  }

  public Vector getTargetElements() {
    return TARGET_ELEMENTS;
  }

  private void setCurrentTarget(QName elementName) {
    if (getTargetElements() != null) {
      getTargetElements().remove(elementName);
    }
    //Reset Current Context
    CURRENT_TARGET = elementName;
    CURRENT_TARGET_CONTEXT = null;
    System.out.println("CURRENT_TARGET::::" + CURRENT_TARGET);
  }

  public boolean isCurrentTarget(QName elementName) {
    boolean isCurrent = false;
    if (CURRENT_TARGET.equals(elementName)) {
      isCurrent = true;
    }
    return isCurrent;

  }

  public boolean isNextTarget(QName elementName) {
    boolean isNext = false;
    if (getTargetElements().contains(elementName)) {
      isNext = true;
    }
    return isNext;

  }

  private boolean isValidContext(QName elementName) {
    boolean isValid = true;
    ElementContext context = getContextBuilder().getContext().getParentContext();
    QName parentName = context.getName();
    if (isNextTarget(parentName)) {
      isValid = false;
    }
    System.out.println("isValidContext()::::" + isValid);
    return isValid;
  }

  private boolean isTargetFinished() throws XMLStreamException {
    XMLEvent peek = READER.peek();
    boolean isComplete = false;
    ElementContext context = getContextBuilder().getContext();

    //If context hasnt been set, then we are in the first event
    //and so current target is not complete
    if (context == null || CURRENT_TARGET_CONTEXT == null) {
      return isComplete;
    }

    if (peek.isStartElement()) {
      QName peekName = peek.asStartElement().getName();
      context = context.newSubContext(peekName);
      System.out.println("CURRENT_TARGET_CONTEXT::::" +
                         CURRENT_TARGET_CONTEXT.getPath() +
                         " at Depth=" + CURRENT_TARGET_CONTEXT.getDepth());
      System.out.println("CURRENT_PEEK_CONTEXT::::" + context.getPath() +
                         " at Depth=" + context.getDepth());

      if ( (CURRENT_TARGET_CONTEXT.getDepth() - context.getDepth()) == 2) {
        isComplete = true;
      }

    }
    return isComplete;
  }

  private XMLEventWriter getXMLWriter(String fileName) throws
      FileNotFoundException, XMLStreamException {

    // Create the output factory
    XMLOutputFactory xmlof = XMLOutputFactory.newInstance();

    FileOutputStream outputStream = new FileOutputStream(fileName);

    XMLEventWriter writer = xmlof.createXMLEventWriter(outputStream);

    //Write document header
    writer.add( (XMLEvent) HEADER_ELEMENTS.get(0));

    //Convert to pretty-print writer
    IndentingXMLEventWriter iw = new IndentingXMLEventWriter(writer);
    iw.setIndent("    ");

    return iw;

  }

  /*
   * This method used to build XML string sent to JMS Queue
   */
  public void appendXMLOutput(XMLEvent xml) {
    if (XML_OUTPUT == null) {
      XML_OUTPUT = new java.lang.StringBuffer();

      //Add document header
      XMLEvent headerEvent = (XMLEvent) HEADER_ELEMENTS.get(0);
      XML_OUTPUT = XML_OUTPUT.append(headerEvent.toString());
    }
    XML_OUTPUT = XML_OUTPUT.append(xml.toString());
  }

  public void sendXMLtoJmsMessage() {

    String url = "tcp://localhost:7222";
    String userName = "tibco";
    String password = "tibco";
    String queueName = "testing.queue";
    MessageProducer msgProducer = new MessageProducer(url, userName, password);
    msgProducer.createJmsQueueMessage(XML_OUTPUT.toString(),queueName);

    //Reset Output
    XML_OUTPUT = null;
  }

  public static void main(String[] args) {
    int outputMode = 2;
    XMLFileStaxParser handler = new XMLFileStaxParser();
    handler.initialize(outputMode);
    String inputFile = "C:\\Data\\TIBCO\\Projects\\SRP\\Integration Systems\\AMR\\InputXML\\srpcus_test.xml";

    handler.addElementToTarget("Meter");
    //handler.addElementToTarget("MeterReadings");


    handler.processXMLFile(inputFile);
  }

}
