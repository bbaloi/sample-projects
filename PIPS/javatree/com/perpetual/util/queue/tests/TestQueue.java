/*
 * Created on 23-Jul-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.perpetual.util.queue.tests;

import junit.framework.TestCase;

import com.perpetual.util.queue.IGarbageCollectorFilter;
import com.perpetual.util.queue.QueueWithGarbageCollection;

public class TestQueue extends TestCase {

	private class MyFilter implements IGarbageCollectorFilter
	{
		public boolean isCollectable (Object element)
		{
			boolean result = false;
			QueueEntry entry;
			
			if (element instanceof QueueEntry) {
				entry = (QueueEntry) element;
				
				result = entry.isCollectable();	
			} 
			
			return result;
		}
	}

	private class QueueEntry 
	{
		private String data;
		private boolean collectable;
	
		public QueueEntry (String data, boolean collectable)
		{
			this.data = data;
			this.collectable = collectable; 
		}
			
		public boolean isCollectable() {
			return collectable;
		}

		public String getData() {
			return data;
		}

		public void setCollectable(boolean b) {
			collectable = b;
		}

		public void setData(String string) {
			data = string;
		}
	}

	public TestQueue(String arg0) {
		super(arg0);
	}
	
	public void testWithNoGC () throws Exception
	{
		QueueWithGarbageCollection queue = new QueueWithGarbageCollection(500, 500, 0, null);
		
		queue.addToTail(new QueueEntry("data 1", false));
		queue.addToTail(new QueueEntry("data 2", false));
		queue.addToTail(new QueueEntry("data 3", false));
		queue.addToTail(new QueueEntry("data 4", false));
		queue.addToTail(new QueueEntry("data 5", false));
		
		assertEquals("queue size", 5, queue.size());		
	}
	
	public void testWithGC () throws Exception
	{
		QueueWithGarbageCollection queue = new QueueWithGarbageCollection(500, 500, 100, new MyFilter());
		queue.initialize();
		
		queue.addToTail(new QueueEntry("data 1", true));
		queue.addToTail(new QueueEntry("data 2", true));
		queue.addToTail(new QueueEntry("data 3", false));
		queue.addToTail(new QueueEntry("data 4", false));
		queue.addToTail(new QueueEntry("data 5", false));
		
		Thread.sleep(1000);  // wait for GC
		
		assertEquals("queue size", 3, queue.size());
	}
	
	public void testWithGCStopsAtFirstNonCollectable () throws Exception
	{
		QueueWithGarbageCollection queue = new QueueWithGarbageCollection(500, 500, 100, new MyFilter());
		queue.initialize();
		QueueEntry entry3;
		
		queue.addToTail(new QueueEntry("data 1", true));
		queue.addToTail(new QueueEntry("data 2", true));
		queue.addToTail(entry3 = new QueueEntry("data 3", false));
		queue.addToTail(new QueueEntry("data 4", true));
		queue.addToTail(new QueueEntry("data 5", false));
		
		Thread.sleep(1000);  // wait for GC
		
		assertEquals("queue size", 3, queue.size());
		
		// now set entry3 to be collectable
		entry3.setCollectable(true);
		
		Thread.sleep(1000);  // wait for GC
		
		assertEquals("queue size", 1, queue.size());	
	}

}
