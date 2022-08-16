package com.perpetual.util;

/**
 * @author Mike Pot http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.security.*;
import sun.misc.BASE64Encoder;


public class MD5Utils
{
	public static String encodePassword(String pw) throws Exception
	{
		MessageDigest md = MessageDigest.getInstance("MD5");
		return new BASE64Encoder().encode(md.digest(pw.getBytes()));
	}
}


