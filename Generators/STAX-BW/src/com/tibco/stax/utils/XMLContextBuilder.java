package com.tibco.stax.utils;

import java.io.*;
import javax.xml.namespace.QName;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.*;
import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;

import javanet.staxutils.helpers.*;
import javanet.staxutils.*;

/**
 * <p>Title: XMLContextBuilder</p>
 * <p>Description: This class uses a StaxUtils utility
 * for recording element information in a stack to keep track of the current element.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: TIBCO Software Inc.</p>
 * @author Jorge A. Flores
 * @version 1.0
 */

public class XMLContextBuilder {
  private ElementContext CONTEXT = null;
  public XMLContextBuilder() {

  }

  public void addContext(QName contextName) {
    if (CONTEXT == null) {
      CONTEXT = new ElementContext(contextName);
    }
    else if (!isRepeatElement(contextName)) {
      CONTEXT = CONTEXT.newSubContext(contextName);
    }
   System.out.println("CONTEXT::::" + CONTEXT.getPath());
  }

  public void removeContext(QName contextName) {
    if (CONTEXT!= null) {
      CONTEXT = CONTEXT.getParentContext();
    }
  }

  public boolean isRepeatElement(QName contextName) {
    String fullContextPath = CONTEXT.getPath();
    String contextStr = "/" + contextName.toString() + "/";

    if (fullContextPath.indexOf(contextStr) == -1) {
      return false;
    }
    return true;

  }

  public ElementContext getContext() {
    return CONTEXT;
  }

}