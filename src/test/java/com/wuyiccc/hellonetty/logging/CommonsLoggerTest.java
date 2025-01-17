/*
 * JBoss, Home of Professional Open Source
 *
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * by the @author tags. See the COPYRIGHT.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.wuyiccc.hellonetty.logging;

import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertTrue;


/**
 * @author The Netty Project (netty-dev@lists.jboss.org)
 * @author Trustin Lee (tlee@redhat.com)
 *
 * @version $Rev$, $Date$
 *
 */
public class CommonsLoggerTest {
    private static final Exception e = new Exception();

    @Test
    public void testIsDebugEnabled() {
        org.apache.commons.logging.Log mock =
            createStrictMock(org.apache.commons.logging.Log.class);

        expect(mock.isDebugEnabled()).andReturn(true);
        replay(mock);

        InternalLogger logger = new CommonsLogger(mock, "foo");
        assertTrue(logger.isDebugEnabled());
        verify(mock);
    }

    @Test
    public void testIsInfoEnabled() {
        org.apache.commons.logging.Log mock =
            createStrictMock(org.apache.commons.logging.Log.class);

        expect(mock.isInfoEnabled()).andReturn(true);
        replay(mock);

        InternalLogger logger = new CommonsLogger(mock, "foo");
        assertTrue(logger.isInfoEnabled());
        verify(mock);
    }

    @Test
    public void testIsWarnEnabled() {
        org.apache.commons.logging.Log mock =
            createStrictMock(org.apache.commons.logging.Log.class);

        expect(mock.isWarnEnabled()).andReturn(true);
        replay(mock);

        InternalLogger logger = new CommonsLogger(mock, "foo");
        assertTrue(logger.isWarnEnabled());
        verify(mock);
    }

    @Test
    public void testIsErrorEnabled() {
        org.apache.commons.logging.Log mock =
            createStrictMock(org.apache.commons.logging.Log.class);

        expect(mock.isErrorEnabled()).andReturn(true);
        replay(mock);

        InternalLogger logger = new CommonsLogger(mock, "foo");
        assertTrue(logger.isErrorEnabled());
        verify(mock);
    }

    @Test
    public void testDebug() {
        org.apache.commons.logging.Log mock =
            createStrictMock(org.apache.commons.logging.Log.class);

        mock.debug("a");
        replay(mock);

        InternalLogger logger = new CommonsLogger(mock, "foo");
        logger.debug("a");
        verify(mock);
    }

    @Test
    public void testDebugWithException() {
        org.apache.commons.logging.Log mock =
            createStrictMock(org.apache.commons.logging.Log.class);

        mock.debug("a", e);
        replay(mock);

        InternalLogger logger = new CommonsLogger(mock, "foo");
        logger.debug("a", e);
        verify(mock);
    }

    @Test
    public void testInfo() {
        org.apache.commons.logging.Log mock =
            createStrictMock(org.apache.commons.logging.Log.class);

        mock.info("a");
        replay(mock);

        InternalLogger logger = new CommonsLogger(mock, "foo");
        logger.info("a");
        verify(mock);
    }

    @Test
    public void testInfoWithException() {
        org.apache.commons.logging.Log mock =
            createStrictMock(org.apache.commons.logging.Log.class);

        mock.info("a", e);
        replay(mock);

        InternalLogger logger = new CommonsLogger(mock, "foo");
        logger.info("a", e);
        verify(mock);
    }

    @Test
    public void testWarn() {
        org.apache.commons.logging.Log mock =
            createStrictMock(org.apache.commons.logging.Log.class);

        mock.warn("a");
        replay(mock);

        InternalLogger logger = new CommonsLogger(mock, "foo");
        logger.warn("a");
        verify(mock);
    }

    @Test
    public void testWarnWithException() {
        org.apache.commons.logging.Log mock =
            createStrictMock(org.apache.commons.logging.Log.class);

        mock.warn("a", e);
        replay(mock);

        InternalLogger logger = new CommonsLogger(mock, "foo");
        logger.warn("a", e);
        verify(mock);
    }

    @Test
    public void testError() {
        org.apache.commons.logging.Log mock =
            createStrictMock(org.apache.commons.logging.Log.class);

        mock.error("a");
        replay(mock);

        InternalLogger logger = new CommonsLogger(mock, "foo");
        logger.error("a");
        verify(mock);
    }

    @Test
    public void testErrorWithException() {
        org.apache.commons.logging.Log mock =
            createStrictMock(org.apache.commons.logging.Log.class);

        mock.error("a", e);
        replay(mock);

        InternalLogger logger = new CommonsLogger(mock, "foo");
        logger.error("a", e);
        verify(mock);
    }
}
