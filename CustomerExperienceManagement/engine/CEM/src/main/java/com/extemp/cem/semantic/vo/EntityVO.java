package com.extemp.cem.semantic.vo;

/**
 * @author bruno
 *
 */
public class EntityVO
{
	private long numInstances;
	private String entityName;
	
	public EntityVO(String pName,long pNumInstances)
	{
		numInstances=pNumInstances;
		entityName = pName;
	}
	
	public long getNumInstances() {
		return numInstances;
	}
	public void setNumInstances(long numInstances) {
		this.numInstances = numInstances;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	

}
