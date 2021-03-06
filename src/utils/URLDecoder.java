/*
 * Copyright 1998-2006 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as* 
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).

 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,

 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.

 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package utils;

import java.io.*;

/**
 * 
 * Utility class for HTML form decoding. This class contains static methods
 * 
 * for decoding a String from the <CODE>application/x-www-form-urlencoded</CODE>
 * 
 * MIME format.
 * <p>
 * The conversion process is the reverse of that used by the URLEncoder class.
 * It is assumed that all characters in the encoded string are one of the
 * following:
 * 
 * &quot;<code>a</code>&quot; through &quot;<code>z</code>&quot;, &quot;
 * <code>A</code>&quot; through &quot;<code>Z</code>&quot;,
 * 
 * &quot;<code>0</code>&quot; through &quot;<code>9</code>&quot;, and
 * 
 * &quot;<code>-</code>&quot;, &quot;<code>_</code>&quot;, &quot;<code>.</code>
 * &quot;, and &quot;<code>*</code>&quot;. The character &quot;<code>%</code>
 * &quot; is allowed but is interpreted
 * 
 * as the start of a special escaped sequence.
 * <p>
 * The following rules are applied in the conversion:
 * <p>
 * 
 * <ul>
 * <li>The alphanumeric characters &quot;<code>a</code>&quot; through &quot;
 * <code>z</code>&quot;, &quot;<code>A</code>&quot; through
 * 
 * &quot;<code>Z</code>&quot; and &quot;<code>0</code>&quot; through &quot;
 * <code>9</code>&quot; remain the same.
 * 
 * <li>The special characters &quot;<code>.</code>&quot;,
 * 
 * &quot;<code>-</code>&quot;, &quot;<code>*</code>&quot;, and &quot;
 * <code>_</code>&quot; remain the same.
 * 
 * <li>The plus sign &quot;<code>+</code>&quot; is converted into a
 * 
 * space character &quot;<code>&nbsp;</code>&quot; .
 * <li>A sequence of the form "<code>%<i>xy</i></code>" will be treated as
 * representing a byte where <i>xy</i> is the two-digit
 * 
 * hexadecimal representation of the 8 bits. Then, all substrings
 * 
 * 
 * that contain one or more of these byte sequences consecutively
 * 
 * will be replaced by the character(s) whose encoding would result
 * 
 * 
 * in those consecutive bytes. The encoding scheme used to decode these
 * characters may be specified, or if unspecified, the default encoding of the
 * platform will be used.
 * 
 * 
 * </ul>
 * 
 * 
 * <p>
 * 
 * 
 * There are two possible ways in which this decoder could deal with
 * 
 * illegal strings. It could either leave illegal characters alone or
 * 
 * it could throw an <tt>{@link java.lang.IllegalArgumentException}</tt>. Which
 * approach the decoder takes is left to the implementation.
 * 
 * @author Mark Chamness
 * 
 * @author Michael McCloskey
 * 
 * @since 1.2
 */

public class URLDecoder

{

	public static String decode(String s, String enc)

	throws UnsupportedEncodingException {

		boolean needToChange = false;

		int numChars = s.length();
		StringBuffer sb = new StringBuffer(numChars > 500 ? numChars / 2
				: numChars);

		int i = 0;

		if (enc.length() == 0) {

			throw new UnsupportedEncodingException(
					"URLDecoder: empty string enc parameter");

		}

		char c;

		byte[] bytes = null;

		while (i < numChars) {
			c = s.charAt(i);

			switch (c) {

			case '+':

				sb.append(' ');

				i++;

				needToChange = true;

				break;
			case '&':
				int pos1 = i;
				String source = s.substring(pos1 + 2, pos1 + 5);
				int stringConvert = Integer.parseInt(source);
				//byte[] byteArray = source.getBytes();
				char unicodeChar = (char)stringConvert;
				//sb.append(new String(byteArray, 0, 6, enc));
				sb.append(unicodeChar);
				i += 6;
				needToChange = true;
				break;
			case '%':

				try {

					if (bytes == null)

						bytes = new byte[(numChars - i) / 3];

					int pos = 0;

					while (((i + 2) < numChars) &&

					(c == '%')) {

						int v = Integer.parseInt(s.substring(i + 1, i + 3), 16);

						if (v < 0)

							throw new IllegalArgumentException(
									"URLDecoder: Illegal hex characters in escape (%) pattern - negative value");

						bytes[pos++] = (byte) v;

						i += 3;

						if (i < numChars)

							c = s.charAt(i);

					}

					if ((i < numChars) && (c == '%'))

						throw new IllegalArgumentException(

						"URLDecoder: Incomplete trailing escape (%) pattern");

					sb.append(new String(bytes, 0, pos, enc));

				} catch (NumberFormatException e) {

					throw new IllegalArgumentException(

					"URLDecoder: Illegal hex characters in escape (%) pattern - "

					+ e.getMessage());

				}

				needToChange = true;

				break;

			default:

				sb.append(c);

				i++;

				break;

			}

		}

		return (needToChange ? sb.toString() : s);

	}

}
