package com.perpetual.application.collector.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.perpetual.application.collector.configuration.Configuration;
import com.perpetual.exception.PerpetualException;

/**
 * @author simon
 *
 * Parse a configuration file in the syntax described in syslogd(1M)
 * 
 * Each line is either a comment begins with '#' or an action line.
 * An action line consists of a selector[\t]*actionText.
 * 
 * E.g.
 * 
 * 		# This forwards to host 192.168.2.1
 * 		*.*		@192.168.2.1
 */
public class TextFileParser {
	
	private String configurationFilePath = null;
	private BufferedReader reader = null;
	private Configuration configuration;
	
	public TextFileParser (String configurationFilePath, Configuration configuration)
	{
		this.configurationFilePath = configurationFilePath;
		this.configuration = configuration;
	}
	
	public TextFileParser (BufferedReader reader, Configuration configuration)
	{
		this.reader = reader;	
		this.configuration = configuration;
	}
	
	public List parse () throws FileNotFoundException, IOException,
			PerpetualException
	{
		int lineNumber = 0;
		List actionList = new ArrayList();
		ActionFactory factory = new ActionFactory();
		
		if (this.reader == null) {
			this.reader = new BufferedReader(
					new FileReader(new File(this.configurationFilePath)));
		}
		
		String textLine;
		
		while ((textLine = this.reader.readLine()) != null) {
			lineNumber++;
			// each line will either be an action line or comment
			// comments start with '#'
			if (!isComment(textLine)) {
				String[] pair = extractSelectorAndActionText(textLine);
				
				String selector = pair[0];
				String actionText = pair[1];
			
				if (selector != null && actionText != null) {
					BaseAction action = factory.createAction(selector, actionText,
							this.configuration);
					actionList.add(action);
					
					if (action == null) {
						throw new PerpetualException("Line " + lineNumber
							+ ": Cannot find an action for '"							+ actionText + "'");
					}
				} else {
					throw new PerpetualException("Line" + lineNumber
						+ ": cannot parse '" + textLine + "' into selector-action");	
				}
			} 
		}
		
		return actionList;
	}
	
	private boolean isComment (String textLine)
	{
		return textLine.charAt(0) == '#';
	}
	
	private String extractSelector (String textLine)
	{
		int tabIndex = textLine.indexOf('\t');
		 
		if (tabIndex == -1) {
			return null;
		} else {
			return textLine.substring(0, tabIndex);
		}
	}
	
	private String[] extractSelectorAndActionText (String textLine)
	{
		String[] selectorActionPair = new String[2];
		
		StringTokenizer st = new StringTokenizer(textLine, "\t");
		
		selectorActionPair[0] = st.nextToken();
		selectorActionPair[1] = st.nextToken();
		
		return selectorActionPair;
	}
}
