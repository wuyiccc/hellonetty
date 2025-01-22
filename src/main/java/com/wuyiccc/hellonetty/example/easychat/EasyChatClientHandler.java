package com.wuyiccc.hellonetty.example.easychat;

import com.wuyiccc.hellonetty.channel.ChannelEvent;
import com.wuyiccc.hellonetty.channel.ChannelHandlerContext;
import com.wuyiccc.hellonetty.channel.ChannelPipelineCoverage;
import com.wuyiccc.hellonetty.channel.ChannelStateEvent;
import com.wuyiccc.hellonetty.channel.ExceptionEvent;
import com.wuyiccc.hellonetty.channel.MessageEvent;
import com.wuyiccc.hellonetty.channel.SimpleChannelHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author wuyiccc
 * @date 2025/1/22 13:59
 */
@ChannelPipelineCoverage("all")
public class EasyChatClientHandler extends SimpleChannelHandler {

    private static final Logger logger = Logger.getLogger(EasyChatClientHandler.class.getName());

    @Override
    public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {

        if (e instanceof ChannelStateEvent) {
            logger.info(e.toString());
        }
        super.handleUpstream(ctx, e);
    }


    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {

        logger.info(">>> 我已经连接上服务端了");
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {

        System.err.println(e.getMessage());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {

        logger.log(Level.WARNING, "Unexpected exception from downstream", e.getCause());
        e.getChannel().close();
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {

        logger.log(Level.WARNING, "我断开连接了");
    }
}
