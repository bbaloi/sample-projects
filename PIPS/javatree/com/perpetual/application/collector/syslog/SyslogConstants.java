/*
 * Created on 29-Jul-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.perpetual.application.collector.syslog;

/**
 * @author simon
 *
 * Contains the values for syslog facilities as defined in syslog(3C)
 */
public final class SyslogConstants {

	public final static int NUMBER_OF_FACILITIES = 24;

	public final static int KERNEL = 0;
	public final static int USER = 1;
	public final static int MAIL = 2;
	public final static int DAEMON = 3;
	public final static int AUTHPRIV = 4;
	public final static int SYSLOG = 5;
	public final static int LPR = 6;
	public final static int NEWS = 7;
	public final static int UUCP = 8;
	public final static int CRON = 9;
	public final static int AUTHPRIV2 = 10;
	public final static int FTP = 11;
	public final static int NTP = 12;
	public final static int AUDIT = 13;
	public final static int FACILITY_ALERT = 14;
	public final static int CRON2 = 15;
	public final static int LOCAL0 = 16;
	public final static int LOCAL1 = 17;
	public final static int LOCAL2 = 18;
	public final static int LOCAL3 = 19;
	public final static int LOCAL4 = 20;
	public final static int LOCAL5 = 21;
	public final static int LOCAL6 = 22;
	public final static int LOCAL7 = 23;
	
	// severities
	
	public final static int NUMBER_OF_SEVERITIES = 8;
	
	public final static int EMERG = 0;
	public final static int ALERT = 1;
	public final static int CRIT = 2; 
	public final static int ERR = 3;
	public final static int WARNING = 4;
	public final static int NOTICE = 5;
	public final static int INFO = 6;
	public final static int DEBUG = 7;
	
	private static class IdName
	{
		private int id;
		private String name;
		private String longName;
		
		public IdName(int id, String name, String longName) {
			this.id = id;
			this.name = name;
			this.longName = longName;
		}
		/**
		 * @return
		 */
		public int getId() {
			return id;
		}

		/**
		 * @return
		 */
		public String getName() {
			return name;
		}
		
		public String getLongName() {
			return longName;
		}

	}
	
	public static IdName[] FACILITIES;
	public static IdName[] SEVERITIES;

	static {
		FACILITIES = new IdName[NUMBER_OF_FACILITIES];
		
		FACILITIES[0] = new IdName(KERNEL, "kern", "Kernel");
		FACILITIES[1] = new IdName(USER, "user", "User");
		FACILITIES[2] = new IdName(MAIL, "mail", "Mail");
		FACILITIES[3] = new IdName(DAEMON, "daemon", "Daemon");
		FACILITIES[4] = new IdName(AUTHPRIV, "authpriv", "Security");
		FACILITIES[5] = new IdName(SYSLOG, "syslog", "Syslog");
		FACILITIES[6] = new IdName(LPR, "lpr", "Lpr");
		FACILITIES[7] = new IdName(NEWS, "news", "News");
		FACILITIES[8] = new IdName(UUCP, "uucp", "Uucp");
		FACILITIES[9] = new IdName(CRON, "cron", "Crond");
		FACILITIES[10] = new IdName(AUTHPRIV2, "authpriv2", "Authority");
		FACILITIES[11] = new IdName(FTP, "ftp", "Ftp");
		FACILITIES[12] = new IdName(NTP, "ntp", "Ntp");
		FACILITIES[13] = new IdName(AUDIT, "audit", "Audit");
		FACILITIES[14] = new IdName(FACILITY_ALERT, "alert", "Alert");
		FACILITIES[15] = new IdName(CRON2, "cron2", "Crond 2");
		
		FACILITIES[16] = new IdName(LOCAL0, "local0", "Local 0");
		FACILITIES[17] = new IdName(LOCAL1, "local1", "Local 1");
		FACILITIES[18] = new IdName(LOCAL2, "local2", "Local 2");
		FACILITIES[19] = new IdName(LOCAL3, "local3", "Local 3");
		FACILITIES[20] = new IdName(LOCAL4, "local4", "Local 4");
		FACILITIES[21] = new IdName(LOCAL5, "local5", "Local 5");
		FACILITIES[22] = new IdName(LOCAL6, "local6", "Local 6");
		FACILITIES[23] = new IdName(LOCAL7, "local7", "Local 7");
		
		SEVERITIES = new IdName[NUMBER_OF_SEVERITIES];
		
		SEVERITIES[0] = new IdName(EMERG, "emerg", "Emergency");
		SEVERITIES[1] = new IdName(ALERT, "alert", "Alert");
		SEVERITIES[2] = new IdName(CRIT, "crit", "Critical");
		SEVERITIES[3] = new IdName(ERR, "err", "Error");
		SEVERITIES[4] = new IdName(WARNING, "warning", "Warning");
		SEVERITIES[5] = new IdName(NOTICE, "notice", "Notice");
		SEVERITIES[6] = new IdName(INFO, "info", "Info");
		SEVERITIES[7] = new IdName(DEBUG, "debug", "Debug");
	}
	
	public static String getFacilityNameById (int id)
	{
		String result = null;
		
		if (id >= 0 &&  id < NUMBER_OF_FACILITIES) {
			result = FACILITIES[id].getName();
		}
		
		return result;
	}

	public static int getFacilityIdByName (String name)
	{
		int result = -1;
		
		for (int i = 0; result == -1 && i < NUMBER_OF_FACILITIES; i++) {
			if (FACILITIES[i].getName().equals(name)) {
				result = i;
			}
		}
		
		return result;
	}
	
	public static int getFacilityIdByLongName (String longName)
	{
		int result = -1;
		
		for (int i = 0; result == -1 && i < NUMBER_OF_FACILITIES; i++) {
			if (FACILITIES[i].getLongName().equals(longName)) {
				result = i;
			}
		}
		
		return result;
	}

	
	public static String getSeverityNameById (int id)
	{
		String result = null;
		
		if (id >= 0 &&  id < NUMBER_OF_SEVERITIES) {
			result = SEVERITIES[id].getName();
		}
		
		return result;
	}

	public static int getSeverityIdByName (String name)
	{
		int result = -1;
		
		for (int i = 0; result == -1 && i < NUMBER_OF_SEVERITIES; i++) {
			if (SEVERITIES[i].getName().equals(name)) {
				result = i;
			}
		}
		
		return result;
	}
	
	public static int getSeverityIdByLongName (String longName)
	{
		int result = -1;
		
		for (int i = 0; result == -1 && i < NUMBER_OF_SEVERITIES; i++) {
			if (SEVERITIES[i].getLongName().equals(longName)) {
				result = i;
			}
		}
		
		return result;
	}
}
