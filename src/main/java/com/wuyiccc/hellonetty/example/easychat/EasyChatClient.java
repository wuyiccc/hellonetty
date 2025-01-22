package com.wuyiccc.hellonetty.example.easychat;

import com.wuyiccc.hellonetty.bootstrap.ClientBootstrap;
import com.wuyiccc.hellonetty.channel.Channel;
import com.wuyiccc.hellonetty.channel.ChannelFactory;
import com.wuyiccc.hellonetty.channel.ChannelFuture;
import com.wuyiccc.hellonetty.channel.socket.nio.NioClientSocketChannelFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * @author wuyiccc
 * @date 2025/1/22 14:05
 */
public class EasyChatClient {

    public static void main(String[] args) throws IOException {

        ChannelFactory factory = new NioClientSocketChannelFactory(Executors.newCachedThreadPool()
        , Executors.newCachedThreadPool());

        ClientBootstrap bootstrap = new ClientBootstrap(factory);

        EasyChatClientHandler handler = new EasyChatClientHandler();

        bootstrap.setPipelineFactory(new EasyChatPipelineFactory(handler));
        bootstrap.setOption("tcpNoDelay", true);
        bootstrap.setOption("keepAlive", true);


        ChannelFuture future = bootstrap.connect(new InetSocketAddress("localhost", 8080));

        Channel channel = future.awaitUninterruptibly().getChannel();
        if (!future.isSuccess()) {

            future.getCause().printStackTrace();
            System.exit(0);
        }

        ChannelFuture lastWriteFuture = null;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        for (;;) {

            String line = in.readLine();
            if (line == null) {
                break;
            }
            lastWriteFuture = channel.write(line + "\n");
        }

        if (lastWriteFuture != null) {
            lastWriteFuture.awaitUninterruptibly();
        }

        channel.close().awaitUninterruptibly();


        System.exit(0);
    }
}
