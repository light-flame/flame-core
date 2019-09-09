package io.lightflame.store;

import org.junit.Test;

/**
 * FlameHttpStoreTest
 */
public class FlameHttpStoreTest {

    @Test
    public void testUrlsConditions(){

        HttpUrlScore path = new HttpUrlScore("/path/to/url", "GET");

        path.getScore("/*", "GET");
        path.getScore("/path/*", "GET");
        path.getScore("/path/to/{url}", "GET");
        path.getScore("/path/to/url", "GET");
        path.getScore("/path/to/url/{bigger}", "GET");
        path.getScore("/path/to/url/bigger", "GET");
        
    }




    // public Function<FullHttpRequest,FullHttpResponse> p1() {
    //     return (request) -> {
    //         String name = request.content().toString(CharsetUtil.UTF_8);
    //         String greeting = String.format("hello %s", name);
    //         return new DefaultFullHttpResponse(
    //             HTTP_1_1,OK, Unpooled.copiedBuffer(greeting, CharsetUtil.UTF_8));
    //     };
    // }
}