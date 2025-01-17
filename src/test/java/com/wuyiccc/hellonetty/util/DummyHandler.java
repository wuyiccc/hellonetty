package com.wuyiccc.hellonetty.util;

import com.wuyiccc.hellonetty.channel.ChannelDownstreamHandler;
import com.wuyiccc.hellonetty.channel.ChannelEvent;
import com.wuyiccc.hellonetty.channel.ChannelHandlerContext;
import com.wuyiccc.hellonetty.channel.ChannelPipelineCoverage;
import com.wuyiccc.hellonetty.channel.ChannelUpstreamHandler;

/**
 * A dummy handler for a testing purpose.
 *
 * @author The Netty Project (netty-dev@lists.jboss.org)
 * @author Trustin Lee (tlee@redhat.com)
 *
 * @version $Rev$, $Date$
 */
@ChannelPipelineCoverage("all")
public class DummyHandler implements ChannelUpstreamHandler, ChannelDownstreamHandler {

    public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
            throws Exception {
        ctx.sendUpstream(e);
    }

    public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent e)
            throws Exception {
        ctx.sendDownstream(e);
    }
}
