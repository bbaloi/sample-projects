package com.perpetual.util.threadpool;

/*****************************************************************************
 *	Title:			ThreadPoolManager
<p>
 *	Description:	Controls incoming requests and provides means for 
 *					ManagedThread to run Runnable objects
<p>
 *	Company:		Perpetual
<p>
 *	@author			b2
******************************************************************************/
import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.exception.BasePerpetualException;
import java.util.ArrayList;
import java.util.Iterator;
import java.lang.reflect.Constructor;

public final class SimpleThreadPoolManager implements IThreadManager
{
	private static PerpetualC2Logger sLog = new PerpetualC2Logger( SimpleThreadPoolManager.class );

        protected	int		lLoadedThreads   	= 0;
        protected	int		lInitialThreadPoolSize	= 0;
        protected	int		lMaxThreadPoolSize	= 0;
        protected       ArrayList        threadList              =null;
        protected       int currentlyRunningThreads;
	
	public SimpleThreadPoolManager( int initialThreadPoolSize,int maxThreadPoolSize) throws BasePerpetualException
	{
            sLog.debug("SimpleThreadManagerConstructor: inittial size="+initialThreadPoolSize+", max size="+maxThreadPoolSize);
		Thread	lThread		= null;
		lInitialThreadPoolSize	= initialThreadPoolSize;
                lMaxThreadPoolSize      = maxThreadPoolSize;    
		startInitialPool();		
	}
        public SimpleThreadPoolManager( int initialThreadPoolSize,int maxThreadPoolSize,Class loadType,Object [] params,Class [] paramTypes) throws BasePerpetualException
	{
            sLog.debug("SimpleThreadManagerConstructor: inittial size="+initialThreadPoolSize+", max size="+maxThreadPoolSize+" for load:"+loadType.getName());
		Thread	lThread		= null;
		lInitialThreadPoolSize	= initialThreadPoolSize;
                lMaxThreadPoolSize      = maxThreadPoolSize;    
		startInitialPool(loadType,params,paramTypes);		
	}
	
	public synchronized void addNewThread(String pThreadName, Runnable pRunnable)
                            throws BasePerpetualException
	{
            ManagedThread thread=null;
            
            if(threadList.size()==lMaxThreadPoolSize) //can't add any more threads - max reached
            {
                    sLog.error("Max ThreadPool Limit reached !");
                   throw new BasePerpetualException("Reached ThreadPool Max Limit - cannot create new Thread !");
            }
            else
            {
                  sLog.debug("TP Manager - adding a new thread !");
                  thread = new ManagedThread(pThreadName, pRunnable);
                  threadList.add(thread);
                  thread.start();		
                  lLoadedThreads++;
                  sLog.debug("New Thread-"+pThreadName+"- added !");
            }       
	}
        
       public synchronized void addNewThread(String pThreadName) throws BasePerpetualException
       {
           ManagedThread thread=null;
            
            if(threadList.size()==lMaxThreadPoolSize) //can't add any more threads - max reached
            {
                   sLog.error("Max ThreadPool Limit reached !");
                   throw new BasePerpetualException("Reached ThreadPool Max Limit - cannot create new Thread !");
            }
            else
            {
                  sLog.debug("adding a new thread !");
                  thread = new ManagedThread(pThreadName, null);
                  threadList.add(thread);
                  thread.start();		
                  sLog.debug("New Thread-"+pThreadName+"- added !");
            }       
       }
        
