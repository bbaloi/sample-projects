package com.perpetual.viewer.presentation;

/**
 * @author Mike Pot - http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.*;

import org.apache.struts.action.ActionForm;


public class ViewGeneralSummaryActionForm extends ActionForm
{
      private String[] hostList,facilityList,severityList,selectedSeverities,selectedFacilities,m_messagePatternList;
      private int startYear, startMonth, startDay, startHour, startMinute,
			endYear, endMonth, endDay, endHour, endMinute;
	private String timeZone;
	private int page;
        private boolean severitiesAnd,facilitesAnd,facilitesOrSeverities;
        private boolean facilitiesOrMessages,severitiesOrMessages;
        
        /*boolean kernel,user,mail,daemon,security,syslog,lpr,news,uucp,crond,authority,
                    ftp,audit,alert,crond2,local0,local1,local2,local3,local4,local5,local6,
                    local7,emergency,s_alert,critical,error,warning,notice,info,debug;
                    */
        public void setMessagePatternList(String[] messagePatternList)
	{
		m_messagePatternList = messagePatternList;
	}
	public String[] getMessagePatternList()
	{
		return m_messagePatternList;
	}
        public void resetMessagePatternList()
        {
           m_messagePatternList=null;  
        }
	public void setHostList(String[] pHostList)
	{
		hostList = pHostList;
	}
	public String[] getHostList()
	{
		return hostList;
	}
        public void resetHostList()
        {
            hostList=null;
        }
	public void setStartYear(int pStartYear)
	{
		startYear = pStartYear;
	}
	public int getStartYear()
	{
		return startYear;
	}
	public void setStartMonth(int pStartMonth)
	{
		startMonth = pStartMonth;
	}
	public int getStartMonth()
	{
		return startMonth;
	}
	public void setStartDay(int pStartDay)
	{
		startDay = pStartDay;
	}
	public int getStartDay()
	{
		return startDay;
	}
	public void setStartHour(int pStartHour)
	{
		startHour = pStartHour;
	}
	public int getStartHour()
	{
		return startHour;
	}
	public void setStartMinute(int pStartMinute)
	{
		startMinute = pStartMinute;
	}
	public int getStartMinute()
	{
		return startMinute;
	}
	public void setEndYear(int pEndYear)
	{
		endYear = pEndYear;
	}
	public int getEndYear()
	{
		return endYear;
	}
	public void setEndMonth(int pEndMonth)
	{
		endMonth = pEndMonth;
	}
	public int getEndMonth()
	{
		return endMonth;
	}
	public void setEndDay(int pEndDay)
	{
		endDay = pEndDay;
	}
	public int getEndDay()
	{
		return endDay;
	}
	public void setEndHour(int pEndHour)
	{
		endHour = pEndHour;
	}
	public int getEndHour()
	{
		return endHour;
	}
	public void setEndMinute(int pEndMinute)
	{
		endMinute = pEndMinute;
	}
	public int getEndMinute()
	{
		return endMinute;
	}
	public void setTimeZone(String pTimeZone)
	{
		timeZone = pTimeZone;
	}
	public String getTimeZone()
	{
		return timeZone;
	}
	public void setPage(int pPage)
	{
		page = pPage;
	}
	public int getPage()
	{
		return page;
	}
        public void setFacilitiesAnd(boolean pFacilitesAnd)
        {
            facilitesAnd=pFacilitesAnd;            
        }
        public boolean getFacilitiesAnd()
        {
            return facilitesAnd;            
        }
        public void setSeveritiesAnd(boolean pSeveritiesAnd)
        {
            severitiesAnd=pSeveritiesAnd;            
        }
        public boolean getSeveritiesAnd()
        {
            return severitiesAnd;            
        }
        public void setFacilitiesOrSeverities(boolean pFacilitesOrSeverities)
        {
            facilitesOrSeverities=pFacilitesOrSeverities;            
        }
        public boolean getFacilitiesOrSeverities()
        {
            return facilitesOrSeverities;            
        }
         public void setFacilitiesOrMessages(boolean pFacilitiesOrMessages)
        {
            facilitiesOrMessages=pFacilitiesOrMessages;            
        }
        public boolean getFacilitiesOrMessages()
        {
            return facilitiesOrMessages;            
        }
        public void setSeveritiesOrMessages(boolean pSeveritiesOrMessages)
        {
            severitiesOrMessages=pSeveritiesOrMessages;            
        }
        public boolean getSeveritiesOrMessages()
        {
            return severitiesOrMessages;            
        }
	  
        public String[] getSelectedFacilities()
        {
            return this.selectedFacilities;
        }
        public void setSelectedFacilities(String [] facs)
        {
            this.selectedFacilities=facs;
        }
         public String[] getSelectedSeverities()
        {
            return this.selectedSeverities;
        }
        public void setSelectedSeverities(String [] sevs)
        {
            this.selectedSeverities=sevs;
        }
        public void setFacilityList(String [] pFacilityList)
        {
            facilityList = pFacilityList;
        }
        public String [] getFacilityList()
        {
            return facilityList;
        }
        public void resetFacilityList()
        {
            facilityList=null;
        }
        public void setSeverityList(String [] pSeverityList)
        {
            severityList = pSeverityList;
        }
        public String [] getSeverityList()
        {
            return severityList;
        }
        public void resetSeverityList()
        {
            severityList=null;
        }
        /*
        public void setKernel(boolean pKernel)
        {
            kernel=pKernel;            
        }
        public boolean getKernel()
        {
            return kernel;            
        }
        public void setUser(boolean pUser)
        {
            user=pUser;            
        }
        public boolean getUser()
        {
            return user;            
        }
        public void setMail(boolean pMail)
        {
            mail=pMail;            
        }
        public boolean geMail()
        {
            return mail;            
        }
        public void setDaemon(boolean pDaemon)
        {
           daemon=pDaemon;            
        }
        public boolean getDaemon()
        {
            return daemon;            
        }
        public void setSecurity(boolean pSecurity)
        {
            security=pSecurity;            
        }
        public boolean geSecurity()
        {
            return security;            
        }
        public void setSyslog(boolean pSyslog)
        {
            syslog=pSyslog;            
        }
        public boolean getSyslog()
        {
            return syslog;            
        }
        public void setLpr(boolean pLpr)
        {
            lpr=pLpr;            
        }
        public boolean getLpr()
        {
            return lpr;            
        }
        public void setNews(boolean pNews)
        {
            news=pNews;            
        }
        public boolean getNews()
        {
            return news;            
        }
        public void setUucp(boolean pUucp)
        {
            uucp=pUucp;            
        }
        public boolean getUucp()
        {
            return uucp;            
        }
        public void setCrond(boolean pCrond)
        {
            crond=pCrond;            
        }
        public boolean getCrond()
        {
            return crond;            
        }
        public void setAuthority(boolean pAuthority)
        {
            authority=pAuthority;            
        }
        public boolean getAuthority()
        {
            return authority;            
        }
        public void setFtp(boolean pFtp)
        {
            ftp=pFtp;            
        }
        public boolean getFtp()
        {
            return ftp;            
        }
        public void setAudit(boolean pAudit)
        {
            audit=pAudit;            
        }
        public boolean getAudit()
        {
            return audit;            
        }
        public void setAlert(boolean pAlert)
        {
            alert=pAlert;            
        }
        public boolean getAlert()
        {
            return alert;            
        }
        public void setCrond2(boolean pCrond2)
        {
            crond2=pCrond2;
        }
        public boolean getCrond2()
        {
            return crond2;  
        }
        public void setLocal0(boolean pLocal0)
        {
            local0=pLocal0;            
        }
        public boolean getLocal0()
        {
            return local0;            
        }
         public void setLocal1(boolean pLocal1)
        {
            local1=pLocal1;            
        }
        public boolean getLocal1()
        {
            return local1;            
        }
         public void setLocal2(boolean pLocal2)
        {
            local2=pLocal2;            
        }
        public boolean getLocal2()
        {
            return local2;            
        }
         public void setLocal3(boolean pLocal3)
        {
            local3=pLocal3;            
        }
        public boolean getLocal3()
        {
            return local3;            
        }
         public void setLocal4(boolean pLocal4)
        {
            local4=pLocal4;            
        }
        public boolean getLocal4()
        {
            return local4;            
        }
         public void setLocal5(boolean pLocal5)
        {
            local5=pLocal5;            
        }
        public boolean getLocal5()
        {
            return local5;            
        }
        public void setLocal6(boolean pLocal6)
        {
            local6=pLocal6;            
        }
        public boolean getLocal6()
        {
            return local6;            
        }
        public void setLocal7(boolean pLocal7)
        {
            local7=pLocal7;            
        }
        public boolean getLocal7()
        {
            return local7;            
        }
         public void setEmergency(boolean pEmergency)
        {
            emergency=pEmergency;            
        }
        public boolean getEmergency()
        {
            return emergency;            
        }
         public void setS_alert(boolean pS_alert)
        {
            s_alert=pS_alert;            
        }
        public boolean getS_alert()
        {
            return s_alert;            
        }
         public void setCritical(boolean pCritical)
        {
            critical=pCritical;            
        }
        public boolean getCritical()
        {
            return critical;            
        }
         public void setLError(boolean pError)
        {
            error=pError;            
        }
        public boolean getError()
        {
            return error;            
        }
         public void setWarning(boolean pWarning)
        {
            warning=pWarning;            
        }
        public boolean getWarning()
        {
            return warning;            
        }
         public void setNotice(boolean pNotice)
        {
            notice=pNotice;            
        }
        public boolean getNotice()
        {
            return notice;            
        }
         public void setInfo(boolean pInfo)
        {
            info=pInfo;            
        }
        public boolean getInfo()
        {
            return info;            
        }
         public void setDebug(boolean pDebug)
        {
            debug=pDebug;            
        }
        public boolean getDebug()
        {
            return debug;            
        }
         **/
         
      
}



