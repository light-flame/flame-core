package io.lightflame.store;

import java.util.HashMap;
import java.util.Map;

import io.lightflame.context.FlameHttpContext;
import io.lightflame.functions.FlameHttpFunction;
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

    static private Map<HttpUrlScore, FlameHttpFunction> functionMap = new HashMap<>();

    private String prefix = "";

    public FlameHttpStore() {
    }

    public FlameHttpStore(String prefix) {
        this.prefix = prefix;
    }

    public FlameHttpContext runFunctionByRequest(FullHttpRequest request) throws Exception{
        FlameHttpFunction function = handler404();
        String rawUri = "";
        int finalScore = 0;

        for (Map.Entry<HttpUrlScore,FlameHttpFunction> entry : functionMap.entrySet()){
            int score = entry.getKey().getScore(request.uri(), request.method().name());
            if (score > finalScore){
                function = entry.getValue();
                rawUri = entry.getKey().getRawConditionURI();
                finalScore = score;
            }
        }

        FlameHttpContext ctx = new FlameHttpContext(request, new FlameHttpUtils(rawUri));
        return function.chain(ctx);
    }

    public void httpGET(String url, FlameHttpFunction function){
        functionMap.put(new HttpUrlScore(this.prefix + url, HttpMethod.GET.name()), function);
    }


    public void httpPOST(String url, FlameHttpFunction function){
        functionMap.put(new HttpUrlScore(this.prefix + url, HttpMethod.POST.name()), function);
    }

    private FlameHttpFunction handler404() {
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