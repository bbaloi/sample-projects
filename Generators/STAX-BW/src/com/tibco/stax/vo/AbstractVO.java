package com.tibco.stax.vo;

public abstract class AbstractVO 
{
	public AbstractVO(String pFilename,boolean pWriteToFile)
	{
		lFileName=pFilename;
		lWriteToFile=pWriteToFile;
		
	}
	protected String lFileName;
	protected boolean lWriteToFile;
	
	public String getLFileName() {
		return lFileName;
	}
	public boolean isLWriteToFile() {
		return lWriteToFile;
	}

}
