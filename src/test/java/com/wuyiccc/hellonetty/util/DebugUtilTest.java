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
package com.wuyiccc.hellonetty.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.Permission;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * @author The Netty Project (netty-dev@lists.jboss.org)
 * @author Trustin Lee (tlee@redhat.com)
 *
 * @version $Rev$, $Date$
 *
 */
public class DebugUtilTest {

    public void shouldReturnFalseIfPropertyIsNotSet() {
        assertFalse(DebugUtil.isDebugEnabled());
    }

    @Test
    public void shouldReturnTrueInDebugMode() {
        System.setProperty("com.wuyiccc.hellonetty.debug", "true");
        assertTrue(DebugUtil.isDebugEnabled());
    }

    @Test
    public void shouldReturnFalseInNonDebugMode() {
        System.setProperty("com.wuyiccc.hellonetty.debug", "false");
        assertFalse(DebugUtil.isDebugEnabled());
    }

    @Test
    public void shouldNotBombOutWhenSecurityManagerIsInAction() {
        System.setProperty("com.wuyiccc.hellonetty.debug", "true");
        System.setSecurityManager(new SecurityManager() {
            @Override
            public void checkPropertyAccess(String key) {
                throw new SecurityException();
            }

            @Override
            public void checkPermission(Permission perm, Object context) {
                // Allow
            }

            @Override
            public void checkPermission(Permission perm) {
                // Allow
            }

        });
        try {
            assertFalse(DebugUtil.isDebugEnabled());
        } finally {
            System.setSecurityManager(null);
        }
    }

    @Before @After
    public void cleanup() {
        System.clearProperty("com.wuyiccc.hellonetty.debug");
    }
}
