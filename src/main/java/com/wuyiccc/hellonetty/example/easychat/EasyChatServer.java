package com.wuyiccc.hellonetty.example.easychat;

import com.wuyiccc.hellonetty.bootstrap.ServerBootstrap;
import com.wuyiccc.hellonetty.channel.ChannelFactory;
import com.wuyiccc.hellonetty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * @author wuyiccc
 * @date 2025/1/22 13:40
 */
public class EasyChatServer {

    public static void main(String[] args) {

        ChannelFactory factory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool()
        , Executors.newCachedThreadPool());

        ServerBootstrap bootstrap = new ServerBootstrap(factory);

        EasyChatServerHandler handler = new EasyChatServerHandler();

        bootstrap.setPipelineFactory(new EasyChatPipelineFactory(handler));
        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);
        bootstrap.setOption("backlog", 2);

        bootstrap.bind(new InetSocketAddress(8080));
    }
}
