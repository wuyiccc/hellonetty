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
package com.wuyiccc.hellonetty.bootstrap;
import static org.easymock.EasyMock.*;

import com.wuyiccc.hellonetty.channel.Channel;
import com.wuyiccc.hellonetty.channel.ChannelException;
import com.wuyiccc.hellonetty.channel.ChannelFactory;
import com.wuyiccc.hellonetty.channel.ChannelHandlerContext;
import com.wuyiccc.hellonetty.channel.ChannelPipelineCoverage;
import com.wuyiccc.hellonetty.channel.ChannelPipelineException;
import com.wuyiccc.hellonetty.channel.ChannelPipelineFactory;
import com.wuyiccc.hellonetty.channel.ChildChannelStateEvent;
import com.wuyiccc.hellonetty.channel.SimpleChannelHandler;
import com.wuyiccc.hellonetty.channel.socket.SocketChannelConfig;
import com.wuyiccc.hellonetty.util.DummyHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;


/**
 * @author The Netty Project (netty-dev@lists.jboss.org)
 * @author Trustin Lee (tlee@redhat.com)
 *
 * @version $Rev$, $Date$
 *
 */
public abstract class AbstractSocketServerBootstrapTest {

    private static ExecutorService executor;

    @BeforeClass
    public static void init() {
        executor = Executors.newCachedThreadPool();
    }

    @AfterClass
    public static void destroy() {
        executor.shutdownNow();
        for (;;) {
            try {
                if (executor.awaitTermination(1, TimeUnit.MILLISECONDS)) {
                    break;
                }
            } catch (InterruptedException e) {
                // Ignore.
            }
        }
    }

    protected abstract ChannelFactory newServerSocketChannelFactory(Executor executor);

    @Test(timeout = 10000, expected = ChannelException.class)
    public void testFailedBindAttempt() throws Exception {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.setFactory(newServerSocketChannelFactory(executor));
        bootstrap.setOption("localAddress", new InetSocketAddress("255.255.255.255", 0));
        bootstrap.bind();
    }

    @Test(timeout = 10000)
    public void testSuccessfulBindAttempt() throws Exception {
        ServerBootstrap bootstrap = new ServerBootstrap(
                newServerSocketChannelFactory(executor));

        bootstrap.setParentHandler(new ParentChannelHandler());
        bootstrap.setOption("localAddress", new InetSocketAddress(0));
        bootstrap.setOption("child.receiveBufferSize", 9753);
        bootstrap.setOption("child.sendBufferSize", 8642);

        bootstrap.getPipeline().addLast("dummy", new DummyHandler());

        Channel channel = bootstrap.bind();
        ParentChannelHandler pch =
            channel.getPipeline().get(ParentChannelHandler.class);

        Socket socket = null;
        try {
            socket = new Socket(
                    InetAddress.getLocalHost(),
                    ((InetSocketAddress) channel.getLocalAddress()).getPort());

            // Wait until the connection is open in the server side.
            while (pch.child == null) {
                Thread.yield();
            }

            SocketChannelConfig cfg = (SocketChannelConfig) pch.child.getConfig();
            assertEquals(9753, cfg.getReceiveBufferSize());
            assertEquals(8642, cfg.getSendBufferSize());
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // Ignore.
                }
            }
            channel.close().awaitUninterruptibly();
        }

        // Wait until the child connection is closed in the client side.
        // We don't use Channel.close() to make sure it's closed automatically.
        while (pch.child.isOpen()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                // Ignore
            }
        }

        // Wait until all child events are fired.
        while (pch.result.length() < 2) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                // Ignore
            }
        }

        // Confirm the received child events.
        assertEquals("12", pch.result.toString());
    }

    @Test(expected = ChannelPipelineException.class)
    public void testFailedPipelineInitialization() throws Exception {
        ClientBootstrap bootstrap = new ClientBootstrap(createMock(ChannelFactory.class));
        ChannelPipelineFactory pipelineFactory = createMock(ChannelPipelineFactory.class);
        bootstrap.setPipelineFactory(pipelineFactory);

        expect(pipelineFactory.getPipeline()).andThrow(new ChannelPipelineException());
        replay(pipelineFactory);

        bootstrap.connect(new InetSocketAddress(InetAddress.getLocalHost(), 1));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldHaveLocalAddressOption() {
        new ServerBootstrap(createMock(ChannelFactory.class)).bind();
    }


    @Test(expected = NullPointerException.class)
    public void shouldDisallowNullLocalAddressParameter() {
        new ServerBootstrap(createMock(ChannelFactory.class)).bind(null);
    }

    @ChannelPipelineCoverage("all")
    private static class ParentChannelHandler extends SimpleChannelHandler {

        volatile Channel child;
        final StringBuffer result = new StringBuffer();

        ParentChannelHandler() {
            super();
        }

        @Override
        public void childChannelClosed(ChannelHandlerContext ctx,
                ChildChannelStateEvent e) throws Exception {
            result.append('2');
        }

        @Override
        public void childChannelOpen(ChannelHandlerContext ctx,
                ChildChannelStateEvent e) throws Exception {
            child = e.getChildChannel();
            result.append('1');
        }
    }
}
