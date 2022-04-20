/*
 * ao-encoding-servlet - High performance streaming character encoding in a Servlet environment.
 * Copyright (C) 2019, 2020, 2021, 2022  AO Industries, Inc.
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
 * along with ao-encoding-servlet.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.aoapps.encoding.servlet;

import com.aoapps.encoding.Doctype;
import com.aoapps.servlet.attribute.AttributeEE;
import com.aoapps.servlet.attribute.ScopeEE;
import java.util.Locale;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;

/**
 * @author  AO Industries, Inc.
 */
public final class DoctypeEE {

  /** Make no instances. */
  private DoctypeEE() {
    throw new AssertionError();
  }

  /**
   * Context init parameter that may be used to configure the default doctype within an application.
   */
  public static final String DEFAULT_INIT_PARAM = Doctype.class.getName() + ".default";

  /**
   * Determines the default doctype by first checking for {@linkplain ServletContext#getInitParameter(java.lang.String) context-param}
   * of {@link #DEFAULT_INIT_PARAM}, then using {@link Doctype#DEFAULT} when unspecified or "default".
   */
  public static Doctype getDefault(ServletContext servletContext) {
    String initParam = servletContext.getInitParameter(DEFAULT_INIT_PARAM);
    if (initParam != null) {
      initParam = initParam.trim();
      if (!initParam.isEmpty() && !"default".equalsIgnoreCase(initParam)) {
        return Doctype.valueOf(initParam.toUpperCase(Locale.ROOT));
      }
    }
    return Doctype.DEFAULT;
  }

  private static final ScopeEE.Request.Attribute<Doctype> REQUEST_ATTRIBUTE =
    ScopeEE.REQUEST.attribute(Doctype.class.getName());

  /**
   * Registers the doctype in effect for the request.
   */
  public static void set(ServletRequest request, Doctype doctype) {
    REQUEST_ATTRIBUTE.context(request).set(doctype);
  }

  /**
   * Replaces the doctype in effect for the request.
   *
   * @return  The previous attribute value, if any
   */
  public static Doctype replace(ServletRequest request, Doctype doctype) {
    AttributeEE.Request<Doctype> attribute = REQUEST_ATTRIBUTE.context(request);
    Doctype old = attribute.get();
    attribute.set(doctype);
    return old;
  }

  /**
   * Gets the doctype in effect for the request, or {@linkplain #getDefault(javax.servlet.ServletContext) the default}
   * when not yet {@linkplain #set(javax.servlet.ServletRequest, com.aoapps.encoding.Doctype) set}.
   * <p>
   * Once the default is resolved,
   * {@linkplain #set(javax.servlet.ServletRequest, com.aoapps.encoding.Doctype) sets the request attribute}.
   * </p>
   */
  public static Doctype get(ServletContext servletContext, ServletRequest request) {
    AttributeEE.Request<Doctype> attribute = REQUEST_ATTRIBUTE.context(request);
    Doctype doctype = attribute.get();
    if (doctype == null) {
      doctype = getDefault(servletContext);
      attribute.set(doctype);
    }
    return doctype;
  }
}
