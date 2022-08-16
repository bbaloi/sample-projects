package com.perpetual.log;

/**
 * @author Mike Pot - http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.*;
import java.util.regex.*;
import java.text.*;

import org.jdom.*;

import com.perpetual.util.Environment;
import com.perpetual.util.patternmatcher.RecordPatternMatcherEngine;
import com.perpetual.util.patternmatcher.IPatternMatcher;
import com.perpetual.util.patternmatcher.IMessagePatternInfo;

import java.util.Collection;

import com.perpetual.util.PerpetualC2Logger;

public class LogRecordFormat
{
   private static PerpetualC2Logger sLog = new PerpetualC2Logger( LogRecordFormat.class );    
   private IPatternMatcher recordMatcherEngine=null;
   
    
	private LogRecordFormat() { }

	/*public LogRecordFormat(Element format) throws Exception
	{
		m_fieldsElement = format.getChild("fields");
		String pattern = m_fieldsElement.getAttributeValue("pattern");
		setPattern(pattern);
		for (Iterator i = m_fieldsElement.getChildren("field").iterator(); i.hasNext(); )
		{
			Element elem = (Element)i.next();
			if ("true".equals(elem.getAttributeValue("skip")))
				continue;
			m_names.add(elem.getAttributeValue("name"));
		}
	}*/
        public LogRecordFormat(Element format) throws Exception
	{
		m_fieldsElement = format.getChild("fields");
                initPatternMatcher();
		for (Iterator i = m_fieldsElement.getChildren("field").iterator(); i.hasNext(); )
		{
			Element elem = (Element)i.next();
			//if ("true".equals(elem.getAttributeValue("skip")))
			//	continue;
                        
                        if ("false".equals(elem.getAttributeValue("skip")))
				continue;
                        m_names.add(elem.getAttributeValue("name"));
		}
	}
     
        private void initPatternMatcher()
        {
             sLog.debug("Instantiating RecordPatternMatcher:");
             recordMatcherEngine = new RecordPatternMatcherEngine(Environment.getRecordPatternConfigPath());    
        }
        
	public void setPattern(String pattern)
	{
//		pattern = pattern.replaceAll("0", "\\\\d+").replaceAll("W", "\\\\S+").replaceAll(" ", "\\\\s+");
		m_pattern = Pattern.compile(pattern);
	}

	public Iterator iterateFieldNames() { return m_names.iterator(); }

	/*public Map parseLogRecord(String line) throws Exception
	{
		Map map = new LinkedHashMap();
                Matcher matcher = m_pattern.matcher(line);
		if (!matcher.matches()) throw new Exception("Parse error: " + line);
 
		int j = 1;
		for (Iterator i = m_fieldsElement.getChildren("field").iterator(); i.hasNext(); j++)
		{
			Element elem = (Element)i.next();
			if ("true".equals(elem.getAttributeValue("skip")))
				continue;
			String type = elem.getAttributeValue("type");
			Field field;
			if ("millis".equals(type))
				field = new TimeField(elem, Long.parseLong(matcher.group(j)));
			else if ("date".equals(type))
				field = new DateField(elem, matcher.group(j));
			else if ("mapped".equals(type))
				field = new MappedField(elem, matcher.group(j));
			else
				field = new TextField(elem, matcher.group(j));
			map.put(field.getName(), field);
		}
		return map;
	}*/
        public Map parseLogRecord(String line) throws Exception
	{
            //sLog.debug("parsing line !");
		Map map = new LinkedHashMap();
                IMessagePatternInfo patternInfo=recordMatcherEngine.findFirstMatch(line);
                //sLog.debug("Message:"+line);                
                if(patternInfo==null) 
                {
                    sLog.debug("the pattern doesn't not match");
                    throw new Exception("Parse error: " + line);
                }
                else
                {
                    sLog.debug("pattern detected:"+patternInfo.getPatternName());  
                }
                Matcher matcher = patternInfo.getMatcher();
                //sLog.debug("NumGroups:"+matcher.groupCount());
                //sLog.debug("Num fields:"+m_fieldsElement.getChildren().size());
		int j = 1;
		for (Iterator i = m_fieldsElement.getChildren("field").iterator(); i.hasNext(); j++)
		{
                        Element elem = (Element)i.next();
                        String type = elem.getAttributeValue("type");
                        Field field;
                        String grpVal=matcher.group(j);
                       // sLog.debug("count"+j);
                        //sLog.debug("value:"+grpVal);
                        
			if ("true".equals(elem.getAttributeValue("skip")))
                            continue;
                        else if("false".equals(elem.getAttributeValue("skip")))
                        {                            
                            if(grpVal.equals(": "))
                            {
                                elem = (Element)i.next();//this is the PID element
                                //sLog.debug("this is a no PID msg");
                                field = new TextField(elem, "N/A");
                                //sLog.debug("field name:"+field.getName());
                                map.put(field.getName(), field);     
                                continue;
                            }
                            else
                                continue;
                            
                        }
			                        
			if ("millis".equals(type))
                        {
                                //sLog.debug("time");
                                grpVal=new Long(Long.parseLong(matcher.group(j))).toString();
				field = new TimeField(elem, Long.parseLong(matcher.group(j)));
                                //field = new TimeField(elem, Long.parseLong(grpVal));
                        }
                        else if ("date".equals(type))
                        {
                                //sLog.debug("date");
                                //grpVal=matcher.group(j);
                            	field = new DateField(elem, grpVal);
                        }
                        else if("mapped".equals(type))
                        {
                                //sLog.debug("mapped");
                                //grpVal=matcher.group(j);
				field = new MappedField(elem, grpVal);
                        }
			else
                        {   
                                //sLog.debug("text");
                                //grpVal=matcher.group(j);
				field = new TextField(elem, grpVal);
                        }
                        //sLog.debug("group"+j+":"+field.getName()+":"+grpVal);
			map.put(field.getName(), field);
		}
		return map;
	}
                
	public abstract class Field implements Comparable
	{
		private Field(Element field)
		{
			m_field = field;
			m_name = field.getAttributeValue("name");
			m_notNull = "true".equals(field.getAttributeValue("notNull"));
		}

		public String getName()
		{
			return m_name;
		}

		public boolean matches(Map map)
		{
			if (matches2(map)) return true;
//			System.out.println("NO MATCH " + this + " " + map);
			return false;
		}
/*
		public boolean matches2(Map map)
		{
			String min = m_field.getAttributeValue("min"), max = m_field.getAttributeValue("max");
			if (min != null && max != null)
			{
				int minCompared = compareWith(map.get(min)), maxCompared = compareWith(map.get(max));
//System.out.println("c " + map.get(min) + " " + map.get(max) + " " + minCompared + " " + maxCompared);
//				if (!((minCompared < 0 && maxCompared > 0) || (maxCompared < 0 && minCompared > 0)))
				if ((minCompared < 0 || maxCompared > 0) && (maxCompared < 0 || minCompared > 0))
				{
//System.out.println("!min/max " + m_name + " " + toString() + " " + map.get(min) + " " + map.get(max));
					return false;
				}
			}
			else if (min != null)
			{
//System.out.println("! " + m_name);
				if (compareWith(map.get(min)) < 0)
				{
//System.out.println("! " + m_name);
//System.out.println("!min " + m_name + " " + toString() + " " + ((Object[])map.get(min))[0]);
					return false;
				}
			}
			else if (max != null)
			{
//System.out.println("! " + m_name);
				if (compareWith(map.get(max)) > 0)
				{
//System.out.println("!max " + m_name + " " + toString() + " " + ((Object[])map.get(max))[0]);
					return false;
				}
			}
			if (min == null && max == null)
			{
//System.out.println(map);
				Object value = map.get(m_name);
				if (value == null)
				{
                                    if (!m_notNull) 
                                            return true;
//System.out.println("NO MATCH BECUASE NULL: " + m_name);
					return false;
				}
				boolean match = compareWith(value) == 0;
//System.out.println("match " + match + " " + m_name + " " + toString() + " " + map.get(m_name));
				return match;
			}
			
			return true;
		}*/
                public boolean matches2(Map map)
		{
			String min = m_field.getAttributeValue("min"), max = m_field.getAttributeValue("max");
			if (min != null && max != null)
			{
				int minCompared = compareWith(map.get(min)), maxCompared = compareWith(map.get(max));
				if ((minCompared < 0 || maxCompared > 0) && (maxCompared < 0 || minCompared > 0))
				{
					return false;
				}
			}
			else if (min != null)
			{
				if (compareWith(map.get(min)) < 0)
				{
					return false;
				}
			}
			else if (max != null)
			{
				if (compareWith(map.get(max)) > 0)
				{
					return false;
				}
			}
			if (min == null && max == null)
			{
				Object value = map.get(m_name);
				if (value == null)
				{
                                    if (!m_notNull) 
                                         return true;
                                    else
                                    {
                                        if(m_name.equals("Facility")||m_name.equals("Severity"))
                                            return true;
                                        else
                                            return false;
                                    }
				}
				boolean match = compareWith(value) == 0;
				return match;
			}
			
			return true;
		}

		private int compareWith(Object value)
		{
//if (true) return 0;
			if (value == null) return 0;
			if (value instanceof Object[])
			{                  
                            	Object[] values = (Object[])value;
                                int compared = m_notNull ? -1 : 0;
//				int compared = 0;
				for (int i = 0; i < values.length; i++)
				{
					compared = compareWith(values[i]);
					if (compared == 0)
						break;
//					{
//System.out.println(m_name + " " + this + " == " + values[i] + ": " + compared);
//						return compared;
//					}
				}
//if (compared != 0)
//System.out.println(m_name + " " + this + ": " + compared);
				return compared;
			}
//System.out.println(toString() + " ? " + value);
			return compareTo(value);
		}

                /*private int compareWith(Object value)
		{
//if (true) return 0;
			if (value == null) return 0;
			if (value instanceof Object[])
			{
                            	Object[] values = (Object[])value;
				int compared = m_notNull ? -1 : 0;
//				int compared = 0;
				for (int i = 0; i < values.length; i++)
				{
					compared = compareWith(values[i]);
					if (compared == 0)
						break;
//					{
//System.out.println(m_name + " " + this + " == " + values[i] + ": " + compared);
//						return compared;
//					}
				}
//if (compared != 0)
//System.out.println(m_name + " " + this + ": " + compared);
				return compared;
			}
//System.out.println(toString() + " ? " + value);
			return compareTo(value);
		}*/

		public int compareTo(Object value)
		{
			if (value.toString().trim().length() == 0)
				return 0;
			return toString().compareTo(value);
		}

		private Element m_field;
		private String m_name;
		private boolean m_notNull;
	}

	public class TextField extends Field
	{
		private TextField(Element field, String text)
		{
			super(field);
                        //if(text.equals(""))
                          //  text="N/A";
			if (text.endsWith("\0"))
				text = text.substring(0, text.length() - 1);
			m_text = text;
		}

		public String toString() { return m_text; }

		private String m_text;
	}

	public class TimeField extends Field
	{
		private TimeField(Element field, long time)
		{
			super(field);
			m_time = new Date(time);
		}

		public String toString() { return m_time.toString(); }

		public int compareTo(Object value)
		{
//System.out.println(m_time + " " + value);
			return m_time.compareTo(value);
		}

		public Date getTime() { return m_time; }

		private Date m_time;
	}

	public class DateField extends Field
	{
		private DateField(Element field, String text) throws Exception
		{
			super(field);
			m_format = new SimpleDateFormat(field.getAttributeValue("format"));
			m_format.setTimeZone(TimeZone.getTimeZone("GMT"));
			m_date = m_format.parse(text);
		}

		public String toString() { return m_date.toString(); }

		public int compareTo(Object value)
		{
			return m_date.compareTo(value);
		}

		public Date getDate() { return m_date; }

		private DateFormat m_format;
		private Date m_date;
	}

	public class MappedField extends Field
	{
		private MappedField(Element field, String text)
		{
			super(field);
			m_text = m_value = text;
//System.out.println("!!!!!! " + m_value);
			for (Iterator i = field.getChildren("map").iterator(); i.hasNext(); )
			{
				Element map = (Element)i.next();
				if (text.equals(map.getAttributeValue("key")))
				{
					m_value = map.getAttributeValue("value");
					break;
				}
			}
		}

		public int compareTo(Object value)
		{
//System.out.println("mapped compare: " + m_text + " == " + value);
			if (value.toString().trim().length() == 0)
				return 0;
			return m_text.compareTo(value);
		}
		public String toString() { return m_value; }

		private String m_text, m_value;
	}

	private Pattern m_pattern;
	private Element m_fieldsElement;
	private List m_names = new ArrayList();

	public static void main(String[] args)
	{
		LogRecordFormat lrf = new LogRecordFormat();
		lrf.setPattern("W (W) /?(W) (0) (0) W W W W (\\S*)\\[(0)]: (.*)");
String line =
//"1055402871195-31 20030612-072751-195 /test1 6 9 Jun 12 07:27:51 /test1 CROND[9792]: (mail) CMD (/usr/bin/python -S /var/lib/mailman/cron/qrunner)";
"192.168.1.251_1058904559533_1 20030722-200919-533 127.0.0.1 0 0 Jul 22 16:09:19 127.0.0.1 7/22/03 4:09 PMThread-1: This is MY Syslog Test Message....watch out #2!!!";
//		Matcher m = lrf.m_pattern.matcher("192.168.1.251_1058904559533_1 20030722-200919-533 127.0.0.1 0 0 Jul 22 16:09:19 127.0.0.1 7/22/03 4:09 PMThread-1: This is MY Syslog Test Message....watch out #2!!!");
		Matcher m = lrf.m_pattern.matcher(line);
		System.out.println(m.matches());
	}
        
        protected void finalize()
        {
            recordMatcherEngine=null;
        }
}


