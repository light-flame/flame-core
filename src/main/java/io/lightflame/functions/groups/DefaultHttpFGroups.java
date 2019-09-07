package io.lightflame.functions.groups;

import java.util.function.Function;
import io.lightflame.functions.httpAdapters.HttpInAdapter;
import io.lightflame.functions.httpAdapters.HttpOutAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * DefaultJsonFunction
 */
public class DefaultHttpFGroups {


    // public <IN,OUT> Function<FullHttpRequest,OUT> formDataConsumer(String strI, Function<String,OUT> func){
    //     HttpInAdapterFunction<String> strUnm =  new HttpInAdapter().textUnmarshall();
    //     return strUnm.andThen(func);
    // }


    // public <IN,OUT> Function<FullHttpRequest,OUT> unknowConsumer(I obj, Function<IN,OUT> func){
    //     Function<FullHttpRequest,I> jsonUnm =  new HttpInAdapter().jsonUnmarshall(clazzI);
    //     return new HttpInAdapter().unmashallGatewayOut(jsonUnm, textUnm).andThen(func);
    // }

    public <IN,OUT> Function<FullHttpRequest,OUT> jsonConsumer(Class<IN> clazzI, Function<IN,OUT> func){
        Function<FullHttpRequest,IN> jsonUnm =  new HttpInAdapter().jsonUnmarshall(clazzI);
        return jsonUnm.andThen(func);
    }

    public <IN,OUT> Function<IN,FullHttpResponse> jsonProducer(Class<OUT> clazzO, Function<IN,OUT> func){
        Function<OUT, FullHttpResponse> jsonMarsh = new HttpOutAdapter().jsonMarshall(clazzO);
        return func.andThen(jsonMarsh);
    }

    public <IN,OUT> Function<FullHttpRequest,FullHttpResponse> jsonProducerConsumer(
            Class<IN> clazzI, 
            Function<IN,OUT> func,
            Class<OUT> clazzO,
            HttpResponseStatus status
        ){
        Function<FullHttpRequest,IN> jsonUnm =  new HttpInAdapter().jsonUnmarshall(clazzI);
        Function<OUT, FullHttpResponse> jsonMarsh = new HttpOutAdapter().jsonMarshall(clazzO);
        Function<FullHttpResponse,FullHttpResponse> setStatus = new HttpOutAdapter().status(status);
        return jsonUnm.andThen(func).andThen(jsonMarsh).andThen(setStatus);
    }
}