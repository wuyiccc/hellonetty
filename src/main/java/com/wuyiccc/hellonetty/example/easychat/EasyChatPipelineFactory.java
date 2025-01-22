package com.wuyiccc.hellonetty.example.easychat;

import com.wuyiccc.hellonetty.channel.ChannelHandler;
import com.wuyiccc.hellonetty.channel.ChannelPipeline;
import com.wuyiccc.hellonetty.channel.ChannelPipelineFactory;
import com.wuyiccc.hellonetty.channel.Channels;
import com.wuyiccc.hellonetty.handler.codec.frame.DelimiterBasedFrameDecoder;
import com.wuyiccc.hellonetty.handler.codec.frame.Delimiters;
import com.wuyiccc.hellonetty.handler.codec.frame.FixedLengthFrameDecoder;
import com.wuyiccc.hellonetty.handler.codec.string.StringDecoder;
import com.wuyiccc.hellonetty.handler.codec.string.StringEncoder;

/**
 * @author wuyiccc
 * @date 2025/1/22 13:22
 */
public class EasyChatPipelineFactory implements ChannelPipelineFactory {

    private final ChannelHandler handler;

    public EasyChatPipelineFactory(ChannelHandler handler) {
        this.handler = handler;
    }

    @Override
    public ChannelPipeline getPipeline() throws Exception {

        ChannelPipeline pipeline = Channels.pipeline();

        pipeline.addLast("framer"
                , new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast("decoder", new StringDecoder());
        pipeline.addLast("encoder", new StringEncoder());

        pipeline.addLast("handler", handler);

        return pipeline;
    }
}
