package io.lightflame.websocket;

import io.lightflame.routerules.HttpRuleKind;
import io.lightflame.routerules.Rule;
import io.lightflame.routerules.RuleKind;
import io.netty.channel.ChannelHandlerContext;

/**
 * HttpPortRule
 */
public class WsPortRule implements Rule<ChannelHandlerContext> {

    private Integer port;

    public WsPortRule(int p) {
        this.port = p;
    }

    @Override
    public boolean isValid(ChannelHandlerContext ctx) {
        String port =  ctx.channel().attr(WsAttributes.portAttKey).get();
        return this.port.toString().equals(port);
    }

    @Override
    public RuleKind kind() {
        return HttpRuleKind.PORT;
    }

    @Override
    public int score() {
        return 0;
    }

    
}