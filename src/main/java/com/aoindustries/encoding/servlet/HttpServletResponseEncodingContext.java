/*
 * ao-encoding-servlet - High performance streaming character encoding in a Servlet environment.
 * Copyright (C) 2016, 2019  AO Industries, Inc.
 *     support@aoindustries.com
 *     7262 Bull Pen Cir
 *     Mobile, AL 36695
 *
 * This file is part of ao-encoding-servlet.
 *
 * ao-encoding-servlet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ao-encoding-servlet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ao-encoding-servlet.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aoindustries.encoding.servlet;

import com.aoindustries.encoding.EncodingContext;
import com.aoindustries.net.UrlUtils;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletResponse;

/**
 * Encoding being done within a servlet context.
 *
 * @author  AO Industries, Inc.
 */
public class HttpServletResponseEncodingContext implements EncodingContext {

	private final HttpServletResponse response;

	public HttpServletResponseEncodingContext(HttpServletResponse response) {
		this.response = response;
	}

	/**
	 * Encodes the URL by:
	 * <ol>
	 * <li>Encoding the path elements with the current response character encoding.</li>
	 * <li>Calling response.encodeURL()</li>
	 * </ol>
	 * @see  UrlUtils#encodeUrlPath(java.lang.String, java.lang.String)
	 * @see  HttpServletResponse#encodeRedirectURL(java.lang.String)
	 */
	@Override
	public String encodeURL(String href) throws UnsupportedEncodingException {
		return response.encodeURL(
			UrlUtils.encodeUrlPath(
				href,
				response.getCharacterEncoding()
			)
		);
	}
}
