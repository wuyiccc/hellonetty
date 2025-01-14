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
package com.wuyiccc.hellonetty.handler.codec.string;

import com.wuyiccc.hellonetty.buffer.ChannelBuffer;
import com.wuyiccc.hellonetty.channel.*;
import com.wuyiccc.hellonetty.handler.codec.frame.DelimiterBasedFrameDecoder;
import com.wuyiccc.hellonetty.handler.codec.frame.Delimiters;
import com.wuyiccc.hellonetty.handler.codec.frame.FrameDecoder;

import java.nio.charset.Charset;

import static com.wuyiccc.hellonetty.channel.Channels.*;

/**
 * Decodes a received {@link ChannelBuffer} into a {@link String}.  Please
 * note that this decoder must be used with a proper {@link FrameDecoder}
 * such as {@link DelimiterBasedFrameDecoder} if you are using a stream-based
 * transport such as TCP/IP.  A typical decoder setup for a text-based line
 * protocol in a TCP/IP socket would be:
 * <pre>
 * {@link ChannelPipeline} pipeline = ...;
 *
 * // Decoders
 * pipeline.addLast("frameDecoder", new {@link DelimiterBasedFrameDecoder}({@link Delimiters#lineDelimiter()}));
 * pipeline.addLast("stringDecoder", new {@link StringDecoder}("UTF-8"));
 *
 * // Encoder
 * pipeline.addLast("stringEncoder", new {@link StringEncoder}("UTF-8"));
 * </pre>
 * and then you can use {@link String}s instead of {@link ChannelBuffer}s
 * as a message:
 * <pre>
 * void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
 *     String msg = (String) e.getMessage();
 *     ch.write("Did you say '" + msg + "'?\n");
 * }
 * </pre>
 *
 * @author The Netty Project (netty-dev@lists.jboss.org)
 * @author Trustin Lee (tlee@redhat.com)
 *
 * @version $Rev:231 $, $Date:2008-06-12 16:44:50 +0900 (목, 12 6월 2008) $
 *
 * @apiviz.landmark
 */
@ChannelPipelineCoverage("all")
public class StringDecoder implements ChannelUpstreamHandler {

    private final String charsetName;

    /**
     * Creates a new instance with the current system character set.
     */
    public StringDecoder() {
        this(Charset.defaultCharset());
    }

    /**
     * Creates a new instance.
     *
     * @param charsetName  the name of the character set to use for decoding
     */
    public StringDecoder(String charsetName) {
        this(Charset.forName(charsetName));
    }

    /**
     * Creates a new instance.
     *
     * @param charset  the character set to use for decoding
     */
    public StringDecoder(Charset charset) {
        if (charset == null) {
            throw new NullPointerException("charset");
        }
        charsetName = charset.name();
    }

    public void handleUpstream(
            ChannelHandlerContext context, ChannelEvent evt) throws Exception {
        if (!(evt instanceof MessageEvent)) {
            context.sendUpstream(evt);
            return;
        }

        MessageEvent e = (MessageEvent) evt;
        if (!(e.getMessage() instanceof ChannelBuffer)) {
            context.sendUpstream(evt);
            return;
        }

        fireMessageReceived(
                context, e.getChannel(),
                ((ChannelBuffer) e.getMessage()).toString(charsetName));
    }
}
