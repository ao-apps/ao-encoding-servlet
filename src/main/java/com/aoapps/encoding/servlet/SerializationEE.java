/*
 * ao-encoding-servlet - High performance streaming character encoding in a Servlet environment.
 * Copyright (C) 2019, 2020, 2021  AO Industries, Inc.
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
package com.aoapps.encoding.servlet;

import com.aoapps.encoding.Serialization;
import static com.aoapps.encoding.Serialization.SGML;
import static com.aoapps.encoding.Serialization.XML;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * @author  AO Industries, Inc.
 */
final public class SerializationEE {

	// Make no instances
	private SerializationEE() {}

	/**
	 * Context init parameter that may be used to configure the use of XHTML within an application.
	 * Must be one of "SGML", "XML", or "auto" (the default).
	 */
	public static final String DEFAULT_INIT_PARAM = Serialization.class.getName() + ".default";

	/**
	 * Determine if the content may be served as <code>application/xhtml+xml</code> by the
	 * rules defined in <a href="http://www.w3.org/TR/xhtml-media-types/">http://www.w3.org/TR/xhtml-media-types/</a>
	 * Default to <code>application/xhtml+xml</code> as discussed at
	 * <a href="https://web.archive.org/web/20080913043830/http://www.smackthemouse.com/xhtmlxml">http://www.smackthemouse.com/xhtmlxml</a>
	 */
	public static Serialization getDefault(ServletContext servletContext, HttpServletRequest request) {
		String initParam = servletContext.getInitParameter(DEFAULT_INIT_PARAM);
		if(initParam != null) {
			initParam = initParam.trim();
			if(!initParam.isEmpty()) {
				if("SGML".equalsIgnoreCase(initParam)) {
					return SGML;
				} else if("XML".equalsIgnoreCase(initParam)) {
					return XML;
				} else if(!"auto".equalsIgnoreCase(initParam)) {
					throw new IllegalArgumentException("Unexpected value for " + DEFAULT_INIT_PARAM + ": Must be one of \"SGML\", \"XML\", or \"auto\": " + initParam);
				}
			}
		}
		return Serialization.select(request.getHeaders("Accept"));
	}

	private static final String REQUEST_ATTRIBUTE = Serialization.class.getName();

	/**
	 * Registers the serialization in effect for the request.
	 */
	public static void set(ServletRequest request, Serialization serialization) {
		request.setAttribute(REQUEST_ATTRIBUTE, serialization);
	}

	/**
	 * Replaces the serialization in effect for the request.
	 *
	 * @return  The previous attribute value, if any
	 */
	public static Serialization replace(ServletRequest request, Serialization serialization) {
		Serialization old = (Serialization)request.getAttribute(REQUEST_ATTRIBUTE);
		request.setAttribute(REQUEST_ATTRIBUTE, serialization);
		return old;
	}

	/**
	 * Gets the serialization in effect for the request, or {@linkplain #getDefault(javax.servlet.ServletContext, javax.servlet.http.HttpServletRequest) the default}
	 * when not yet {@linkplain #set(javax.servlet.ServletRequest, com.aoapps.encoding.Serialization) set}.
	 * <p>
	 * Once the default is resolved,
	 * {@linkplain #set(javax.servlet.ServletRequest, com.aoapps.encoding.Serialization) sets the request attribute}.
	 * </p>
	 */
	public static Serialization get(ServletContext servletContext, HttpServletRequest request) {
		Serialization serialization = (Serialization)request.getAttribute(REQUEST_ATTRIBUTE);
		if(serialization == null) {
			serialization = getDefault(servletContext, request);
			request.setAttribute(REQUEST_ATTRIBUTE, serialization);
		}
		return serialization;
	}
}
