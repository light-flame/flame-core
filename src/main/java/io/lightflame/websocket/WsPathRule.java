package io.lightflame.websocket;

import io.lightflame.routerules.HttpRuleKind;
import io.lightflame.routerules.Rule;
import io.lightflame.routerules.RuleKind;
import io.lightflame.routerules.Utils;
import io.netty.channel.ChannelHandlerContext;

/**
 * HttpPathRule
 */
public class WsPathRule implements Rule<ChannelHandlerContext> {
    private String[] segments;

    public WsPathRule(String obj) {
        this.segments = Utils.extractSegments(obj);
    }

    @Override
    public boolean isValid(ChannelHandlerContext ctx) {
        String uri = ctx.channel().attr(WsAttributes.uriAttKey).get();
        String[] incomeSegments = Utils.extractSegments(uri);

        if (incomeSegments.length != segments.length){
            return false;
        }
        for (int i=0;i < incomeSegments.length ;i++){
            String incomeSegm = incomeSegments[i];
            String conditionSegm = this.segments[i];
            if (conditionSegm.startsWith("{")){
                continue;
            }
            if (!incomeSegm.equals(conditionSegm)){
                return false;
            }
        }
        return true;
    }

    public String[] getSegments(){
        return this.segments;
    }

    @Override
    public RuleKind kind() {
        return HttpRuleKind.PATH;
    }

    @Override
    public int score() {
        return this.segments.length * 2;
    }

    
}