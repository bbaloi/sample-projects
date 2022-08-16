package com.perpetual.util;

/**
 * @author Mike Pot - http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.io.*;

import org.jdom.*;
import org.jdom.input.*;


public class ResourceLoader
{
	public static Element loadResourceAsJdomElement(String resource) throws Exception
	{
		return loadResourceAsJdomElement(resource, "iso-8859-1");
	}

	public static Element loadResourceAsJdomElement(String resource, String charset) throws Exception
	{
		SAXBuilder builder = new SAXBuilder(false);
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
		if (is == null)
			is = new FileInputStream(resource);
		if (is == null)
			throw new Exception("Resource not found: " + resource);
		try
		{
			return builder.build(new InputStreamReader(is, charset)).getRootElement();
		}
		finally
		{
			is.close();
		}
	}

//	public static Element loadResourceAsJdomElement(String resource) throws Exception
}


