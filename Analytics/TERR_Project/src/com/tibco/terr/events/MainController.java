package com.tibco.terr.events;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.tibco.as.space.ASCommon;
import com.tibco.as.space.ASException;
import com.tibco.as.space.DateTime;
import com.tibco.as.space.LogLevel;
import com.tibco.as.space.Member;
import com.tibco.as.space.MemberDef;
import com.tibco.as.space.Metaspace;
import com.tibco.as.space.Tuple;
import com.tibco.terr.TerrJava;
import com.tibco.terr.events.as.SpaceExplorer;
import com.tibco.terr.events.util.Utils;

public class MainController implements IController
{
	//private TERRController _terrController;
	private Thread _dataLoaderThread, _predictionThread, _modelThread;
	private HashMap<String, Metaspace> _physicalMetaSpaceMap;
	private HashMap<String,SpaceExplorer> _spaceExplorerMap;
	private Utils _utils;
	private ExecutorService _threadPool;
		
	
	public MainController()
	{
		init();
	}

	
	private void init()
	{
		ASCommon.setLogLevel(LogLevel.INFO);
		_physicalMetaSpaceMap = new HashMap();
		_spaceExplorerMap = new HashMap();
		_utils = Utils.getInstance();
		
		HashMap _metaMap = _utils.get_asMetaSpaceMap();
		Set _ks = _metaMap.keySet();
		Iterator<String> _it = _ks.iterator();
		try
		{
			while(_it.hasNext())
			{
				String _metaspaceName = _it.next();
				_physicalMetaSpaceMap.put(_metaspaceName, connectMetaspace(_metaspaceName));			
			}
		
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}
		
	}
	 private Metaspace connectMetaspace (String pMetaspaceName) throws ASException
	 {
        MemberDef memberDef = MemberDef.create();
        memberDef.setDiscovery(_utils.get_asDiscovery());
        memberDef.setListen(_utils.get_asListen());
        memberDef.setRemoteListen(_utils.get_asRemoteListen());
        memberDef.setRemoteDiscovery(_utils.get_asRemoteDiscovery());
        memberDef.setMemberName(_utils.get_asMemberName());        
       
        Tuple context = Tuple.create();
        context.put("platform", "java");
        context.put("jointime", DateTime.create());
        memberDef.setContext(context);

        Metaspace ms = Metaspace.connect(pMetaspaceName, memberDef);
        
        Collection<Member> members = ms.getMetaspaceMembers();
        System.out.println("Current metaspace members: ");
        for (Member member : members)
        {
            System.out.println("\t" + member.getName() + " as " + member.getManagementRole() + " context=" + member.getContext());
        }
        System.out.println("");
        
        //createSpaces associated with the metaspace
        createSpaces(ms);

        return ms;
    }
	 
	 private void createSpaces(Metaspace pMs)
	 {
		 HashMap _metaMap = _utils.get_asMetaSpaceMap();
		 List spaceList = (List)_metaMap.get(pMs.getName());
		 Iterator _it = spaceList.iterator();
		 while(_it.hasNext())
		 {
			 String _spaceName= (String)_it.next();			
			 SpaceExplorer _explorer =  _utils.createSpaceFactory(pMs,_spaceName);
			 _spaceExplorerMap.put(pMs.getName()+"."+_spaceName, _explorer);
		 }
		 
		 
	 }


	@Override
	public void startController() {
		// TODO Auto-generated method stub
		//get all SpaceExplorers associated with the metaspaces form metaspaces and kick off Threads to have them started.
		
		Set _spaceSet = _spaceExplorerMap.keySet();
		Iterator _it = _spaceSet.iterator();
		_threadPool = Executors.newFixedThreadPool(_utils.get_threadPoolSize());
		while(_it.hasNext())
		{
			_threadPool.execute(_spaceExplorerMap.get(_it.next()));
		}		
		
	}



	@Override
	public void stopController() {
		// TODO Auto-generated method stub
		try
		{
			TerrJava.stopEngine();
			_threadPool.shutdownNow();
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}
		
		
		
	}
	
}