       public synchronized void removeThread(String pThreadName) throws BasePerpetualException
        {
            sLog.debug("Looking to remove Thread:"+pThreadName);
            boolean found=false;            
            Iterator it = threadList.iterator();
            
            while(it.hasNext())
            {
                ManagedThread thread = (ManagedThread) it.next();
                if(pThreadName.equals(thread.getName()))
                {
                   if(!thread.hasLoad())
                   {
                    threadList.remove(thread);
                    found=true;
                    break;
                   }
                   else
                   {
                       throw new BasePerpetualException("Cannot remove Thread - it is still loaded !");
                   }
                }  
            }
            if(found)
                sLog.debug("Thread:"+pThreadName+" removed !");
            else
                sLog.error("Thread '"+pThreadName+"' not found !");
            
        }
        public synchronized void addLoad(Runnable pRunnable) throws BasePerpetualException
        {
            sLog.debug("Adding a new load to a Free Thread !");
            boolean foundFreeThread=false;
            
            Iterator it = threadList.iterator();
            while(it.hasNext())
            {
                ManagedThread thread = (ManagedThread) it.next();
                if(!thread.hasLoad())
                {
                    foundFreeThread=true;
                    thread.addLoad(null,pRunnable);
                    sLog.debug("Found Free Thread:"+thread.getName()+" - adding Load!");
                    lLoadedThreads++;
                    break;
                }
            }
            if(foundFreeThread==false)
            {
                sLog.error("Cannot add load ! No existing free threads available ! ");
                throw new BasePerpetualException("No Free Threads available to run the load !"); 
           
            }
             
        }    
        public synchronized void addLoadToThread(String pThreadName, Runnable pRunnable)
                                    throws BasePerpetualException
        {
            sLog.debug("adding a load to thread:"+pThreadName);
            boolean foundThread=false;
            
            Iterator it = threadList.iterator();
            while(it.hasNext())
            {
                ManagedThread thread = (ManagedThread) it.next();
                if(pThreadName.equals(thread.getName()))
                {
                    if(!thread.hasLoad())
                    {
                        thread.addLoad(pThreadName,pRunnable);
                        foundThread=true;
                        lLoadedThreads++;
                        sLog.debug("Added Load to thread !");
                        break;
                    }
                    else
                    {
                        sLog.error("Could not add load to thread - Thread already loaded !");
                        throw new BasePerpetualException("Could not add load to thread - Thread already loaded !");
                    }
                }
                   
            }
            
            if(foundThread==false)
            {
                sLog.error("No thread found !");
                throw new BasePerpetualException("Could not add load to thread - Thread already loaded !");
            }
        }
        public synchronized void removeLoadFromThread(String pThreadName) throws BasePerpetualException
        {
            sLog.debug("Removing load from thread");
            Iterator it = threadList.iterator();
            boolean foundThread=false;
            
            while(it.hasNext())
            {
                ManagedThread thread = (ManagedThread) it.next();
                if(pThreadName.equals(thread.getName()))
                {
                    thread.removeLoad();
                    foundThread=true;
                    sLog.debug("Removed loadfrom thread !");
                    lLoadedThreads--;
                     break;
                }
                   
            }
            if(foundThread==false)
            {
                sLog.error("No thread found !"); 
                throw new BasePerpetualException("Could not add load to thread - Thread already loaded !");
            }
        }    
        public synchronized void replaceLoad(String pThreadName, Runnable pRunnable) throws BasePerpetualException
        {
            sLog.debug("Attempting to replace Load  for thread:"+pThreadName);
       
           Iterator it = threadList.iterator();
            boolean foundThread=false;
            
            while(it.hasNext())
            {
                ManagedThread thread = (ManagedThread) it.next();
                if(pThreadName.equals(thread.getName()))
                {
                    thread.addLoad(pThreadName,pRunnable);
                    foundThread=true;
                    sLog.debug("Replaced load in thread:"+pThreadName);
                     break;
                }
                   
            }
            if(foundThread==false)
            {
                sLog.error("No thread found !");
                throw new BasePerpetualException("Could not add load to thread - Thread already loaded !");
            }
        
        
        }  
        
