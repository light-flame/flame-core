package io.lightflame.store;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.lightflame.context.FlameHttpContext;
import io.lightflame.util.FlameHttpUtils;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderValues.TEXT_PLAIN;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;


/**
 * HttpBeanStore
 */
public class FlameHttpStore {

    static private Map<HttpUrlScore, Function<FlameHttpContext, FlameHttpContext>> functionMap = new HashMap<>();

    private String prefix = "";

    public FlameHttpStore() {
    }

    public FlameHttpStore(String prefix) {
        this.prefix = prefix;
    }

    public FlameHttpContext runFunctionByRequest(FullHttpRequest request){
        Function<FlameHttpContext, FlameHttpContext> function = handler404();
        String rawUri = "";
        int finalScore = 0;

        for (Map.Entry<HttpUrlScore,Function<FlameHttpContext, FlameHttpContext>> entry : functionMap.entrySet()){
            int score = entry.getKey().getScore(request.uri(), request.method().name());
            if (score > finalScore){
                function = entry.getValue();
                rawUri = entry.getKey().getRawConditionURI();
                finalScore = score;
            }
        }

        FlameHttpContext ctx = new FlameHttpContext(request, new FlameHttpUtils(rawUri));
        return function.apply(ctx);
    }

    public void httpGET(String url, Function<FlameHttpContext, FlameHttpContext> function){
        functionMap.put(new HttpUrlScore(this.prefix + url, HttpMethod.GET.name()), function);
    }


    public void httpPOST(String url, Function<FlameHttpContext, FlameHttpContext> function){
        functionMap.put(new HttpUrlScore(this.prefix + url, HttpMethod.POST.name()), function);
    }

    private Function<FlameHttpContext, FlameHttpContext> handler404() {
        return (ctx) -> {
            FullHttpResponse response = new DefaultFullHttpResponse(
                ctx.getRequest().protocolVersion(), 
                OK,
                Unpooled.wrappedBuffer("nothing here.. =(".getBytes()));
            response.headers()
                    .set(CONTENT_TYPE, TEXT_PLAIN)
                    .setInt(CONTENT_LENGTH, response.content().readableBytes());
            ctx.setResponse(response);
            return ctx;
        };
    }
    
}