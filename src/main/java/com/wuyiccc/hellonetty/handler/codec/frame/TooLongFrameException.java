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
package com.wuyiccc.hellonetty.handler.codec.frame;

/**
 * An {@link Exception} which is thrown when the length of the frame
 * decoded by {@link DelimiterBasedFrameDecoder} is greater than the maximum.
 *
 * @author The Netty Project (netty-dev@lists.jboss.org)
 * @author Trustin Lee (tlee@redhat.com)
 *
 * @version $Rev:231 $, $Date:2008-06-12 16:44:50 +0900 (목, 12 6월 2008) $
 *
 * @apiviz.hidden
 */
public class TooLongFrameException extends Exception {

    private static final long serialVersionUID = -1995801950698951640L;

    /**
     * Creates a new instance.
     */
    public TooLongFrameException() {
        super();
    }

    /**
     * Creates a new instance.
     */
    public TooLongFrameException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new instance.
     */
    public TooLongFrameException(String message) {
        super(message);
    }

    /**
     * Creates a new instance.
     */
    public TooLongFrameException(Throwable cause) {
        super(cause);
    }
}
