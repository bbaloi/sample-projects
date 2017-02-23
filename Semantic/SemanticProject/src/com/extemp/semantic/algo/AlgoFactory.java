package com.extemp.semantic.algo;

import com.hp.hpl.jena.ontology.OntModel;

public class AlgoFactory 
{
	public static RouteOptimisationAlgo getRouteOptimisationAlgo(OntModel pModel)
	{
		return new RouteOptimisationAlgo(pModel);
	}
	public static ImpactAnalysisAlgo getImapctAnalysisAlgo(OntModel pModel)
	{
		return new ImpactAnalysisAlgo(pModel);
	}

}
