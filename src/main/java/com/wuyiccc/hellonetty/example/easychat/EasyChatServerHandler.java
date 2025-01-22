package com.wuyiccc.hellonetty.example.easychat;

import com.wuyiccc.hellonetty.channel.Channel;
import com.wuyiccc.hellonetty.channel.ChannelEvent;
import com.wuyiccc.hellonetty.channel.ChannelHandlerContext;
import com.wuyiccc.hellonetty.channel.ChannelPipelineCoverage;
import com.wuyiccc.hellonetty.channel.ChannelStateEvent;
import com.wuyiccc.hellonetty.channel.ExceptionEvent;
import com.wuyiccc.hellonetty.channel.MessageEvent;
import com.wuyiccc.hellonetty.channel.SimpleChannelHandler;
import com.wuyiccc.hellonetty.util.MapBackedSet;

import java.net.InetAddress;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author wuyiccc
 * @date 2025/1/22 13:42
 */
@ChannelPipelineCoverage("all")
public class EasyChatServerHandler extends SimpleChannelHandler {

    private static final Logger logger = Logger.getLogger(
            EasyChatServerHandler.class.getName()
    );

    static final Set<Channel> channels = new MapBackedSet<>(new ConcurrentHashMap<Channel, Boolean>());

    @Override
    public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {

        if (e instanceof ChannelStateEvent) {
            logger.info(e.toString());
        }
        super.handleUpstream(ctx, e);
    }


    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {


        ctx.getPipeline().getChannel().write("Welcome to " + InetAddress.getLocalHost().getHostName()
            + " secure chat service!\n");
        channels.add(ctx.getPipeline().getChannel());
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {

        channels.remove(e.getChannel());
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {

        String request = (String) e.getMessage();

        logger.log(Level.INFO, request);
        for (Channel c : channels) {

            if (c != e.getChannel()) {
                c.write("[" + e.getChannel().getRemoteAddress() + "] " + request + "\n");
            }
        }

        if (request.toLowerCase().equals("bye")) {
            e.getChannel().close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {

        logger.log(Level.WARNING, "Unexpected exception from downstream.", e.getCause());
        e.getChannel().close();
    }
}
