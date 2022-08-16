package com.mule.connector.snmp.api.vo;

import java.io.Serializable;
import org.mule.runtime.api.util.MultiMap;
import org.mule.runtime.extension.api.annotation.param.Parameter;

public class SNMPListenerAttributes implements Serializable
{

		  public static final String TAB = "   ";
		  public static final String DOUBLE_TAB = TAB + TAB;

		  /**
		   * Map of SNMP headers in the message. Former properties.
		   */
		  @Parameter
		  protected MultiMap<String, String> headers;
		  
		  /*

		  public HttpAttributes(MultiMap<String, String> headers) {
		    this.headers = headers.toImmutableMultiMap();
		  }

		  public MultiMap<String, String> getHeaders() {
		    return headers;
		  }

		  @Override
		  public String toString() {
		    return ReflectionToStringBuilder.toString(this, SHORT_PREFIX_STYLE);
		  }

		  private static String formatHttpAttributesMapsToString(String name, Stream<Map.Entry<String, String>> stream) {
		    StringBuilder builder = new StringBuilder();
		    builder.append(TAB).append(name).append("=[").append(lineSeparator());
		    stream.forEach(element -> builder.append(DOUBLE_TAB)
		        .append(element.getKey()).append("=").append(obfuscateValueIfNecessary(element)).append(lineSeparator()));
		    builder.append(TAB).append("]").append(lineSeparator());
		    return builder.toString();
		  }

		  private static String obfuscateValueIfNecessary(Map.Entry<String, String> entry) {
		    String key = entry.getKey();
		    if (key.equals("password") || key.equals("pass") || key.contains("secret") || key.contains("authorization")) {
		      return "****";
		    } else {
		      return entry.getValue();
		    }
		  }

		  public static StringBuilder buildMapToString(Map map, String name, Stream stream, StringBuilder builder) {
		    if (map.isEmpty()) {
		      builder.append(TAB).append(name).append("=[]").append(lineSeparator());
		      return builder;
		    }
		    builder.append(formatHttpAttributesMapsToString(name, stream));
		    return builder;
		  }
		*/
	
		
}