        public synchronized void shutdownAllThreads() throws BasePerpetualException
        {
            sLog.debug("Shutding down all threads !...");
            Iterator it = threadList.iterator();
            while(it.hasNext())
            {
                ManagedThread thread = (ManagedThread) it.next();
                threadList.remove(thread);
                thread.shutdown();
                
            }
        }
        public synchronized void shutdownThread(String pThreadName) throws BasePerpetualException
        {
            sLog.debug("Shutting down Thread:"+pThreadName);
            Iterator it = threadList.iterator();
            boolean foundThread=false;
            
            while(it.hasNext())
            {
                ManagedThread thread = (ManagedThread) it.next();
                if(pThreadName.equals(thread.getName()))
                {
                     threadList.remove(thread);
                    thread.shutdown();
                    foundThread=true;
                    sLog.debug("Shut Down Thread:"+pThreadName);
                     break;
                }
                   
            }
            if(foundThread==false)
            {
                sLog.error("No thread found !");
                throw new BasePerpetualException("Could not add load to thread - Thread already loaded !");
            }
        
        }        
        private void startInitialPool() throws BasePerpetualException
	{
		ManagedThread thread = null;
                if(lInitialThreadPoolSize==0)
                    threadList = new ArrayList();
                else
                    threadList = new ArrayList(lInitialThreadPoolSize);
                
                for(int i=0;i<lInitialThreadPoolSize;i++)
                {
                    String threadName =new String(new Integer(i).toString()) ;
                    thread = new ManagedThread(threadName,null);
                    threadList.add(thread);
                    thread.start();
                                  
                }
	}
        private void startInitialPool(Class pLoadType,Object [] params,Class [] paramTypes) throws BasePerpetualException
	{
            sLog.debug("Starting innitial ThreadPool !");
		ManagedThread thread = null;
                Runnable runObj=null;
                if(lInitialThreadPoolSize==0)
                    threadList = new ArrayList();
                else
                    threadList = new ArrayList(lInitialThreadPoolSize);
                
                for(int i=0;i<lInitialThreadPoolSize;i++)
                {
                    String threadName =new String(new Integer(i).toString()) ;
                    //Runnable runObj = (Runnable) pLoadType.newInstance();
                    try
                    {
                        Constructor constructor =  pLoadType.getConstructor(paramTypes);
                        runObj = (Runnable) constructor.newInstance(params);
                    }
                    catch(java.lang.NoSuchMethodException excp)
                    {
                        String msg="Invalid method";
                        sLog.error(msg);
                        excp.printStackTrace();
                        throw new BasePerpetualException(msg,excp);
                        
                    }
                    catch(java.lang.SecurityException excp)
                    {
                        String msg="Method Security Violation - invalid access";
                        sLog.error(msg);
                        excp.printStackTrace();
                        throw new BasePerpetualException(msg,excp);
                    }
                    catch(java.lang.InstantiationException excp)
                    {
                        String msg="Could not instantiate Runnable object !";
                        sLog.error(msg);
                        excp.printStackTrace();
                        throw new BasePerpetualException(msg,excp);
                    }      
                    catch(java.lang.IllegalArgumentException excp)
                    {
                        String msg="Could not instantiate Runnable object !";
                        sLog.error(msg);
                        excp.printStackTrace();
                        throw new BasePerpetualException(msg,excp);
                    }      
                    catch(java.lang.IllegalAccessException excp)
                    {
                        String msg="Could not instantiate Runnable object !";
                        sLog.error(msg);
                        excp.printStackTrace();
                        throw new BasePerpetualException(msg,excp);
                    }      
                    catch(java.lang.reflect.InvocationTargetException excp)
                    {
                        String msg="Could not instantiate Runnable object !";
                        sLog.error(msg);
                        excp.printStackTrace();
                        throw new BasePerpetualException(msg,excp);
                    }      
                   
                    thread = new ManagedThread(threadName,runObj);
                    threadList.add(thread);
                    thread.start();
                                  
                }
	}
      
        public int getNumberOfRunningThreads()
        {
            return currentlyRunningThreads;
        }        
       
               
}
