/*
 * ao-encoding-servlet - High performance streaming character encoding in a Servlet environment.
 * Copyright (C) 2016, 2019, 2020, 2021, 2022  AO Industries, Inc.
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
import com.aoapps.encoding.EncodingContext;
import com.aoapps.encoding.Serialization;
import com.aoapps.net.URIEncoder;
import java.nio.charset.Charset;
import javax.servlet.ServletContext;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Encoding being done within a servlet context.
 *
 * @author  AO Industries, Inc.
 */
public class EncodingContextEE implements EncodingContext {

  private final HttpServletResponse response;
  private final Doctype doctype;
  private final Serialization serialization;
  private final Charset characterEncoding;

  /**
   * Uses the provided doctype, serialization, and character encoding.
   * <p>
   * Changes on request or response will not be detected and should not be done.  Please ensure doctype, serialization,
   * and encoding are all set on the request and response.
   * </p>
   */
  public EncodingContextEE(
      Doctype doctype,
      Serialization serialization,
      Charset characterEncoding,
      HttpServletResponse response
  ) {
    this.response = response;
    this.doctype = doctype;
    this.serialization = serialization;
    this.characterEncoding = characterEncoding;
  }

  /**
   * The values for {@link Doctype}, {@link Serialization}, and {@link Charset} are only looked-up once and cached.
   * <p>
   * Changes on request or response will not be detected and should not be done.  Please ensure doctype, serialization,
   * and encoding are all set on the request and response.
   * </p>
   *
   * @see  DoctypeEE#get(javax.servlet.ServletContext, javax.servlet.ServletRequest)
   * @see  SerializationEE#get(javax.servlet.ServletContext, javax.servlet.http.HttpServletRequest)
   * @see  ServletResponse#getCharacterEncoding()
   * @see  Charset#forName(java.lang.String)
   */
  public EncodingContextEE(ServletContext servletContext, HttpServletRequest request, HttpServletResponse response) {
    this(
        DoctypeEE.get(servletContext, request),
        SerializationEE.get(servletContext, request),
        Charset.forName(response.getCharacterEncoding()),
        response
    );
  }

  /**
   * {@inheritDoc}
   * <p>
   * Encodes the URL via {@link URIEncoder#encodeURI(java.lang.String)} then {@link HttpServletResponse#encodeURL(java.lang.String)}.
   * </p>
   */
  @Override
  public String encodeURL(String url) {
    return response.encodeURL(
        URIEncoder.encodeURI(url)
    );
  }

  /**
   * {@inheritDoc}
   * <p>
   * Uses the cached value from construction time.
   * </p>
   */
  @Override
  public Doctype getDoctype() {
    return doctype;
  }

  /**
   * {@inheritDoc}
   * <p>
   * Uses the cached value from construction time.
   * </p>
   */
  @Override
  public Serialization getSerialization() {
    return serialization;
  }

  /**
   * {@inheritDoc}
   * <p>
   * Uses the cached value from construction time.
   * </p>
   */
  @Override
  public Charset getCharacterEncoding() {
    return characterEncoding;
  }
}
