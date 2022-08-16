/*
 * BasePerpetualException.java
 *
 * Created on June 25, 2003, 5:36 PM
 */
package com.perpetual.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Vector;

import org.jdom.*;
import org.jdom.input.*;
import org.xml.sax.InputSource;

import com.perpetual.util.PerpetualResourceLoader;


/**
 *
 * @author  brunob
 */
public class BasePerpetualException extends Exception implements java.io.Serializable
{
    protected static BasePerpetualExceptionHandler lHandler=null;
        
    /** Creates a new instance of BasePerpetualException */
    public BasePerpetualException() 
    {
    }
        private	Throwable	exception	= null;
	private Vector		stackMessage	= null;
	private String		lineSeparator	= System.getProperty( "line.separator" );


	// variables used to print the getLocalizedMessage() string
	protected	String		code			= null;
	protected	Object[]	parameterArray	= null;

        public static void handle(String pMessage)
        {
            lHandler.handleException(pMessage);
        }
        public static void handle(String pMessage,Throwable t)
        {
            lHandler.handleException(pMessage,new BasePerpetualException(pMessage,t));
        }
        
	public BasePerpetualException(String pMessage)
	{
		super( pMessage );
		
		stackMessage = new Vector();
	}

	public BasePerpetualException(String pCode, String pMessage, Object[] pParameterArray)
	{
		this( pMessage );

		code = pCode;
		parameterArray = pParameterArray;
	}

	public BasePerpetualException(String pCode, String pMessage, Throwable pThrowable, Object[] pParameterArray)
	{
		super(pMessage, pThrowable);	// without this, we never see where the exception originated

		code = pCode;
		parameterArray = pParameterArray;

		exception = pThrowable;

		appendStackMessage( pThrowable.getClass().getName() + ":  " + pThrowable.getMessage() );
	}

	public BasePerpetualException(String pMessage, Throwable pThrowable)
	{
		this( pMessage );

		exception = pThrowable;

		appendStackMessage( pThrowable.getClass().getName() + ":  " + pThrowable.getMessage() );
	}

	public String getCode()
	{
		return( code );
	}

	public Object[] getParameterArray()
	{
		return( parameterArray );
	}

	public Throwable getException()
	{
		return( exception );
	}

	public void appendStackMessage( String pStackMessage )
	{
		stackMessage.addElement( pStackMessage );
	}

	public Vector getStackMessage()
	{
		return( stackMessage );
	}

	public static void processException( String pMessage, Throwable pThrowable )
         throws BasePerpetualException
	{
		if ( pThrowable instanceof BasePerpetualException ) {
			((BasePerpetualException)pThrowable).appendStackMessage( pMessage );
			throw (BasePerpetualException)pThrowable;
		}
		else {
			throw new BasePerpetualException( pMessage, pThrowable );
		}
	}

	public static void processException( String pCode, String pMessage, Throwable pThrowable, Object[] pParameterArray )
         throws BasePerpetualException
	{
		if ( pThrowable instanceof BasePerpetualException ) {
			((BasePerpetualException)pThrowable).appendStackMessage( pMessage );
			throw (BasePerpetualException)pThrowable;
		}
		else {
			throw new BasePerpetualException( pCode, pMessage, pThrowable, pParameterArray );
		}
	}

	public String toString()
	{
		return( getMessage() );
	}

	public String getMessage( boolean pVerbose )
	{
		if ( pVerbose ) {
			return( getMessage() );
		}
		else {
			return( super.getMessage() );
		}
	}

	public String getMessage()
	{
		String returnValue		= null;
		int    stackSize		= 0;

		returnValue = this.getClass().getName() + ":  " +  super.getMessage();
		
		stackSize = stackMessage.size();
		for ( int i = 0 ; i < stackSize ; i++ ) {
		  returnValue += lineSeparator + (String)stackMessage.elementAt( i );
		}

		return( returnValue );
	}

	public String getLocalizedMessage()
	{
		ResourceBundle	lResourceBundle = null;

		String		lErrorString	= null;
		String		lResult			= null;

		try
		{
			lResourceBundle = PerpetualResourceLoader.getDefault().getResourceBundle( "errors" );

			lErrorString = lResourceBundle.getString( code );

			lResult = MessageFormat.format( lErrorString, parameterArray );
		}
		catch( Exception e )
		{
			System.err.println( "Unable to find error string for:  " + code );
			lResult = getMessage( false );
		}

		return( lResult );
	}	

	/*
	 *	public void printStackTrace()
	 *	
	 *	These methods override the Exceptions class' versions
	 *	to handle the internal stack trace
	 */
	public void printStackTrace()
	{
		printStackTrace( System.err );
	}

	public void printStackTrace( PrintStream pPrintStream )
	{
		pPrintStream.println( this.toString() );

		if ( exception != null ) {
			exception.printStackTrace( pPrintStream );
		}

		super.printStackTrace( pPrintStream );
	}

	public void printStackTrace( PrintWriter pPrintWriter )
	{
		pPrintWriter.println( this.toString() );

		if ( exception != null ) {
			exception.printStackTrace( pPrintWriter );
		}

		super.printStackTrace( pPrintWriter );
	}

	public String toXml()
	{
		String lXml = null;

		lXml =	"<BasePerpetualException>";
		lXml +=	"<CODE>" + code + "</CODE>";
		lXml +=	"<MESSAGE>" + getMessage( false ) + "</MESSAGE>";
		if ( parameterArray != null ) {
			int lLength = parameterArray.length;
			for ( int i = 0 ; i < lLength ; i++ ) {
				lXml +=	"<P" + i + ">" + parameterArray[i] + "</P" + i + ">";
			}
		}
		lXml += "</BasePerpetualException>";

		return( lXml );
	}

	public static BasePerpetualException fromXml( String pXml )
	{
		SAXBuilder		lSAXBuilder			= null;
		StringReader	lStringReader		= null;
		InputSource		lInputSource		= null;
		Document		lDocument			= null;
		Element			lElement			= null;

		BasePerpetualException lBasePerpetualException = null;

		String		lCode				= "UNKNOWN";
		String		lMessage			= "Unable to recreate BasePerpetualException";
		String		lParameter			= null;
		ArrayList	lParameterArrayList	= null;
		Object[]	lParameterArray		= null;

		try
		{
			lStringReader = new StringReader( pXml );
			lInputSource = new InputSource( lStringReader );
			lSAXBuilder = new SAXBuilder();

			lDocument = lSAXBuilder.build( lInputSource );
			
			// get the root of the tree
			lElement = lDocument.getRootElement();

			try
			{
				lCode = lElement.getChildText( "CODE" );
			}
			catch( Exception e )
			{
				// do nothing
			}

			try
			{
				lMessage = lElement.getChildText( "MESSAGE" );
			}
			catch( Exception e )
			{
				// do nothing
			}

			lParameterArrayList = new ArrayList();
			try
			{
				for ( int i = 0 ; ; i++ ) {
					lParameter = lElement.getChildText( "P" + i );

					if ( lParameter == null ) {
						break;
					}

					lParameterArrayList.add( lParameter );
				}
			}
			catch( Exception e )
			{
				// do nothing
			}

			if ( lParameterArrayList.size() > 0 ) {
				lParameterArray = lParameterArrayList.toArray();
			}
		}
		catch( Exception e )
		{
			// do nothing
		}

		lBasePerpetualException = new BasePerpetualException( lCode, lMessage, lParameterArray );

		return( lBasePerpetualException );
	}
    
}
