/*
 * HostSummaryVO.java
 *
 * Created on July 24, 2003, 8:32 AM
 */

package com.perpetual.rp.model.vo;

import java.util.Date;

/**
 *
 * @author  brunob
 */
public class HostSummaryVO implements java.io.Serializable
{
    private int id;
    private String lHostName=null;
    private Date lStartTimeStamp=null;
    private Date lEndTimeStamp=null;
    private String lDomainName=null;
    private Integer lMessagePatternId = null;
    private VendorSummaryVO lVendorVO=null;

    //facilities
    public int fac0=0;
    public int fac1=0;
    public int fac2=0;
    public int fac3=0;
    public int fac4=0;
    public int fac5=0;
    public int fac6=0;
    public int fac7=0;
    public int fac8=0;
    public int fac9=0;
    public int fac10=0;
    public int fac11=0;
    public int fac12=0;
    public int fac13=0;
    public int fac14=0;
    public int fac15=0;
    public int fac16=0;
    public int fac17=0; 
    public int fac18=0;
    public int fac19=0;
    public int fac20=0;
    public int fac21=0;
    public int fac22=0;
    public int fac23=0;
    //severities
    public int sev0=0;
    public int sev1=0;
    public int sev2=0;
    public int sev3=0;
    public int sev4=0;
    public int sev5=0;
    public int sev6=0;
    public int sev7=0;
    
    /** Creates a new instance of HostSummaryVO */
     public HostSummaryVO(String pHostName,String pDomainName,
     					Integer pMessagePatternId,
     					Date pStartTStamp,Date pEndTStamp,
                        VendorSummaryVO pVendorVO)
     {
         lHostName=pHostName;
         lDomainName=pDomainName;
         lMessagePatternId = pMessagePatternId;
         lStartTimeStamp = pStartTStamp;
         lEndTimeStamp = pEndTStamp;
         lVendorVO = pVendorVO;
     }
     public String getHostName()
     {
         return lHostName;
     }
     public String getDomainName()
     {
         return lDomainName;
     }
     public Integer getMessagePatternId()
     {
         return lMessagePatternId;
     }

     public Date getStartTimeStamp()
     {
         return lStartTimeStamp;
     }
     public Date getEndTimeStamp()
     {
         return lEndTimeStamp;
     }
    public void setId(int pId)
    {
        id=pId;
    }
    public int getId()
    {
        return id;
    }
}
