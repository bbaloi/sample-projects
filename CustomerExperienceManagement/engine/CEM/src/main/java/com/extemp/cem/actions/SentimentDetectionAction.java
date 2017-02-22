package com.extemp.cem.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.pattern.Patterns;
import akka.util.Timeout;

import com.aliasi.classify.LMClassifier;
import com.aliasi.tokenizer.Tokenizer;
import com.aliasi.util.AbstractExternalizable;
import com.extemp.cem.akka.kie.UntypedKieSessionActor;
import com.extemp.cem.backbone.core.RuleSession;
import com.extemp.cem.backbone.core.SentimentClassifier;
import com.extemp.cem.util.CEMUtil;

//public class SentimentDetectionAction extends BaseSentimentAction
public class SentimentDetectionAction 
{

	private static SentimentDetectionAction instance=null;
	private static LMClassifier lmClassifier;  

	
	public SentimentDetectionAction()
	{
		//super();
	}
	
	public static SentimentDetectionAction getInstance()
	{
		if(instance==null)
		{
			instance = new SentimentDetectionAction();
			String _classifierModelPath = CEMUtil.getInstance().getCEMProperty("cem.classifier.model");
			
			System.out.println("modelpath: "+_classifierModelPath);
			try
			{
				lmClassifier = (LMClassifier) AbstractExternalizable.readObject(new File(_classifierModelPath));  
			}
			catch(Exception excp)
			{
				excp.printStackTrace();
			}
			
		}
		return instance;
	}
	
	public String getSentiment(String pMessage)
	{
		String result=null ; 
		
		try
		{
			/*
			Timeout timeout = new Timeout(Duration.create(2, "seconds"));
			//Future<Object> future = Patterns.ask(getContext().actorOf(Props.create(SentimentClassifier.class)), "my beer is hot and hotdog cold...", timeout);
			Future<Object> future = Patterns.ask(sentimentClassifier, "my beer is hot and hotdog cold...", timeout);
			result = (String) Await.result(future, timeout.duration());
			System.out.println("Sentiment - " + result);
			*/
			result = lmClassifier.classify((String)pMessage).bestCategory();
			
			//result = lmClassifier.classify("my beer is hot and hotdog cold").bestCategory();
			
			
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}
    
		return result;
	}
	public List extractEntities(String pMessage)
	{
		System.out.println(" Negative response: getting entities !!!!");
		
			InputStream _tModelIn=null,_pModelIn = null;
			POSModel _posModel=null;
			TokenizerModel _tokenModel=null;
			ArrayList _retList=new ArrayList();
	
			try {
				String _posModelPath = CEMUtil.getInstance().getCEMProperty("cem.nlp.pos.model");
				String _tokenModelPath = CEMUtil.getInstance().getCEMProperty("cem.nlp.token.model");
				//modelIn = new FileInputStream("en-pos-maxent.bin");
				
				_tModelIn = new FileInputStream(_tokenModelPath);
				_pModelIn = new FileInputStream(_posModelPath);
				
				_tokenModel = new TokenizerModel(_tModelIn);				
				_posModel = new POSModel(_pModelIn);
				
				
				//System.out.println("Token Model:"+ _tokenModel+", POSModel: "+_posModel);
				
				TokenizerME tokenizer = new TokenizerME(_tokenModel);			
				POSTaggerME tagger = new POSTaggerME(_posModel);
				
				
				String tokens[] = tokenizer.tokenize(pMessage);			
				String tags[] = tagger.tag(tokens);
				
					
			//	System.out.println("-----POS tags-----num tokens:"+tokens.length+", num tags:"+tags.length);
				
				for (int i=0;i< tags.length;i++)
				{
				//	System.out.println("word="+tokens[i]+", tag="+tags[i]);
					
					if(tags[i].equals("NN") || tags[i].equals("NNP") ||tags[i].equals("NNPS") || tags[i].equals("NNS"))
						_retList.add(tokens[i]);
						
				}
				
			
				//_retList = Arrays.asList(tags);
				
			}
			catch (IOException e) {
			  // Model loading failed, handle the error
			  e.printStackTrace();
			}
			finally 
			{
			  if (_tModelIn != null) {
			    try {
			      _tModelIn.close();
			    }
			    catch (IOException e) {
			    }
			  }
			  if (_pModelIn != null) {
				    try {
				      _pModelIn.close();
				    }
				    catch (IOException e) {
				    }
				  }
			}
			
			
			
		return _retList;
	}

	
  
}
