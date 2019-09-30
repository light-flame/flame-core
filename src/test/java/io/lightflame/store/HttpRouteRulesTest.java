package io.lightflame.store;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import io.lightflame.routerules.*;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;

import java.util.UUID;


/**
 * HttpRouteRulesTest
 */
public class HttpRouteRulesTest {

    
    @Test
    public void testcase0(){

        RouteStore<FullHttpRequest> rs = new RouteStore<>();

        UUID r1 =  UUID.randomUUID();
        rs.addRouteRule(
            new RouteRules<FullHttpRequest>(r1)
                .addRule(new HttpPathRule("/path/to/my"))
        );

        UUID r2 =  UUID.randomUUID();
        rs.addRouteRule(
            new RouteRules<FullHttpRequest>(r2)
                .addRule(new HttpPathRule("/path/to/my"))
                .addRule(new HttpMethodRule(HttpMethod.GET))
        );

        UUID r3 =  UUID.randomUUID();
        rs.addRouteRule(
            new RouteRules<FullHttpRequest>(r3)
                .addRule(new HttpPathRule("/path/to/my"))
                .addRule(new HttpMethodRule(HttpMethod.GET))
                .addRule(new HttpQParamRule("name", "carol"))
        );

        UUID r4 =  UUID.randomUUID();
        rs.addRouteRule(
            new RouteRules<FullHttpRequest>(r4)
                .addRule(new HttpPathRule("/path/to/my"))
                .addRule(new HttpMethodRule(HttpMethod.GET))
                .addRule(new HttpQParamRule("name", "carol"))
                .addRule(new HttpHeaderRule("x-auth", "xlz"))
        );

        UUID r5 =  UUID.randomUUID();
        rs.addRouteRule(
            new RouteRules<FullHttpRequest>(r5)
                .addRule(new HttpPathRule("/path/to/my/url"))
                .addRule(new HttpMethodRule(HttpMethod.GET))
        );

        UUID r6 =  UUID.randomUUID();
        rs.addRouteRule(
            new RouteRules<FullHttpRequest>(r6)
                .addRule(new HttpPrefixPathRule("/path/to/*"))
                .addRule(new HttpMethodRule(HttpMethod.POST))
        );

        UUID r7 =  UUID.randomUUID();
        rs.addRouteRule(
            new RouteRules<FullHttpRequest>(r7)
                .addRule(new HttpPrefixPathRule("/path/to/*"))
        );

        UUID r8 =  UUID.randomUUID();
        rs.addRouteRule(
            new RouteRules<FullHttpRequest>(r8)
                .addRule(new HttpPathRule("/path/to/my/url/{param}"))
        );


        FullHttpRequest request;

        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/path/to/my?name=daniel");
        request.headers().set("x-auth", "abc");
        assertEquals(r2, rs.getRouteRules(request).getKey());

        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/path/to/my?name=carol");
        request.headers().set("x-auth", "abc");
        assertEquals(r3, rs.getRouteRules(request).getKey());

        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/path/to/my?name=carol");
        request.headers().set("x-auth", "xlz");
        assertEquals(r4, rs.getRouteRules(request).getKey());

        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/path/to/my/url?name=carol");
        request.headers().set("x-auth", "xlz");
        assertEquals(r5, rs.getRouteRules(request).getKey());

        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/path/to/my/life?name=carol");
        request.headers().set("x-auth", "xlz");
        assertEquals(r6, rs.getRouteRules(request).getKey());

        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/path/to/my/life?name=carol");
        request.headers().set("x-auth", "xlz");
        assertEquals(r7, rs.getRouteRules(request).getKey());

        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/path/to/my/url/bigger/more?name=carol");
        request.headers().set("x-auth", "xlz");
        assertEquals(r7, rs.getRouteRules(request).getKey());

        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/path/to/my/url/bigger?name=carol");
        request.headers().set("x-auth", "xlz");
        assertEquals(r8, rs.getRouteRules(request).getKey());
        
    }

    @Test
    public void testcase2(){

        RouteStore<FullHttpRequest> rs = new RouteStore<>();

        UUID r1 =  UUID.randomUUID();
        rs.addRouteRule(
            new RouteRules<FullHttpRequest>(r1)
                .addRule(new HttpPrefixPathRule("/*"))
        );

        UUID r2 =  UUID.randomUUID();
        rs.addRouteRule(
            new RouteRules<FullHttpRequest>(r2)
                .addRule(new HttpPrefixPathRule("/path/*"))
        );


        FullHttpRequest request;

        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/");
        request.headers().set("x-auth", "abc");
        assertEquals(r1, rs.getRouteRules(request).getKey());

        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/path");
        request.headers().set("x-auth", "abc");
        assertEquals(r2, rs.getRouteRules(request).getKey());

        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/path/to/my");
        request.headers().set("x-auth", "abc");
        assertEquals(r2, rs.getRouteRules(request).getKey());
    }

    @Test
    public void testcase3(){

        RouteStore<FullHttpRequest> rs = new RouteStore<>();

        UUID r1 =  UUID.randomUUID();
        rs.addRouteRule(
            new RouteRules<FullHttpRequest>(r1)
                .addRule(new HttpPrefixPathRule("/*"))
        );



        FullHttpRequest request;

        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/");
        assertEquals(r1, rs.getRouteRules(request).getKey());

        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/abcd");
        assertEquals(r1, rs.getRouteRules(request).getKey());


    }
}